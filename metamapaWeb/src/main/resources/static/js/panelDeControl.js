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

  confirmBtnDelete?.addEventListener('click', (e) => {
    e.preventDefault();
    if (!currentCardForDelete) return;

    const form = currentCardForDelete.querySelector('form.delete-form');
    if (form) {
      form.submit();
    } else {
      closeDeleteModal();
    }
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
    // TODO: lógica real de creación
    modalAdd?.classList.remove('active');
  });
  modalAdd?.addEventListener('click', (e) => {
    if (e.target.id === 'add-modal') modalAdd?.classList.remove('active');
  });

  // ===== Edición in-place =====
  if (!list) return;

  const getCard = (el) => el.closest('.collection-card');

  const startEdit = (card) => {
    // guardar valores originales
    const descInput = card.querySelector('.card-field:nth-child(1) .card-input.edit-mode');
    if (descInput) card.dataset.originalDesc = descInput.value;
    card.classList.add('is-editing');
    adminMainContent?.classList.add('child-is-editing');
  };

  const cancelEdit = (card) => {
    // restauro descripción
    const descInput = card.querySelector('.card-field:nth-child(1) .card-input.edit-mode');
    const descView  = card.querySelector('.card-field:nth-child(1) .view-mode');
    if (descInput && card.dataset.originalDesc !== undefined) {
      descInput.value = card.dataset.originalDesc;
      if (descView) descView.textContent = card.dataset.originalDesc;
    }

    card.classList.remove('is-editing');
    adminMainContent?.classList.remove('child-is-editing');
  };

  // 👇 ESTA es la única saveEdit
  const saveEdit = (card) => {
    const id = card.id?.replace('card-', '');
    if (!id) {
      console.error('No pude obtener el id de la card');
      return;
    }

    // como en tu HTML actual el primer field es la DESCRIPCIÓN
    const descInput = card.querySelector('.card-field:nth-child(1) .card-input.edit-mode');
    const descView  = card.querySelector('.card-field:nth-child(1) .view-mode');

    const nuevaDesc = descInput ? descInput.value.trim() : null;

    // actualizo vista
    if (descView && nuevaDesc !== null) {
      descView.textContent = nuevaDesc;
    }

    // mando al backend
    console.log('enviando PATCH...', id, nuevaDesc);
    fetch(`/admin/colecciones/${id}/modificar`, {
      method: 'PATCH',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        // si después volvés a poner el campo "Nombre" arriba, lo agregás acá
        // titulo: nuevoNombre,
        descripcionColeccion: nuevaDesc
      })
    })
      .then(resp => {
        if (!resp.ok) {
          console.error('No se pudo guardar la colección');
        } else {
          console.log('Colección actualizada ok');
        }
      })
      .catch(err => console.error(err));

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
});
