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

    // 1. Obtener Título y Descripción de la COLECCIÓN
    const nombreInput = document.getElementById('new-nombre');
    const infoInput = document.getElementById('new-info');
    const nombre = nombreInput ? nombreInput.value.trim() : null;
    const info = infoInput ? infoInput.value.trim() : null;

    if (!nombre) {
      alert('El nombre es obligatorio.');
      return;
    }

    // 2. Obtener los IDs de las FUENTES seleccionadas
    const fuentesSeleccionadas = [];
    const checkboxes = document.querySelectorAll('input[name="fuentesSeleccionadas"]:checked');
    checkboxes.forEach((checkbox) => {
      const id = parseInt(checkbox.value, 10);
      if (Number.isFinite(id)) { // Evita IDs nulos/NaN si th:value está vacío
        fuentesSeleccionadas.push(id);
      }
    });
    // 3. Construir la lista de CRITERIOS
    const criterios = [];

    // Criterios de Texto
    const criterioNombreVal = document.getElementById('new-criterio-nombre')?.value.trim();
    if (criterioNombreVal) {
      criterios.push({ "type": "nombre", "palabraClave": criterioNombreVal });
    }

    const criterioDescVal = document.getElementById('new-criterio-descripcion')?.value.trim();
    if (criterioDescVal) {
      criterios.push({ "type": "descripcion", "palabraClave": criterioDescVal });
    }

    const criterioCatVal = document.getElementById('new-criterio-categoria')?.value.trim();
    if (criterioCatVal) {
      criterios.push({ "type": "categoria", "categoria": criterioCatVal });
    }

    // Criterio de Ubicación (requiere ambos campos)
    const criterioLatVal = document.getElementById('new-criterio-lat')?.value.trim();
    const criterioLonVal = document.getElementById('new-criterio-lon')?.value.trim();
    if (criterioLatVal && criterioLonVal) {
      criterios.push({
        "type": "ubicacion",
        "latitud": parseFloat(criterioLatVal),
        "longitud": parseFloat(criterioLonVal)
      });
    }

    // Criterio Fecha Suceso (requiere ambos campos)
    const criterioSucesoDesde = document.getElementById('new-criterio-suceso-desde')?.value;
    const criterioSucesoHasta = document.getElementById('new-criterio-suceso-hasta')?.value;
    if (criterioSucesoDesde && criterioSucesoHasta) {
      criterios.push({
        "type": "fechaSuceso",
        "desde": criterioSucesoDesde, // "YYYY-MM-DD"
        "hasta": criterioSucesoHasta
      });
    }

    // Criterio Fecha Carga (requiere ambos campos)
    const criterioCargaDesde = document.getElementById('new-criterio-carga-desde')?.value;
    const criterioCargaHasta = document.getElementById('new-criterio-carga-hasta')?.value;
    if (criterioCargaDesde && criterioCargaHasta) {
      criterios.push({
        "type": "fechaCarga",
        "desde": criterioCargaDesde,
        "hasta": criterioCargaHasta
      });
    }

    // 4. Preparar el 'payload' final
    const payload = {
      titulo: nombre,
      descripcionColeccion: info,
      hechos: [],
      fuente: fuentesSeleccionadas,
      criterioDePertenencia: criterios // <-- AHORA ENVIAMOS TODOS LOS CRITERIOS
    };

    console.log("Enviando payload:", JSON.stringify(payload)); // Para depurar

    // 5. Enviar la petición
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
            location.reload();
          } else {
            alert('No se pudo crear la colección. Revisa la consola.');
            console.error('Error del servidor al crear.', resp);
          }
        })
        .catch(err => {
          console.error('Error de red:', err);
          alert('Error de conexión al intentar crear.');
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

    // --- Helper para obtener el token CSRF ---
    const getCsrfToken = () => {
      const tokenInput = document.querySelector('input[name="_csrf"]');
      return tokenInput ? tokenInput.value : null;
    };

    // 1. Obtener datos Inputs Texto
    const tituloInput = card.querySelector('.card-header .card-input.edit-mode');
    const tituloView  = card.querySelector('.card-header .view-mode');
    const nuevoTitulo = tituloInput ? tituloInput.value.trim() : null;

    const descInput = card.querySelector('.card-body .card-field:nth-child(1) .card-input.edit-mode');
    const descView  = card.querySelector('.card-body .card-field:nth-child(1) .view-mode');
    const nuevaDesc = descInput ? descInput.value.trim() : null;

    // 2. Obtener FUENTES seleccionadas
    const fuentesSeleccionadas = [];
    const checkboxes = card.querySelectorAll('input[name="fuentesSeleccionadasEdit"]:checked');
    checkboxes.forEach((checkbox) => {
      const idFuente = parseInt(checkbox.value, 10);
      if (Number.isFinite(idFuente)) {
        fuentesSeleccionadas.push(idFuente);
      }
    });

    // 3. Obtener ALGORITMO
    const algoritmoSelect = card.querySelector('select[name="algoritmoConsensoEdit"]');
    const algoritmoConsenso = algoritmoSelect ? algoritmoSelect.value : null;

    // 4. Obtener MODO DE NAVEGACIÓN (¡Nuevo!)
    const modoSelect = card.querySelector('select[name="modoDeNavegacionEdit"]');
    const modoView = card.querySelector('.card-field p.view-mode[th\\:text*="modoDeNavegacion"]'); // Selector aproximado, mejor si le pones ID o clase específica al <p>
    // O busca el <p> que está justo antes del select en el mismo div
    const modoViewContainer = modoSelect ? modoSelect.previousElementSibling : null;

    const modoNavegacion = modoSelect ? modoSelect.value : null;

    // 5. Preparar Headers con Token CSRF
    const token = getCsrfToken();
    const headers = {
      'Content-Type': 'application/json'
    };
    if (token) {
      headers['X-CSRF-TOKEN'] = token;
    }

    // 6. Enviar Fetch
    console.log('Enviando PATCH...', id, {
      titulo: nuevoTitulo,
      modo: modoNavegacion,
      algoritmo: algoritmoConsenso
    });

    fetch(`/admin/colecciones/${id}/modificar`, {
      method: 'PATCH',
      headers: headers, // <--- Importante: enviar headers con CSRF
      body: JSON.stringify({
        titulo: nuevoTitulo,
        descripcionColeccion: nuevaDesc,
        fuentes: fuentesSeleccionadas,
        algoritmoConsenso: algoritmoConsenso,
        modoDeNavegacion: modoNavegacion
      })
    })
        .then(resp => {
          if (!resp.ok) {
            console.error('Error al guardar:', resp.status);
            throw new Error('Error en la respuesta del servidor');
          }
          return resp; // o resp.json() si el backend devuelve algo
        })
        .then(() => {
          console.log('Colección actualizada ok');

          // Actualizar la vista (View Mode)
          if (tituloView && nuevoTitulo !== null) tituloView.textContent = nuevoTitulo;
          if (descView && nuevaDesc !== null) descView.textContent = nuevaDesc;

          // Actualizar texto del Modo de Navegación en la vista
          // Buscamos el elemento <p> hermano del select para actualizar su texto
          if (modoSelect && modoSelect.parentElement) {
            const pView = modoSelect.parentElement.querySelector('.view-mode');
            if (pView) pView.textContent = modoNavegacion;
          }

          // Actualizar "Originales" para futura edición
          if (tituloInput) card.dataset.originalTitulo = nuevoTitulo;
          if (descInput) card.dataset.originalDesc = nuevaDesc;
          // (Opcional: guardar también el estado original de los selects para restaurar con Cancelar)

          card.classList.remove('is-editing');
          adminMainContent?.classList.remove('child-is-editing');
        })
        .catch(err => {
          console.error(err);
          alert('No se pudo guardar la colección. Verifica la consola.');
          cancelEdit(card); // Revertir cambios visuales
        });
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
