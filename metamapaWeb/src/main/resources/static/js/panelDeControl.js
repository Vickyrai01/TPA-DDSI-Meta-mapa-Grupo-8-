// panelDeControl.js — versión limpia con DELEGACIÓN (funciona para todas las cards)
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

 confirmBtnDelete?.addEventListener('click', (e) => {
   e.preventDefault();
   if (!currentCardForDelete) return;

   // Enviamos el <form class="delete-form"> de ESTA card
   const form = currentCardForDelete.querySelector('form.delete-form');
   if (form) {
     form.submit();         // ← hace POST a /admin/colecciones/{id}/eliminar
   } else {
     // si por algún motivo no hay form, cerramos modal
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
    // TODO: lógica real de creación
    modalAdd?.classList.remove('active');
  });
  modalAdd?.addEventListener('click', (e) => {
    if (e.target.id === 'add-modal') modalAdd?.classList.remove('active');
  });

  // ===== Edición in-place (delegación en la lista) =====
  if (!list) return;

  const getCard = (el) => el.closest('.collection-card');
  const startEdit = (card) => {
    card.classList.add('is-editing');
    adminMainContent?.classList.add('child-is-editing');
  };
  const cancelEdit = (card) => {
    card.classList.remove('is-editing');
    adminMainContent?.classList.remove('child-is-editing');
  };
  const saveEdit = (card) => {
    // TODO: armar payload desde los inputs de la card y enviar al backend
    cancelEdit(card);
  };

  list.addEventListener('click', (e) => {
    const btn = e.target.closest('button');
    if (!btn) return;

    // Editar (lápiz)
    if (btn.classList.contains('edit-btn')) {
      e.preventDefault();
      const card = getCard(btn);
      if (card) startEdit(card);
      return;
    }

    // Cancelar edición
    if (btn.classList.contains('cancel-btn')) {
      e.preventDefault();
      const card = getCard(btn);
      if (card) cancelEdit(card);
      return;
    }

    // Guardar edición
    if (btn.classList.contains('save-btn')) {
      e.preventDefault();
      const card = getCard(btn);
      if (card) saveEdit(card);
      return;
    }

    // Eliminar (tachito)
    if (btn.classList.contains('delete-btn')) {
      e.preventDefault();
      const card = getCard(btn);
      if (card) openDeleteModal(card);
      return;
    }
  });

  // ===== (Opcional) Lista de fuentes dentro de cada card — también con delegación =====
  list.addEventListener('click', (e) => {
    const del = e.target.closest('.delete-source-btn');
    if (del) {
      e.preventDefault();
      del.closest('li')?.remove();
      return;
    }
    const add = e.target.closest('#add-source-btn');
    if (add) {
      e.preventDefault();
      const card = getCard(add);
      const input = card?.querySelector('#add-source-bar input');
      const ul = card?.querySelector('.source-list');
      const val = (input?.value || '').trim();
      if (!val || !ul) return;
      const li = document.createElement('li');
      li.innerHTML = `<span>${val}</span><button class="delete-source-btn" aria-label="Eliminar fuente">&times;</button>`;
      ul.appendChild(li);
      input.value = '';
    }
  });
});
