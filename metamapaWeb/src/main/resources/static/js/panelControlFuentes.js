// panelDeControlFuentes.js — versión sin edición, solo listar / buscar / agregar / eliminar
document.addEventListener('DOMContentLoaded', () => {
  const adminMainContent = document.querySelector('.admin-main-content');
  const list = document.querySelector('#fuentes-list');

  // ========== Helpers ==========
  const getCsrf = () => {
    const el = document.querySelector('input[name="_csrf"]');
    return el ? el.value : null;
  };
  const withCsrf = (headers = {}) => {
    const token = getCsrf();
    return token ? { ...headers, 'X-CSRF-TOKEN': token } : headers;
  };
  const getCard = (el) => el.closest('.fuente-card');

  // =========================
  //   MODAL ELIMINAR
  // =========================
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

  confirmBtnDelete?.addEventListener('click', async (e) => {
    e.preventDefault();
    if (!currentCardForDelete) return;

    // tomamos el form que ya está en la card
    const form = currentCardForDelete.querySelector('form.delete-form');
    if (!form) {
      closeDeleteModal();
      return;
    }

    // preferimos fetch POST para no recargar toda la página
    const action = form.getAttribute('action');
    try {
      const resp = await fetch(action, {
        method: 'POST',
        headers: withCsrf()
      });
      if (resp.ok) {
        currentCardForDelete.remove();
      } else {
        alert('No se pudo eliminar la fuente.');
      }
    } catch (err) {
      console.error(err);
      alert('Error al eliminar la fuente.');
    } finally {
      closeDeleteModal();
    }
  });

  cancelBtnDelete?.addEventListener('click', (e) => {
    e.preventDefault();
    closeDeleteModal();
  });

  // cerrar clickeando afuera
  modalDelete?.addEventListener('click', (e) => {
    if (e.target.id === 'delete-modal') {
      closeDeleteModal();
    }
  });

  // =========================
  //   MODAL CREAR
  // =========================
  const modalAdd = document.getElementById('add-modal');
  const openBtnAdd = document.getElementById('open-add-modal');
  const cancelBtnAdd = document.getElementById('cancel-add');
  const confirmBtnAdd = document.getElementById('confirm-add');

  const inputNombre = document.getElementById('new-nombre');
  const inputLink = document.getElementById('new-link');
  const inputTipo = document.getElementById('new-tipo');
  const inputStrategy = document.getElementById('new-strategy');

  const openAddModal = () => modalAdd?.classList.add('active');
  const closeAddModal = () => modalAdd?.classList.remove('active');

  openBtnAdd?.addEventListener('click', (e) => {
    e.preventDefault();
    openAddModal();
  });

  cancelBtnAdd?.addEventListener('click', (e) => {
    e.preventDefault();
    closeAddModal();
  });

  modalAdd?.addEventListener('click', (e) => {
    if (e.target.id === 'add-modal') {
      closeAddModal();
    }
  });

  confirmBtnAdd?.addEventListener('click', async (e) => {
    e.preventDefault();
    const nombre = inputNombre?.value?.trim();
    const link = inputLink?.value?.trim();
    const tipoFuente = inputTipo?.value?.trim();
    const strategyTipoConexion = inputStrategy?.value?.trim();

    if (!nombre) {
      alert('El nombre es obligatorio.');
      return;
    }

    try {
      const resp = await fetch('/admin/fuentes/crear', {
        method: 'POST',
        headers: withCsrf({ 'Content-Type': 'application/json' }),
        body: JSON.stringify({
          nombre,
          link,
          tipoFuente,
          strategyTipoConexion
        })
      });
      if (!resp.ok) {
        alert('No se pudo crear la fuente.');
        return;
      }
      // opción simple: recargar
      location.reload();
    } catch (err) {
      console.error(err);
      alert('Error al crear la fuente.');
    } finally {
      closeAddModal();
    }
  });

  // =========================
  //   LISTA (delegación)
  // =========================
  if (list) {
    list.addEventListener('click', (e) => {
      const btn = e.target.closest('button');
      if (!btn) return;
      if (btn.classList.contains('delete-btn')) {
        e.preventDefault();
        const card = getCard(btn);
        if (card) openDeleteModal(card);
      }
    });
  }

  // =========================
  //   BÚSQUEDA FRONT
  // =========================
  const searchInput = document.getElementById('search-input');
  const searchBtn = document.getElementById('search-btn');

  const doSearch = () => {
    const q = searchInput?.value?.toLowerCase().trim() || '';
    const cards = list ? list.querySelectorAll('.fuente-card') : [];
    cards.forEach((card) => {
      const nombre = card.dataset.nombre?.toLowerCase() || '';
      card.style.display = nombre.includes(q) ? '' : 'none';
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

document.addEventListener("DOMContentLoaded", function () {
  const formatoInput = document.getElementById("new-formato");
  const linkGroup = document.getElementById("group-link");
  const fileGroup = document.getElementById("group-file");
  const linkInput = document.getElementById("new-link");
  const fileInput = document.getElementById("new-file");
  const fileButton = document.getElementById("file-button");
  const fileName = document.getElementById("file-name");

  function actualizarCamposFormato() {
    const value = (formatoInput.value || "").trim().toUpperCase();

    linkGroup.style.display = "none";
    fileGroup.style.display = "none";
    linkInput.required = false;
    fileInput.required = false;

    if (value === "API REST") {
      linkGroup.style.display = "block";
      linkInput.required = true;
    } else if (value === "CSV") {
      fileGroup.style.display = "block";
      fileInput.required = true;
    }
  }

  formatoInput.addEventListener("input", actualizarCamposFormato);

  // Botón que abre el input file oculto
  if (fileButton) {
    fileButton.addEventListener("click", function () {
      fileInput.click();
    });
  }

  // Mostrar nombre del archivo
  if (fileInput && fileName) {
    fileInput.addEventListener("change", function () {
      fileName.textContent = fileInput.files.length > 0 ? fileInput.files[0].name : "";
    });
  }
});