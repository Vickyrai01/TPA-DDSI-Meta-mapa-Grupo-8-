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

// Enviar login
function enviarLogin(event) {
    event.preventDefault();
    const form = event.target;
    const data = {
        correo: form.correo.value,
        contrasena: form.contrasena.value
    };
    fetch('/api/auth/login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: `correo=${encodeURIComponent(data.correo)}&contrasena=${encodeURIComponent(data.contrasena)}`
    })
        .then(res => {
            if (res.ok) return res.json();
            else return res.text().then(msg => { throw new Error(msg); });
        })
        .then(usuario => {
            cerrarLoginModal();
            window.location.href = '/perfil'; // Redirige al perfil, ajusta si quieres otra vista
        })
        .catch(err => {
            alert('Error: ' + err.message);
        });
    return false;
}

// Enviar registro
function enviarRegistro(event) {
    event.preventDefault();
    const form = event.target;
    const data = {
        nombre: form.nombre.value,
        apellido: form.apellido.value,
        correo: form.correo.value,
        contrasena: form.contrasena.value,
        foto: form.foto.value
    };
    fetch('/api/auth/register', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: `nombre=${encodeURIComponent(data.nombre)}&apellido=${encodeURIComponent(data.apellido)}&correo=${encodeURIComponent(data.correo)}&contrasena=${encodeURIComponent(data.contrasena)}&foto=${encodeURIComponent(data.foto)}`
    })
        .then(res => {
            if (res.ok) return res.json();
            else return res.text().then(msg => { throw new Error(msg); });
        })
        .then(usuario => {
            cerrarLoginModal();
            window.location.href = '/perfil'; // Redirige al perfil, ajusta si quieres otra vista
        })
        .catch(err => {
            alert('Error: ' + err.message);
        });
    return false;
}
