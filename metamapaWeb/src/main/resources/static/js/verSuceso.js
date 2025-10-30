// Utilidad: parsea lat/lon que vienen como String (posible coma decimal)


const toNum = v => {
    if (v == null) return null;
    const n = parseFloat(String(v).replace(',', '.'));
    return Number.isFinite(n) ? n : null;
};

const puntos = hechos
    .map(h => ({
        nombre: h.nombre,
        hash: h.hash,
        lat: toNum(h.latitud),
        lon: toNum(h.longitud),
        fecha: h.fechaSuceso,
        hora: h.horaSuceso
    }))
    .filter(p => p.lat != null && p.lon != null);

// Iniciar mapa: default CABA si no hay puntos
const defaultCenter = [-34.6037, -58.3816], defaultZoom = 11;
const map = L.map('mapa', { maxZoom: 19 })
    .setView(puntos.length ? [puntos[0].lat, puntos[0].lon] : defaultCenter,
        puntos.length ? 13 : defaultZoom);

L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    attribution: '&copy; OpenStreetMap contributors', maxZoom: 19
}).addTo(map);

const markers = [];
for (const p of puntos) {
    const html = `
        <b>${p.nombre ?? '(sin título)'}</b><br/>
        ${p.fecha ?? ''} ${p.hora ?? ''}<br/>
        <a href="/hechos/${p.hash}">Ver detalle</a>
      `;
    markers.push(
        L.marker([p.lat, p.lon]).addTo(map).bindPopup(html)
    );
}

if (markers.length > 1) {
    const group = L.featureGroup(markers);
    map.fitBounds(group.getBounds().pad(0.15));
}
window.addEventListener('load', () => map.invalidateSize());
window.addEventListener('resize', () => map.invalidateSize());
/*]]>*/