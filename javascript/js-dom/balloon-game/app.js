// Buscamos cada elemento por su id una sola vez
// y lo guardamos en una variable para usarlo después

const hud                 = document.getElementById("hud");
const cntPuntos           = document.getElementById("cnt-puntos");
const cntGlobos           = document.getElementById("cnt-globos");
const contenedor          = document.getElementById("contenedor");
const overlay             = document.getElementById("overlay");
const overlayPuntosFinales = document.getElementById("overlay-puntos-finales");
const btnPausa            = document.getElementById("btn-pausa");
const btnReset            = document.getElementById("btn-reset");
const btnNuevo            = document.getElementById("btn-nuevo");


// CONSTANTES — valores fijos que no cambian

const TOTAL_GLOBOS   = 20;   

const RADIO_MIN      = 15;   
const RADIO_MAX      = 45;   

const VEL_MIN        = 60;  
const VEL_MAX        = 140;  
// Los globos pequeños se desplazan más rápido

const PUNTOS_NORMAL  = 10;   
const PUNTOS_RAPIDO  = 20;   

const UMBRAL_RAPIDO  = 150;  // Si la velocidad final supera este valor → es "rápido"


// ESTADO DEL JUEGO — variables que cambian

let globos    = [];    
let paused    = false; 
let puntos    = 0;     
let eliminados = 0;   



// FUNCIONES DE UTILIDAD

function aleatorio(min, max) {
  return min + Math.random() * (max - min);
}

function colorAleatorio() {
  const r = Math.floor(Math.random() * 256);
  const g = Math.floor(Math.random() * 256);
  const b = Math.floor(Math.random() * 256);
  return `rgb(${r},${g},${b})`;
}

function actualizarHUD() {
  cntPuntos.textContent = puntos;
  cntGlobos.textContent = eliminados + "/" + TOTAL_GLOBOS;
}



// CREAR UN GLOBO

function crearGlobo() {
  // Altura del HUD para no colocar globos debajo de él
  const hudH = hud.offsetHeight || 40;

  // Tamaño aleatorio
  const radio = Math.round(aleatorio(RADIO_MIN, RADIO_MAX));

  // Velocidad: los globos pequeños van más rápido 
  // "ratio" va de 0 (radio máximo = lento) a 1 (radio mínimo = rápido)
  const ratio = 1 - (radio - RADIO_MIN) / (RADIO_MAX - RADIO_MIN);
  // El bonus extra hace que los pequeños sean notablemente más veloces
  const bonusVelocidad = ratio * 120;
  const velocidad = aleatorio(VEL_MIN, VEL_MAX) + bonusVelocidad;

  // Puntos que dará este globo al reventarlo 
  const puntosAlReventar = velocidad > UMBRAL_RAPIDO ? PUNTOS_RAPIDO : PUNTOS_NORMAL;

  // Límites del área de juego
  const maxX = contenedor.clientWidth  - radio * 2;
  const maxY = contenedor.clientHeight - radio * 2 - hudH;

  // Posición inicial aleatoria dentro del área de juego 
  const x = aleatorio(0, maxX);
  const y = hudH + aleatorio(0, maxY);

  // Elemento HTML del globo (un div redondo de colores) 
  const el = document.createElement("div");
  el.className = "globo";
  el.style.width           = radio * 2 + "px";
  el.style.height          = radio * 2 + "px";
  el.style.backgroundColor = colorAleatorio();
  // Al hacer clic → reventamos este globo
  el.onclick = () => reventarGlobo(g);

  // Objeto globo: agrupa todos los datos que necesita la animación 
  const g = {
    el,                               // Referencia al div HTML
    x,                                // Posición actual X
    y,                                // Posición actual Y
    radio,                            // Radio en píxeles
    targetX: aleatorio(0, maxX),      // Destino actual X (hacia donde se mueve)
    targetY: hudH + aleatorio(0, maxY), // Destino actual Y
    velocidad,                        // Píxeles por segundo
    puntosAlReventar,                 // Puntos que da al hacer clic
    intervalId: null,                 // Guardará el id del setInterval para poder cancelarlo
  };

  // Cada cierto tiempo el globo elige un nuevo destino aleatorio
  g.intervalId = setInterval(() => {
    if (paused) return; // Si está pausado, no cambiamos el destino

    // Recalculamos límites por si la ventana cambió de tamaño
    const mx = contenedor.clientWidth  - radio * 2;
    const my = contenedor.clientHeight - radio * 2 - hud.offsetHeight;

    g.targetX = aleatorio(0, mx);
    g.targetY = hud.offsetHeight + aleatorio(0, my);
  }, aleatorio(1200, 2200)); // cambia de destino cada 1.2 – 2.2 segundos

  contenedor.appendChild(el);
  return g;
}


