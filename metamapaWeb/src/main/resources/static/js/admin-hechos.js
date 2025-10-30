document.addEventListener('DOMContentLoaded', () => {
    const main = document.querySelector('.admin-main-content');
    const list = document.getElementById('hechos-list');
    if (!list) return;

    // CSRF (Spring Security)
    const csrfToken = document.querySelector('meta[name="_csrf"]')?.content || null;
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]')?.content || 'X-CSRF-TOKEN';

    // Buscar
    const searchInput = document.getElementById('search-input');
    const doFilter = () => {
        const q = (searchInput.value || '').trim().toLowerCase();
        list.querySelectorAll('.hecho-card').forEach(card => {
            const nombre = (card.dataset.nombre || '').toLowerCase();
            const desc = (card.dataset.descripcion || '').toLowerCase();
            card.style.display = (nombre.includes(q) || desc.includes(q)) ? '' : 'none';
        });
    };
    searchInput?.addEventListener('input', doFilter);
    document.getElementById('search-btn')?.addEventListener('click', (e) => { e.preventDefault(); doFilter(); });

    // Modal delete (con fallback)
    const deleteModal = document.getElementById('delete-modal');
    const btnCancelDelete = document.getElementById('cancel-delete');
    const btnConfirmDelete = document.getElementById('confirm-delete');
    let pendingDeleteForm = null;

    const openDeleteModal = (form) => {
        if (!deleteModal) {
            if (window.confirm('¿Confirmar eliminación?')) form.submit();
            return;
        }
        pendingDeleteForm = form;
        deleteModal.classList.add('active');
    };
    const closeDeleteModal = () => { pendingDeleteForm = null; deleteModal?.classList.remove('active'); };
    btnCancelDelete?.addEventListener('click', closeDeleteModal);
    btnConfirmDelete?.addEventListener('click', () => { if (pendingDeleteForm) pendingDeleteForm.submit(); });

    // Delegación
    list.addEventListener('click', async (e) => {
        const target = e.target.closest('button, .icon-btn');
        if (!target) return;

        const card = e.target.closest('.hecho-card');
        if (!card) return;

        // Eliminar (abre modal)
        if (target.classList.contains('delete-btn')) {
            const form = card.querySelector('form.delete-form');
            if (form) openDeleteModal(form);
            return;
        }

        // Editar
        if (target.classList.contains('edit-btn') && !card.classList.contains('is-editing')) {
            const chips = card.querySelectorAll('.tags.view-mode .chip');
            const csv = Array.from(chips).map(c => c.textContent.trim()).join(', ');
            const etInput = card.querySelector('input.edit-mode[name="etiquetas"]');
            if (etInput) etInput.value = csv;

            card.classList.add('is-editing');
            main.classList.add('child-is-editing');
            snapshotInputs(card);
            return;
        }

        // Cancelar
        if (target.classList.contains('cancel-btn') && card.classList.contains('is-editing')) {
            restoreSnapshot(card);
            card.classList.remove('is-editing');
            main.classList.remove('child-is-editing');
            return;
        }

        // Guardar
        if (target.classList.contains('save-btn') && card.classList.contains('is-editing')) {
            target.disabled = true;
            try {
                await saveCard(card);
                card.classList.remove('is-editing');
                main.classList.remove('child-is-editing');
            } catch (err) {
                alert('Error guardando cambios. Revisá la consola/Network.');
                console.error(err);
            } finally {
                target.disabled = false;
            }
            return;
        }
    });

    // Snapshot helpers
    function snapshotInputs(card) {
        card.querySelectorAll('.edit-mode[name]').forEach(inp => {
            inp.dataset._original = inp.value;
        });
    }
    function restoreSnapshot(card) {
        card.querySelectorAll('.edit-mode[name]').forEach(inp => {
            if (inp.dataset._original != null) inp.value = inp.dataset._original;
        });
    }

    // Guardar PATCH
    async function saveCard(card) {
        const hash = (card.id || '').replace('hecho-', '').trim();
        if (!hash || hash === 'no-hash') {
            alert('Este hecho no tiene identificador (hash). No se puede guardar.');
            return;
        }

        const nombre = card.querySelector('input.edit-mode[name="nombre"]')?.value?.trim();
        const descripcion = card.querySelector('.edit-mode[name="descripcion"]')?.value?.trim();
        const etiquetasCsv = card.querySelector('input.edit-mode[name="etiquetas"]')?.value?.trim();

        const payload = {
            nombre: nombre || null,
            descripcion: descripcion || null,
            etiquetas: (etiquetasCsv || '').split(',').map(s => s.trim()).filter(Boolean)
        };

        const headers = { 'Content-Type': 'application/json' };
        if (csrfToken) headers[csrfHeader] = csrfToken;

        const resp = await fetch(`/admin/hechos/${encodeURIComponent(hash)}/modificar`, {
            method: 'PATCH',
            headers,
            body: JSON.stringify(payload)
        });

        if (!resp.ok) {
            const txt = await resp.text().catch(() => '');
            throw new Error(`PATCH failed: ${resp.status} ${txt}`);
        }

        // Refrescar UI
        if (nombre != null) {
            card.querySelector('[data-field="nombre"]').textContent = nombre;
            card.dataset.nombre = nombre;
        }
        if (descripcion != null) {
            card.querySelector('[data-field="descripcion"]').textContent = descripcion;
            card.dataset.descripcion = descripcion;
        }
        const tagsWrap = card.querySelector('.tags.view-mode');
        if (tagsWrap) {
            tagsWrap.innerHTML = '';
            (etiquetasCsv || '').split(',').map(s => s.trim()).filter(Boolean).forEach(tag => {
                const span = document.createElement('span');
                span.className = 'chip';
                span.textContent = tag;
                tagsWrap.appendChild(span);
            });
        }
    }
});