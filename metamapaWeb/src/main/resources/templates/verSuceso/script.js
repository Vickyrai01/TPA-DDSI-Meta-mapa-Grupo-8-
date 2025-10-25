let map = L.map('mapa', {
    maxZoom: 19 // Le decís al MAPA que no deje zoomear más de 19
}).setView([-34.722222,-58.363611], 15);

// 2. En las opciones de la capa
L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors',
    maxZoom: 19 // Le decís a la CAPA que su máximo es 19
}).addTo(map);

L.marker([-34.722222,-58.363611]).addTo(map)
    .bindPopup('Hola');

window.addEventListener('resize', function() {
    // Le dice al mapa que revise su tamaño y se ajuste
    map.invalidateSize();
});