// REVENTAR UN GLOBO

function reventarGlobo(g) {
  // 1. Añadimos la clase CSS que anima la explosión (se agranda y desaparece)
  g.el.classList.add("exploding");

  // 2. Esperamos a que acabe la animación (300ms = duración del transition en CSS)
  //    y entonces eliminamos el globo de verdad
  setTimeout(() => {
    // Paramos el intervalo de cambio de destino para no gastar memoria
    clearInterval(g.intervalId);

    // Quitamos el div del HTML
    g.el.remove();

    // Quitamos el globo del array de globos activos
    globos = globos.filter((b) => b !== g);

    // Sumamos los puntos correspondientes y aumentamos el contador
    puntos += g.puntosAlReventar;
    eliminados++;
    actualizarHUD();

    // Si ya no quedan globos → el jugador ha ganado, mostramos pantalla de fin
    if (globos.length === 0) {
      overlayPuntosFinales.textContent = "Puntuación final: " + puntos;
      overlay.style.display = "flex";
    }
  }, 300); // 300 ms coincide con el transition de CSS
}


// INICIAR / REINICIAR PARTIDA

function iniciar() {
  // Ocultamos la pantalla de fin si estaba visible
  overlay.style.display = "none";

  // Eliminamos todos los globos de la partida anterior
  globos.forEach((g) => {
    clearInterval(g.intervalId); // Cancelamos su intervalo de destino
    g.el.remove();               // Quitamos su div del HTML
  });

  // Reseteamos el estado
  globos     = [];
  puntos     = 0;
  eliminados = 0;
  paused     = false;
  btnPausa.textContent = "Pausar";

  actualizarHUD();

  // Creamos los globos nuevos
  for (let i = 0; i < TOTAL_GLOBOS; i++) {
    globos.push(crearGlobo());
  }
}


// BUCLE DE ANIMACIÓN
// Mueve todos los globos en cada fotograma del navegador

let lastTime = null; // Marca de tiempo del fotograma anterior

function animar(time) {
  if (!lastTime) lastTime = time;

  // dt = segundos transcurridos desde el último fotograma
  // Lo limitamos a 0.05s para evitar saltos si la pestaña estuvo inactiva
  const dt = Math.min((time - lastTime) / 1000, 0.05);
  lastTime = time;

  if (!paused) {
    for (const g of globos) {
      // Vector desde la posición actual hasta el destino
      const dx = g.targetX - g.x;
      const dy = g.targetY - g.y;

      // Distancia hasta el destino
      const dist = Math.sqrt(dx * dx + dy * dy);

      // Avanzamos hacia el destino a velocidad constante (px/s × segundos = px)
      if (dist > 1) {
        g.x += (dx / dist) * g.velocidad * dt;
        g.y += (dy / dist) * g.velocidad * dt;
      }

      // Aplicamos la nueva posición al div HTML
      g.el.style.left = g.x + "px";
      g.el.style.top  = g.y + "px";
    }
  }

  // Pedimos el siguiente fotograma (se llama ~60 veces por segundo)
  requestAnimationFrame(animar);
}


// EVENTOS DE LOS BOTONES

// Pausar / Reanudar
btnPausa.onclick = () => {
  paused = !paused;
  btnPausa.textContent = paused ? "Reanudar" : "Pausar";
};

// Reiniciar partida desde el HUD
btnReset.onclick = iniciar;

// Jugar de nuevo desde la pantalla de fin
btnNuevo.onclick = iniciar;


// ARRANQUE

iniciar();                    // Crea los globos de la primera partida
requestAnimationFrame(animar); // Arranca el bucle de animación
