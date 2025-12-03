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

  // ==========================================
  //   LÓGICA DEL MODAL DE AGREGAR CRITERIO
  // ==========================================
  const modalCriteria = document.getElementById('modal-add-criteria');
  const selectType = document.getElementById('select-criteria-type');
  const containerInputs = document.getElementById('criteria-inputs-container');
  const btnCancelCrit = document.getElementById('btn-cancel-criteria');
  const btnConfirmCrit = document.getElementById('btn-confirm-criteria');

  // Variable para recordar qué tarjeta abrió el modal
  let currentEditingCard = null;

  // 1. Abrir Modal desde la tarjeta
  // Variable para saber DÓNDE agregar el <li> (puede ser una tarjeta o la lista de creación)
  let targetUlForCriteria = null;

  // CASO 1: Abrir desde una tarjeta existente (EDICIÓN)
  list.addEventListener('click', (e) => {
    if (e.target.closest('.btn-open-criteria-modal')) {
      e.preventDefault();
      const card = e.target.closest('.collection-card');
      targetUlForCriteria = card.querySelector('.criteria-list-edit'); // Destino: lista de la card

      resetCriteriaModal();
      modalCriteria.classList.add('active');
    }

    // Borrar criterio (Edición)
    if (e.target.classList.contains('btn-delete-crit')) {
      e.preventDefault();
      e.target.closest('li').remove();
    }
  });

  // CASO 2: Abrir desde el formulario de crear nueva colección (CREACIÓN)
  const btnAddCritCreation = document.getElementById('btn-add-crit-creation');
  const newListUl = document.getElementById('new-collection-criteria-list'); // La lista vacía del HTML nuevo

  // Listener para borrar en el formulario de creación (delegación en la lista nueva)
  newListUl?.addEventListener('click', (e) => {
    if (e.target.classList.contains('btn-delete-crit')) {
      e.preventDefault();
      e.target.closest('li').remove();
    }
  });

  btnAddCritCreation?.addEventListener('click', (e) => {
    e.preventDefault();
    targetUlForCriteria = newListUl; // Destino: lista del modal de creación

    resetCriteriaModal();
    modalCriteria.classList.add('active');
  });

  // 2. Cerrar Modal
  const closeCriteriaModal = () => modalCriteria.classList.remove('active');
  btnCancelCrit?.addEventListener('click', (e) => { e.preventDefault(); closeCriteriaModal(); });

  // 3. Lógica del Desplegable (Mostrar inputs según selección)
  selectType?.addEventListener('change', () => {
    const type = selectType.value;
    containerInputs.style.display = 'block';

    // Ocultar todos primero
    document.querySelectorAll('.dynamic-group').forEach(el => el.classList.add('hidden'));

    // Mostrar el correcto
    if (type === 'nombre' || type === 'descripcion') {
      document.getElementById('input-group-text').classList.remove('hidden');
    } else if (type === 'categoria') {
      document.getElementById('input-group-cat').classList.remove('hidden');
    } else if (type === 'ubicacion') {
      document.getElementById('input-group-geo').classList.remove('hidden');
    } else if (type === 'fechaSuceso' || type === 'fechaCarga') {
      document.getElementById('input-group-date').classList.remove('hidden');
    }
  });

  // 4. Confirmar y Agregar a la tarjeta
  btnConfirmCrit?.addEventListener('click', (e) => {
    e.preventDefault();

    // Validación de seguridad
    if (!targetUlForCriteria) return;

    const type = selectType.value;
    if (!type) { alert('Selecciona un tipo'); return; }

    // ... (TODA LA LÓGICA DE RECOLECCIÓN DE DATOS QUEDA IGUAL) ...
    // Recolectar datos
    let palabra = null, cat = null, lat = null, lon = null, desde = null, hasta = null;
    let displayText = "";

    if (type === 'nombre' || type === 'descripcion') {
      palabra = document.getElementById('crit-input-keyword').value;
      displayText = `${type}: ${palabra}`;
    } else if (type === 'categoria') {
      cat = document.getElementById('crit-input-category').value;
      displayText = `Categoría: ${cat}`;
    } else if (type === 'ubicacion') {
      lat = document.getElementById('crit-input-lat').value;
      lon = document.getElementById('crit-input-lon').value;
      displayText = `Ubicación: ${lat}, ${lon}`;
    } else if (type.startsWith('fecha')) {
      desde = document.getElementById('crit-input-from').value;
      hasta = document.getElementById('crit-input-to').value;
      displayText = `${type}: ${desde} al ${hasta}`;
    }

    // Validar mínimamente
    if ((type === 'ubicacion' && (!lat || !lon)) ||
        ((type === 'nombre' || type === 'descripcion') && !palabra) ||
        (type === 'categoria' && !cat)) {
      alert('Completa los campos requeridos');
      return;
    }

    // CREAR EL ELEMENTO LI
    const li = document.createElement('li');
    // Importante: usar las mismas clases que en el HTML para que se vea igual
    li.style.display = 'flex';
    li.style.justifyContent = 'space-between';
    li.style.alignItems = 'center';
    li.style.padding = '8px 12px';
    li.style.background = '#f9fafb';
    li.style.border = '1px solid var(--border)';
    li.style.borderRadius = '8px';
    li.style.marginBottom = '6px';

    // Asignar dataset
    li.dataset.type = type;
    if (palabra) li.dataset.palabra = palabra;
    if (cat) li.dataset.categoria = cat;
    if (lat) li.dataset.lat = lat;
    if (lon) li.dataset.lon = lon;
    if (desde) li.dataset.desde = desde;
    if (hasta) li.dataset.hasta = hasta;

    li.innerHTML = `
          <span>${displayText}</span>
          <button type="button" class="delete-source-btn btn-delete-crit">×</button>
      `;

    // AQUI ESTA LA MAGIA: Agregamos al destino que definimos al abrir
    targetUlForCriteria.appendChild(li);

    closeCriteriaModal();
  });

  function resetCriteriaModal() {
    document.getElementById('form-add-criteria').reset();
    selectType.value = "";
    containerInputs.style.display = 'none';
    document.querySelectorAll('.dynamic-group').forEach(el => el.classList.add('hidden'));
  }


  // ===== Modal Crear =====
  const modalAdd = document.getElementById('add-modal');
  const openBtnAdd = document.querySelector('.add-btn');
  const cancelBtnAdd = document.getElementById('cancel-add');
  const confirmBtnAdd = document.getElementById('confirm-add');

  openBtnAdd?.addEventListener('click', (e) => { e.preventDefault(); modalAdd?.classList.add('active'); });
  cancelBtnAdd?.addEventListener('click', (e) => { e.preventDefault(); modalAdd?.classList.remove('active'); });
  confirmBtnAdd?.addEventListener('click', (e) => {
    e.preventDefault();

    // 1. Obtener Título y Descripción (Igual)
    const nombreInput = document.getElementById('new-nombre');
    const infoInput = document.getElementById('new-info');
    const nombre = nombreInput ? nombreInput.value.trim() : null;
    const info = infoInput ? infoInput.value.trim() : null;

    if (!nombre) { alert('El nombre es obligatorio.'); return; }

    // 2. Obtener los IDs de las FUENTES (Igual)
    const fuentesSeleccionadas = [];
    const checkboxes = document.querySelectorAll('input[name="fuentesSeleccionadas"]:checked');
    checkboxes.forEach((checkbox) => {
      const id = parseInt(checkbox.value, 10);
      if (Number.isFinite(id)) fuentesSeleccionadas.push(id);
    });

    // 3. Construir la lista de CRITERIOS (¡NUEVO!)
    const criterios = [];

    // Leemos la lista del modal de creación (#new-collection-criteria-list)
    const listItems = document.querySelectorAll('#new-collection-criteria-list li');

    listItems.forEach(li => {
      const type = li.dataset.type;
      const c = { type: type };

      const getVal = (val) => (val && val !== 'null' && val.trim() !== '') ? val : undefined;
      const getNum = (val) => (val && val !== 'null') ? parseFloat(val) : undefined;

      if (type === 'nombre' || type === 'descripcion') {
        c.palabraClave = getVal(li.dataset.palabra);
      } else if (type === 'categoria') {
        c.categoria = getVal(li.dataset.categoria);
      } else if (type === 'ubicacion') {
        c.latitud = getNum(li.dataset.lat);
        c.longitud = getNum(li.dataset.lon);
      } else if (type === 'fechaSuceso' || type === 'fechaCarga') {
        c.desde = getVal(li.dataset.desde);
        c.hasta = getVal(li.dataset.hasta);
      }

      criterios.push(c);
    });

    // 4. Algoritmo y Modo (Igual)
    const algoritmoSelect = document.getElementById('new-algoritmo');
    const algoritmoConsenso = algoritmoSelect ? algoritmoSelect.value : null;
    const modoSelect = document.getElementById('new-modoNavegacion');
    const modoDeNavegacion = modoSelect ? modoSelect.value : null;

    if (modoDeNavegacion === 'CURADA' && (!algoritmoConsenso || algoritmoConsenso === 'SIN')) {
      alert('Las colecciones CURADAS necesitan un algoritmo de consenso.');
      return;
    }

    // 5. Payload
    const payload = {
      titulo: nombre,
      descripcionColeccion: info,
      hechos: [],
      fuentes: fuentesSeleccionadas,
      criterioDePertenencia: criterios,
      algoritmoConsenso: algoritmoConsenso,
      modoDeNavegacion: modoDeNavegacion
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

  // --- NUEVO: Listener para eliminar criterios visualmente ---
  list.addEventListener('click', (e) => {
    if (e.target.classList.contains('btn-delete-crit')) {
      e.preventDefault();
      const li = e.target.closest('li');
      if (li) li.remove(); // Se elimina del DOM (al guardar, ya no se incluirá)
    }
  });

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

    if (modoNavegacion === 'CURADA' &&
        (!algoritmoConsenso || algoritmoConsenso === 'SIN')) {
      alert('Las colecciones CURADAS necesitan un algoritmo de consenso (no puede ser "Sin algoritmo").');
      return;
    }

    // --- RECONSTRUIR LISTA DE CRITERIOS ---
    const criteriosFinales = [];

    // Ahora iteramos sobre TODA la lista (existentes + nuevos agregados por modal)
    // Nota: cambié la clase selectora a 'criteria-item' en el HTML para unificar
    const items = card.querySelectorAll('.criteria-list-edit li');

    items.forEach(li => {
      const type = li.dataset.type;
      const c = { type: type };

      const getVal = (val) => (val && val !== 'null' && val.trim() !== '') ? val : undefined;
      const getNum = (val) => (val && val !== 'null') ? parseFloat(val) : undefined;

      if (type === 'nombre' || type === 'descripcion') {
        c.palabraClave = getVal(li.dataset.palabra);
      } else if (type === 'categoria') {
        c.categoria = getVal(li.dataset.categoria);
      } else if (type === 'ubicacion') {
        c.latitud = getNum(li.dataset.lat);
        c.longitud = getNum(li.dataset.lon);
      } else if (type === 'fechaSuceso' || type === 'fechaCarga') {
        c.desde = getVal(li.dataset.desde);
        c.hasta = getVal(li.dataset.hasta);
      }

      criteriosFinales.push(c);
    });

    const token = getCsrfToken();
    const headers = {
      'Content-Type': 'application/json'
    };
    if (token) {
      headers['X-CSRF-TOKEN'] = token;
    }

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
        modoDeNavegacion: modoNavegacion,
        criterioDePertenencia: criteriosFinales
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
