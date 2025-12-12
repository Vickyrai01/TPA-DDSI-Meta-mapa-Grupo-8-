// Toast visual reutilizable
function mostrarToastExito(mensaje) {
    let toast = document.getElementById('popup-toast-success');
    if (!toast) {
        toast = document.createElement('div');
        toast.id = 'popup-toast-success';
        toast.style.position = 'fixed';
        toast.style.top = '30px';
        toast.style.left = '50%';
        toast.style.transform = 'translateX(-50%)';
        toast.style.zIndex = '9999';
        toast.style.background = '#4BB543';
        toast.style.color = '#fff';
        toast.style.padding = '1rem 2rem';
        toast.style.borderRadius = '12px';
        toast.style.boxShadow = '0 4px 16px rgba(0,0,0,0.15)';
        toast.style.fontSize = '1.15rem';
        toast.style.display = 'flex';
        toast.style.alignItems = 'center';
        toast.style.gap = '0.75rem';
        toast.innerHTML = `<svg style="width: 24px; height: 24px;" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24"><circle cx="12" cy="12" r="10" stroke="#fff"/><polyline points="8 12 11 15 16 10" style="fill:none;stroke:#fff;stroke-width:2"/></svg><span></span><button onclick="this.parentNode.style.display='none'" style="background: none; border: none; color: #fff; font-size: 1.5rem; margin-left: 1rem; cursor: pointer;">&times;</button>`;
        document.body.appendChild(toast);
    }
    toast.querySelector('span').textContent = mensaje;
    toast.style.display = 'flex';
    setTimeout(() => { toast.style.display = 'none'; }, 4000);
}

// Mostrar el modal
function abrirLoginModal() {
    document.getElementById('loginModal').style.display = 'flex';
    document.getElementById('loginForm').style.display = 'none';
    document.getElementById('registerForm').style.display = 'none';
}

function cerrarLoginModal() {
    document.getElementById('loginModal').style.display = 'none';
}

function mostrarLoginForm() {
    document.getElementById('loginForm').style.display = 'block';
    document.getElementById('registerForm').style.display = 'none';
}

function mostrarRegisterForm() {
    document.getElementById('registerForm').style.display = 'block';
    document.getElementById('loginForm').style.display = 'none';
}

function enviarLogin(event) {
    event.preventDefault();
    const form = event.target;

    const correo = form.correo.value;
    const contrasena = form.contrasena.value;

    fetch('/api/auth/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: `correo=${encodeURIComponent(correo)}&contrasena=${encodeURIComponent(contrasena)}`
    })
        .then(async res => {
            if (!res.ok) throw new Error(await res.text());
            return res.json();
        })
        .then(() => {
            mostrarToastExito('Login exitoso');
            setTimeout(() => window.location.href = '/perfil', 800);
        })
        .catch(err => alert('Error: ' + err.message));

    return false;
}

function enviarRegistro(event) {
    event.preventDefault();
    const form = event.target;

    const fechaIso = form.fechaNacimiento?.value;

    if (!fechaIso) {
        alert("Elegí tu fecha de nacimiento.");
        return false;
    }

    const nombreCompleto = (form.nombre.value + " " + form.apellido.value).trim();
    const correo = form.correo.value;
    const contrasena = form.contrasena.value;

    fetch('/api/auth/register', {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body:
            `nombre=${encodeURIComponent(nombreCompleto)}` +
            `&correo=${encodeURIComponent(correo)}` +
            `&contrasena=${encodeURIComponent(contrasena)}` +
            `&fechaNacimiento=${encodeURIComponent(fechaIso)}`
    })
        .then(async res => {
            if (!res.ok) throw new Error(await res.text());

            mostrarToastExito('Usuario registrado correctamente');
            setTimeout(() => {
                window.location.href = '/api/auth/login';
            }, 1200);
        })
        .catch(err => alert('Error: ' + err.message));

    return false;
}

//MAYOR A 16
document.addEventListener("DOMContentLoaded", () => {
    const input = document.getElementById("fechaNacimientoReg");
    if (!input) return;

    const hoy = new Date();
    const max = new Date(hoy.getFullYear() - 16, hoy.getMonth(), hoy.getDate());

    const yyyy = String(max.getFullYear());
    const mm = String(max.getMonth() + 1).padStart(2, "0");
    const dd = String(max.getDate()).padStart(2, "0");

    input.max = `${yyyy}-${mm}-${dd}`;
});
