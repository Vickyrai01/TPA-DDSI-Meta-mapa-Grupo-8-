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
            //cerrarLoginModal(); // Solo para modal, no para página
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
        contrasena: form.contrasena.value
    };
    fetch('/api/auth/register', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: `nombre=${encodeURIComponent(data.nombre)}&apellido=${encodeURIComponent(data.apellido)}&correo=${encodeURIComponent(data.correo)}&contrasena=${encodeURIComponent(data.contrasena)}`
    })
        .then(async res => {
            if (res.ok) {
                // Confirmar que el usuario fue persistido realmente
                fetch(`/api/auth/login?correo=${encodeURIComponent(data.correo)}&contrasena=${encodeURIComponent(data.contrasena)}`, {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/x-www-form-urlencoded' }
                })
                    .then(loginRes => {
                        if (loginRes.ok) {
                            mostrarToastExito('Usuario registrado correctamente');
                            setTimeout(() => {
                                window.location.href = '/api/auth/login';
                            }, 1200);
                        } else {
                            // Si no se puede loguear, esperar y reintentar una vez
                            setTimeout(() => {
                                fetch(`/api/auth/login?correo=${encodeURIComponent(data.correo)}&contrasena=${encodeURIComponent(data.contrasena)}`, {
                                    method: 'POST',
                                    headers: { 'Content-Type': 'application/x-www-form-urlencoded' }
                                })
                                    .then(loginRes2 => {
                                        if (loginRes2.ok) {
                                            mostrarToastExito('Usuario registrado correctamente');
                                            setTimeout(() => {
                                                window.location.href = '/api/auth/login';
                                            }, 1200);
                                        } else {
                                            alert('Error: El usuario no se pudo verificar tras el registro. Intenta nuevamente.');
                                        }
                                    });
                            }, 800);
                        }
                    });
            } else {
                const msg = await res.text();
                alert('Error: ' + msg);
            }
        })
        .catch(err => {
            alert('Error: ' + err.message);
        });
    return false;
}
