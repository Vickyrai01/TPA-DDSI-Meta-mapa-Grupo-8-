// Header sticky + burger
const header = document.querySelector('.site-header');
const onScroll = () => header.classList.toggle('is-scrolled', window.scrollY > 2);
document.addEventListener('scroll', onScroll); onScroll();

const burger = document.querySelector('.burger');
const menu = document.querySelector('.menu');
burger?.addEventListener('click', () => {
  menu.style.display = (menu.style.display === 'flex') ? 'none' : 'flex';
});

// Form: contador + validación
const form = document.getElementById('form-solicitud');
const motivo = document.getElementById('motivo');
const counter = document.getElementById('counter');
const ok = document.getElementById('ok');
const err = document.getElementById('error');

function updateCount(){
  counter.textContent = `${motivo.value.length}/1000`;
}
motivo.addEventListener('input', updateCount);
updateCount();

form.addEventListener('submit', (e) => {
  e.preventDefault();
  ok.hidden = true; err.hidden = true;

  if(!motivo.value.trim()){
    err.hidden = false;
    motivo.focus();
    return;
  }
  // Aquí podrías hacer fetch() a tu API
  ok.hidden = false;
  form.reset();
  updateCount();
});
