// panelDeControl.js — versión con restauración y envío al backend
document.addEventListener('DOMContentLoaded', () => {
  const adminMainContent = document.querySelector('.admin-main-content');
  const list = document.querySelector('.collections-list');

  // ===== Modal Eliminar =====
  const modalDelete = document.getElementById('delete-modal');
  const cancelBtnDelete = document.getElementById('cancel-delete');
  const confirmBtnDelete = document.getElementById('confirm-delete');
  let currentCardForDelete = null;

  function openDeleteModal(card) {
    currentCardForDelete = card;
    modalDelete?.classList.add('active');
  }
  function closeDeleteModal() {
    modalDelete?.classList.remove('active');
    currentCardForDelete = null;
  }

  // --- CÓDIGO NUEVO CON FETCH ---
  // Helper para leer el token CSRF del HTML (lo necesitamos)
  const getCsrfToken = () => {
    const tokenInput = document.querySelector('input[name="_csrf"]');
    return tokenInput ? tokenInput.value : null;
  };

  confirmBtnDelete?.addEventListener('click', (e) => {
    e.preventDefault();
    if (!currentCardForDelete) return;

    const form = currentCardForDelete.querySelector('form.delete-form');
    if (!form) {
      closeDeleteModal();
      return;
    }

    const url = form.getAttribute('action');
    const token = getCsrfToken();

    console.log('Enviando POST para eliminar a:', url);

    fetch(url, {
      method: 'POST',
      headers: {
        'X-CSRF-TOKEN': token
      },
      redirect: 'manual' // <-- 1. AÑADIDO: Le decimos a fetch que NO siga la redirección
    })
        .then(resp => {
          // 2. CAMBIADO: Aceptamos un "200 OK" O una redirección como éxito.
          if (resp.ok || resp.type === 'opaqueredirect') {
            currentCardForDelete.remove();
            console.log('Colección eliminada exitosamente.');
          } else {
            console.error('Error del servidor al eliminar la colección.');
            alert('No se pudo eliminar la colección.');
          }
        })
        .catch(err => {
          console.error('Error de red:', err);
          alert('Error de conexión al intentar eliminar.');
        })
        .finally(() => {
          closeDeleteModal();
        });
  });

  cancelBtnDelete?.addEventListener('click', (e) => {
    e.preventDefault();
    closeDeleteModal();
  });

  modalDelete?.addEventListener('click', (e) => {
    if (e.target.id === 'delete-modal') {
      closeDeleteModal();
    }
  });


  // ===== Modal Crear =====
  const modalAdd = document.getElementById('add-modal');
  const openBtnAdd = document.querySelector('.add-btn');
  const cancelBtnAdd = document.getElementById('cancel-add');
  const confirmBtnAdd = document.getElementById('confirm-add');

  openBtnAdd?.addEventListener('click', (e) => { e.preventDefault(); modalAdd?.classList.add('active'); });
  cancelBtnAdd?.addEventListener('click', (e) => { e.preventDefault(); modalAdd?.classList.remove('active'); });
  confirmBtnAdd?.addEventListener('click', (e) => {
    e.preventDefault();

    // 1. Obtener Título y Descripción
    const nombreInput = document.getElementById('new-nombre');
    const infoInput = document.getElementById('new-info');
    const nombre = nombreInput ? nombreInput.value.trim() : '';
    const info = infoInput ? infoInput.value.trim() : '';

    if (nombre === '') {
      alert('El nombre es obligatorio.');
      return;
    }

    // 2. Obtener los IDs de las fuentes seleccionadas
    const checkboxes = document.querySelectorAll('input[name="fuentesSeleccionadas"]:checked');
    const fuentesSeleccionadas = Array.from(checkboxes)
        .map(cb => parseInt(cb.value, 10))              // convertimos a número
        .filter(id => Number.isFinite(id));             // eliminamos null/NaN

    console.log("IDs de fuentes seleccionadas:", fuentesSeleccionadas);

    // Validación extra
    if (fuentesSeleccionadas.length === 0) {
      console.warn("⚠️ No se seleccionó ninguna fuente.");
    }

    // 3. Preparar el payload para el backend
    const payload = {
      titulo: nombre,
      descripcionColeccion: info,
      hechos: [],                      // lista vacía
      fuente: fuentesSeleccionadas,    // importante: el campo se llama "fuente" (singular)
      criterioDePertenencia: null
    };

    console.log("Payload a enviar:", payload);

    // 4. Enviar la petición
    const token = getCsrfToken();

    fetch('/admin/colecciones/crear', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'X-CSRF-TOKEN': token
      },
      body: JSON.stringify(payload)
    })
        .then(resp => {
          if (resp.ok) {
            console.log("Colección creada con éxito");
            location.reload();
          } else {
            alert('No se pudo crear la colección. Revisá la consola.');
            console.error('Error del servidor:', resp);
            modalAdd?.classList.remove('active');
          }
        })
        .catch(err => {
          console.error('Error de red:', err);
          alert('Error de conexión al intentar crear.');
          modalAdd?.classList.remove('active');
        });
  });

  modalAdd?.addEventListener('click', (e) => {
    if (e.target.id === 'add-modal') modalAdd?.classList.remove('active');
  });

  // ===== Edición in-place =====
  if (!list) return;

  const getCard = (el) => el.closest('.collection-card');

  const startEdit = (card) => {
    const tituloInput = card.querySelector('.card-header .card-input.edit-mode');
    const descInput = card.querySelector('.card-body .card-field:nth-child(1) .card-input.edit-mode');

    // Guardar valores originales
    if (tituloInput) card.dataset.originalTitulo = tituloInput.value;
    if (descInput) card.dataset.originalDesc = descInput.value;

    card.classList.add('is-editing');
    adminMainContent?.classList.add('child-is-editing');
  };

  const cancelEdit = (card) => {
    const tituloInput = card.querySelector('.card-header .card-input.edit-mode');
    const tituloView  = card.querySelector('.card-header .view-mode');
    const descInput = card.querySelector('.card-body .card-field:nth-child(1) .card-input.edit-mode');
    const descView  = card.querySelector('.card-body .card-field:nth-child(1) .view-mode');

    // Restaurar Título
    if (tituloInput && card.dataset.originalTitulo !== undefined) {
      tituloInput.value = card.dataset.originalTitulo;
      if (tituloView) tituloView.textContent = card.dataset.originalTitulo;
    }
    // Restaurar Descripción
    if (descInput && card.dataset.originalDesc !== undefined) {
      descInput.value = card.dataset.originalDesc;
      if (descView) descView.textContent = card.dataset.originalDesc;
    }

    card.classList.remove('is-editing');
    adminMainContent?.classList.remove('child-is-editing');
  };

  const saveEdit = (card) => {
    const id = card.id?.replace('card-', '');
    if (!id) {
      console.error('No pude obtener el id de la card');
      return;
    }

    // --- Helper para leer el token CSRF del HTML ---
    const getCsrfToken = () => {
      const tokenInput = document.querySelector('input[name="_csrf"]');
      return tokenInput ? tokenInput.value : null;
    };

    // 1. Obtener datos del TÍTULO
    const tituloInput = card.querySelector('.card-header .card-input.edit-mode');
    const tituloView  = card.querySelector('.card-header .view-mode');
    const nuevoTitulo = tituloInput ? tituloInput.value.trim() : null;

    // 2. Obtener datos de la DESCRIPCIÓN
    const descInput = card.querySelector('.card-body .card-field:nth-child(1) .card-input.edit-mode');
    const descView  = card.querySelector('.card-body .card-field:nth-child(1) .view-mode');
    const nuevaDesc = descInput ? descInput.value.trim() : null;

    // 3. Actualizar la vista (UI)
    if (tituloView && nuevoTitulo !== null) {
      tituloView.textContent = nuevoTitulo;
    }
    if (descView && nuevaDesc !== null) {
      descView.textContent = nuevaDesc;
    }

    // 4. Mandar AMBOS datos al backend
    console.log('enviando PATCH...', id, nuevoTitulo, nuevaDesc);
    fetch(`/admin/colecciones/${id}/modificar`, {
      method: 'PATCH',
      headers: {
        'Content-Type': 'application/json',
        // Aquí deberías incluir el token CSRF si tienes Spring Security activado
      },
      body: JSON.stringify({
        titulo: nuevoTitulo, // <-- AÑADIDO
        descripcionColeccion: nuevaDesc
      })
    })
        .then(resp => {
          if (!resp.ok) {
            console.error('No se pudo guardar la colección');
            // Si falla, revertimos los cambios visuales (opcional pero recomendado)
            cancelEdit(card);
          } else {
            console.log('Colección actualizada ok');
            // Si tiene éxito, actualizamos los "valores originales" para el próximo "cancelar"
            if (tituloInput) card.dataset.originalTitulo = nuevoTitulo;
            if (descInput) card.dataset.originalDesc = nuevaDesc;
          }
        })
        .catch(err => {
          console.error(err);
          cancelEdit(card); // Revertir si hay error de red
        });

    card.classList.remove('is-editing');
    adminMainContent?.classList.remove('child-is-editing');
  };

  // delegación
  list.addEventListener('click', (e) => {
    const btn = e.target.closest('button');
    if (!btn) return;

    if (btn.classList.contains('edit-btn')) {
      e.preventDefault();
      const card = getCard(btn);
      if (card) startEdit(card);
      return;
    }

    if (btn.classList.contains('cancel-btn')) {
      e.preventDefault();
      const card = getCard(btn);
      if (card) cancelEdit(card);
      return;
    }

    if (btn.classList.contains('save-btn')) {
      e.preventDefault();
      const card = getCard(btn);
      if (card) saveEdit(card);
      return;
    }

    if (btn.classList.contains('delete-btn')) {
      e.preventDefault();
      const card = getCard(btn);
      if (card) openDeleteModal(card);
      return;
    }
  });

    const searchInput = document.getElementById('search-input');
    const searchBtn = document.getElementById('search-btn');
    const collectionsList = document.querySelector('.collections-list');

    const doSearch = () => {
      const q = searchInput?.value?.toLowerCase().trim() || '';
      const cards = collectionsList ? collectionsList.querySelectorAll('.collection-card') : [];

      cards.forEach((card) => {
        const titulo = card.dataset.titulo?.toLowerCase() || '';
        const descripcion = card.dataset.descripcion?.toLowerCase() || '';

        const isVisible = titulo.includes(q) || descripcion.includes(q);

        card.style.display = isVisible ? '' : 'none';
      });
    };

    searchBtn?.addEventListener('click', (e) => {
      e.preventDefault();
      doSearch();
    });

    searchInput?.addEventListener('keyup', (e) => {
      if (e.key === 'Enter') {
        doSearch();
      }
    });

});
