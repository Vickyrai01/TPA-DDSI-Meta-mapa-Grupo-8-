const API_BASE = 'http://localhost:8080/api';
// Sticky sombra al scrollear
const header = document.querySelector('.site-header');
const onScroll = () => header.classList.toggle('is-scrolled', window.scrollY > 2);
document.addEventListener('scroll', onScroll); onScroll();

// Burger simple (abre/cierra menú en mobile)
const burger = document.querySelector('.burger');
const menu = document.querySelector('.menu');
burger?.addEventListener('click', () => {
    menu.style.display = (menu.style.display === 'flex') ? 'none' : 'flex';
});

// Scroll suave para anclas internas
document.querySelectorAll('a[href^="#"]').forEach(a => {
    a.addEventListener('click', e => {
        const id = a.getAttribute('href');
        const el = document.querySelector(id);
        if (el){
            e.preventDefault();
            el.scrollIntoView({ behavior:'smooth', block:'start' });
        }
    });
});

// Micro-animación al enviar (demo)
// Esto funciona porque el botón sigue teniendo la clase .btn-primary
document.querySelector('.report-card')?.addEventListener('submit', e => {
    e.preventDefault();
    const btn = e.currentTarget.querySelector('.btn-primary');
    const original = btn.textContent;
    btn.disabled = true; btn.textContent = 'Enviando...';
    setTimeout(() => { btn.textContent = '¡Gracias por reportar!'; }, 800);
    setTimeout(() => { btn.disabled = false; btn.textContent = original; e.target.reset(); }, 2200);
});

// Sticky sombra al scrollear
const header = document.querySelector('.site-header');
const onScroll = () => header.classList.toggle('is-scrolled', window.scrollY > 2);
document.addEventListener('scroll', onScroll);
onScroll();

// Burger simple (abre/cierra menú en mobile)
const burger = document.querySelector('.burger');
const menu = document.querySelector('.menu');
burger?.addEventListener('click', () => {
    menu.style.display = (menu.style.display === 'flex') ? 'none' : 'flex';
});


// Utilidad simple para fetch JSON con manejo de errores
async function fetchJSON(url) {
    const res = await fetch(url);
    if (!res.ok) {
        const text = await res.text().catch(() => '');
        throw new Error(`Error ${res.status} en ${url} - ${text}`);
    }
    return res.json();
}

// Renderiza una <a.collection-item> con los datos de una colección
function buildCollectionItem({ id, nombre, descripcion, criterios, imagen, cantidadHechos, ultimaActualizacion }) {
    const a = document.createElement('a');
    a.href = id ? `/colecciones/${id}` : '#'; // ajustá la navegación deseada
    a.className = 'collection-item';

    const text = document.createElement('div');
    text.className = 'collection-text';

    const h3 = document.createElement('h3');
    h3.textContent = nombre ?? `Colección ${id ?? ''}`;

    const pCount = document.createElement('p');
    pCount.textContent = `Cantidad de hechos: ${Number.isFinite(cantidadHechos) ? cantidadHechos : '—'}`;

    const pUlt = document.createElement('p');
    pUlt.textContent = `Última de actualización: ${ultimaActualizacion ?? '—'}`;

    const pCrit = document.createElement('p');
    if (Array.isArray(criterios)) {
        pCrit.textContent = `Criterios: ${criterios.join(', ')}`;
    } else if (typeof criterios === 'string') {
        pCrit.textContent = `Criterios: ${criterios}`;
    } else {
        pCrit.textContent = 'Criterios: —';
    }

    text.append(h3, pCount, pUlt, pCrit);

    const imgWrap = document.createElement('div');
    imgWrap.className = 'collection-image';

    const img = document.createElement('img');
    img.className = 'brand-logo';
    img.alt = nombre ?? 'Colección';
    // Si tu API no trae imagen, usá una por defecto o mapeá por categoría
    img.src = imagen ?? '/img/incendio1.jpg';

    imgWrap.appendChild(img);

    a.append(text, imgWrap);
    return a;
}

// Mapea una colección del JSON a las props que usamos en el render
function mapColeccionRaw(c) {
    // Ajustá estos nombres a lo que devuelve tu endpoint real
    return {
        id: c.id ?? c.identificador ?? c.codigo ?? null,
        nombre: c.nombre ?? c.titulo ?? 'Colección',
        descripcion: c.descripcion ?? null,
        criterios: c.criterios ?? c.tags ?? null,
        ultimaActualizacion: c.ultimaActualizacion ?? c.updatedAt ?? c.fecha ?? null,
        imagen: c.imagen ?? c.cover ?? null,
        cantidadHechos: (typeof c.cantidadHechos === 'number') ? c.cantidadHechos : undefined,
    };
}

async function loadColecciones() {
    const container = document.querySelector('.collections-list');
    if (!container) return;

    container.innerHTML = '<p>Cargando colecciones...</p>';

    try {
        const raw = await fetchJSON(`${API_BASE}/colecciones`);
        const colecciones = Array.isArray(raw) ? raw : (raw?.data ?? []);

        if (!Array.isArray(colecciones) || colecciones.length === 0) {
            container.innerHTML = '<p>No hay colecciones para mostrar.</p>';
            return;
        }

        // Si querés, opcionalmente traé el conteo de hechos por cada colección
        let withCounts = colecciones.map(mapColeccionRaw);

        if (LOAD_HECHOS_COUNT) {
            withCounts = await Promise.all(withCounts.map(async (c) => {
                if (!c.id) return c;
                try {
                    const hechos = await fetchJSON(`${API_BASE}/colecciones/${c.id}/hechos`);
                    return { ...c, cantidadHechos: Array.isArray(hechos) ? hechos.length : (hechos?.length ?? 0) };
                } catch {
                    return c; // si falla, dejamos sin conteo
                }
            }));
        }

        // Render
        container.innerHTML = '';
        const frag = document.createDocumentFragment();
        withCounts.forEach(c => frag.appendChild(buildCollectionItem(c)));
        container.appendChild(frag);

    } catch (err) {
        console.error(err);
        container.innerHTML = '<p style="color:#c00">No se pudieron cargar las colecciones.</p>';
    }
}

// Tu código existente (navbar/burger/scroll) puede convivir, solo evitá duplicados

// Inicializar al cargar el DOM
document.addEventListener('DOMContentLoaded', () => {
    loadColecciones();
});