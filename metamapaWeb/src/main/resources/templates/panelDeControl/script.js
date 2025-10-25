
    document.addEventListener('DOMContentLoaded', function() {

    // --- Contenedor principal para habilitar/deshabilitar UI (PUNTO 2) ---
    const adminMainContent = document.querySelector('.admin-main-content');

    // --- 1. LÓGICA DEL MODAL DE ELIMINAR ---
    const modalDelete = document.getElementById('delete-modal');
    const openBtnDelete = document.getElementById('open-modal-btn');
    const cancelBtnDelete = document.getElementById('cancel-delete');
    const confirmBtnDelete = document.getElementById('confirm-delete');

    if (modalDelete && openBtnDelete && cancelBtnDelete && confirmBtnDelete) {

    openBtnDelete.addEventListener('click', function(e) {
    e.preventDefault();
    modalDelete.classList.add('active');
});

    cancelBtnDelete.addEventListener('click', function() {
    modalDelete.classList.remove('active');
});

    confirmBtnDelete.addEventListener('click', function() {
    console.log('ACCIÓN: Eliminando la colección...');
    modalDelete.classList.remove('active');
});

    modalDelete.addEventListener('click', function(e) {
    if (e.target.id === 'delete-modal') {
    modalDelete.classList.remove('active');
}
});

} else {
    console.warn("Advertencia: No se encontraron todos los elementos del MODAL DE ELIMINAR.");
}

    // --- 2. LÓGICA DE EDITAR "IN-PLACE" ---
    const card = document.getElementById('card-1');

    if (card && adminMainContent) { // Verificamos que existan
    const editBtn = card.querySelector('.edit-btn');
    const saveBtn = card.querySelector('.save-btn');
    const cancelBtnEdit = card.querySelector('.cancel-btn');

    if (editBtn && saveBtn && cancelBtnEdit) {

    // Al hacer clic en el LÁPIZ
    editBtn.addEventListener('click', function(e) {
    e.preventDefault();
    card.classList.add('is-editing');
    adminMainContent.classList.add('child-is-editing'); // (PUNTO 2)
});

    // Al hacer clic en CANCELAR (edición)
    cancelBtnEdit.addEventListener('click', function(e) {
    e.preventDefault();
    card.classList.remove('is-editing');
    adminMainContent.classList.remove('child-is-editing'); // (PUNTO 2)
});

    // Al hacer clic en GUARDAR
    saveBtn.addEventListener('click', function(e) {
    e.preventDefault();
    console.log('ACCIÓN: Guardando los datos...');
    card.classList.remove('is-editing');
    adminMainContent.classList.remove('child-is-editing'); // (PUNTO 2)
});

} else {
    console.warn("Advertencia: No se encontraron todos los botones de EDICIÓN.");
}
} else {
    console.warn("Advertencia: No se encontró la tarjeta 'card-1' o '.admin-main-content'.");
}

    // --- 3. LÓGICA DEL MODAL DE AÑADIR (PUNTO 3) ---
    const modalAdd = document.getElementById('add-modal');
    const openBtnAdd = document.querySelector('.add-btn'); // El botón flotante '+'
    const cancelBtnAdd = document.getElementById('cancel-add');
    const confirmBtnAdd = document.getElementById('confirm-add');

    if (modalAdd && openBtnAdd && cancelBtnAdd && confirmBtnAdd) {

    // Función para ABRIR el modal de añadir
    openBtnAdd.addEventListener('click', function(e) {
    e.preventDefault();
    modalAdd.classList.add('active');
});

    // Función para CERRAR con "Cancelar"
    cancelBtnAdd.addEventListener('click', function(e) {
    e.preventDefault();
    modalAdd.classList.remove('active');
});

    // Función para CERRAR con "Crear"
    confirmBtnAdd.addEventListener('click', function(e) {
    e.preventDefault();
    console.log('ACCIÓN: Creando nueva colección...');
    // Aquí iría la lógica para leer los inputs y crear
    modalAdd.classList.remove('active');
});

    // Función para CERRAR haciendo clic en el fondo
    modalAdd.addEventListener('click', function(e) {
    if (e.target.id === 'add-modal') {
    modalAdd.classList.remove('active');
}
});

} else {
    console.warn("Advertencia: No se encontraron todos los elementos del MODAL DE AÑADIR.");
}

});