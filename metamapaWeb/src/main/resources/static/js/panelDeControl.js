// panelDeControl.js — versión con restauración y envío al backend
document.addEventListener('DOMContentLoaded', () => {
  // Limitar fecha máxima al día actual en los campos de fecha del modal de crear colección
  const fechaInputs = [
    document.getElementById('new-criterio-suceso-desde'),
    document.getElementById('new-criterio-suceso-hasta'),
    document.getElementById('new-criterio-carga-desde'),
    document.getElementById('new-criterio-carga-hasta')
  ];
  const hoy = new Date();
  const yyyy = hoy.getFullYear();
  const mm = String(hoy.getMonth() + 1).padStart(2, '0');
  const dd = String(hoy.getDate()).padStart(2, '0');
  const maxDate = `${yyyy}-${mm}-${dd}`;
  fechaInputs.forEach(input => {
    if (input) input.setAttribute('max', maxDate);
  });
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

    // Obtener todos los campos obligatorios
    const nombreInput = document.getElementById('new-nombre');
    const infoInput = document.getElementById('new-info');
    const fuentesCheckboxes = document.querySelectorAll('input[name="fuentesSeleccionadas"]');
    const criterioNombreInput = document.getElementById('new-criterio-nombre');
    const criterioDescInput = document.getElementById('new-criterio-descripcion');
    const criterioCatInput = document.getElementById('new-criterio-categoria');
    const criterioLatInput = document.getElementById('new-criterio-lat');
    const criterioLonInput = document.getElementById('new-criterio-lon');
    const criterioSucesoDesdeInput = document.getElementById('new-criterio-suceso-desde');
    const criterioSucesoHastaInput = document.getElementById('new-criterio-suceso-hasta');
    const criterioCargaDesdeInput = document.getElementById('new-criterio-carga-desde');
    const criterioCargaHastaInput = document.getElementById('new-criterio-carga-hasta');
    const algoritmoSelect = document.getElementById('new-algoritmo');

    // Limpiar errores previos
    [nombreInput, infoInput, criterioNombreInput, criterioDescInput, criterioCatInput, criterioLatInput, criterioLonInput, criterioSucesoDesdeInput, criterioSucesoHastaInput, criterioCargaDesdeInput, criterioCargaHastaInput, algoritmoSelect].forEach(el => {
      if (el) el.classList.remove('input-error');
    });

    let errorMsg = '';
    let errorFields = [];

    if (!nombreInput.value.trim()) {
      errorMsg = 'El nombre es obligatorio.';
      errorFields.push(nombreInput);
    }
    if (!infoInput.value.trim()) {
      errorMsg = 'La descripción es obligatoria.';
      errorFields.push(infoInput);
    }
    if (![...fuentesCheckboxes].some(cb => cb.checked)) {
      errorMsg = 'Debes seleccionar al menos una fuente.';
      errorFields.push(fuentesCheckboxes[0]);
    }
    if (!criterioNombreInput.value.trim()) {
      errorMsg = 'El título es obligatorio.';
      errorFields.push(criterioNombreInput);
    }
    if (!criterioDescInput.value.trim()) {
      errorMsg = 'La descripción del criterio es obligatoria.';
      errorFields.push(criterioDescInput);
    }
    if (!criterioCatInput.value.trim()) {
      errorMsg = 'La categoría es obligatoria.';
      errorFields.push(criterioCatInput);
    }
    if (!criterioLatInput.value.trim() || !criterioLonInput.value.trim()) {
      errorMsg = 'La ubicación es obligatoria.';
      errorFields.push(criterioLatInput);
      errorFields.push(criterioLonInput);
    }
    if (!criterioSucesoDesdeInput.value || !criterioSucesoHastaInput.value) {
      errorMsg = 'Las fechas de suceso son obligatorias.';
      errorFields.push(criterioSucesoDesdeInput);
      errorFields.push(criterioSucesoHastaInput);
    } else {
      const desdeSuceso = new Date(criterioSucesoDesdeInput.value);
      const hastaSuceso = new Date(criterioSucesoHastaInput.value);
      if (desdeSuceso > hastaSuceso) {
        errorMsg = 'La fecha "Desde" de suceso no puede ser mayor que la fecha "Hasta".';
        errorFields.push(criterioSucesoDesdeInput);
        errorFields.push(criterioSucesoHastaInput);
      }
    }
    if (!criterioCargaDesdeInput.value || !criterioCargaHastaInput.value) {
      errorMsg = 'Las fechas de carga son obligatorias.';
      errorFields.push(criterioCargaDesdeInput);
      errorFields.push(criterioCargaHastaInput);
    } else {
      const desdeCarga = new Date(criterioCargaDesdeInput.value);
      const hastaCarga = new Date(criterioCargaHastaInput.value);
      if (desdeCarga > hastaCarga) {
        errorMsg = 'La fecha "Desde" de carga no puede ser mayor que la fecha "Hasta".';
        errorFields.push(criterioCargaDesdeInput);
        errorFields.push(criterioCargaHastaInput);
      }
    }
    if (!algoritmoSelect.value) {
      errorMsg = 'El algoritmo de consenso es obligatorio.';
      errorFields.push(algoritmoSelect);
    }

    if (errorFields.length > 0) {
      errorFields.forEach(el => { if (el) el.classList.add('input-error'); });
      alert(errorMsg || 'Completa todos los campos obligatorios.');
      return;
    }

    // 2. Obtener los IDs de las FUENTES seleccionadas
    const fuentesSeleccionadas = [];
    const checkboxes = document.querySelectorAll('input[name="fuentesSeleccionadas"]:checked');
    checkboxes.forEach((checkbox) => {
      const id = parseInt(checkbox.value, 10);
      if (Number.isFinite(id)) {
        fuentesSeleccionadas.push(id);
      }
    });
    // 3. Construir la lista de CRITERIOS
    const criterios = [];
    const criterioNombreVal = criterioNombreInput.value.trim();
    if (criterioNombreVal) {
      criterios.push({ "type": "nombre", "palabraClave": criterioNombreVal });
    }
    const criterioDescVal = criterioDescInput.value.trim();
    if (criterioDescVal) {
      criterios.push({ "type": "descripcion", "palabraClave": criterioDescVal });
    }
    const criterioCatVal = criterioCatInput.value.trim();
    if (criterioCatVal) {
      criterios.push({ "type": "categoria", "categoria": criterioCatVal });
    }
    if (criterioLatInput.value.trim() && criterioLonInput.value.trim()) {
      criterios.push({
        "type": "ubicacion",
        "latitud": parseFloat(criterioLatInput.value.trim()),
        "longitud": parseFloat(criterioLonInput.value.trim())
      });
    }
    if (criterioSucesoDesdeInput.value && criterioSucesoHastaInput.value) {
      criterios.push({
        "type": "fechaSuceso",
        "desde": criterioSucesoDesdeInput.value,
        "hasta": criterioSucesoHastaInput.value
      });
    }
    if (criterioCargaDesdeInput.value && criterioCargaHastaInput.value) {
      criterios.push({
        "type": "fechaCarga",
        fuente: fuentesSeleccionadas,
        "hasta": criterioCargaHastaInput.value
      });
    }

    // 4. Preparar el 'payload' final
    const payload = {
      titulo: nombreInput.value.trim(),
      descripcionColeccion: infoInput.value.trim(),
      hechos: [],
      fuentes: fuentesSeleccionadas,
      criterioDePertenencia: criterios,
      algoritmoConsenso: algoritmoSelect.value
    };

    console.log("Enviando payload:", JSON.stringify(payload));

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

    // 3.1. Obtener fuentes seleccionadas en edición
    const fuentesSeleccionadas = [];
    const checkboxes = card.querySelectorAll('input[name="fuentesSeleccionadasEdit"]:checked');
    checkboxes.forEach((checkbox) => {
      const idFuente = parseInt(checkbox.value, 10);
      if (Number.isFinite(idFuente)) {
        fuentesSeleccionadas.push(idFuente);
      }
    });

    // 3.2. Obtener algoritmo de consenso seleccionado en edición
    const algoritmoSelect = card.querySelector('select[name="algoritmoConsensoEdit"]');
    const algoritmoConsenso = algoritmoSelect ? algoritmoSelect.value : null;

    // 4. Mandar datos al backend (incluyendo fuentes y algoritmoConsenso)
    console.log('enviando PATCH...', id, nuevoTitulo, nuevaDesc, fuentesSeleccionadas, algoritmoConsenso);
    fetch(`/admin/colecciones/${id}/modificar`, {
      method: 'PATCH',
      headers: {
        'Content-Type': 'application/json',
        // Aquí deberías incluir el token CSRF si tienes Spring Security activado
      },
      body: JSON.stringify({
        titulo: nuevoTitulo,
        descripcionColeccion: nuevaDesc,
        fuentes: fuentesSeleccionadas,
        algoritmoConsenso: algoritmoConsenso
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
