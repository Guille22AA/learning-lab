# 🎈 Balloon Popper

A simple balloon-popping game built with plain HTML, CSS and JavaScript. No frameworks, no libraries — just open `index.html` and play.

## How to play

Click the balloons before they escape. Small ones move faster but give more points.

| Balloon | Points |
|---|---|
| Normal | +10 |
| Fast (small) | +20 |

## Project structure

```
globos/
├── index.html   # markup
├── style.css    # styles
└── app.js       # game logic
```

## What's going on under the hood

- **DOM manipulation** — selecting elements, moving them with `style.left / top`, toggling CSS classes
- **CSS transitions** — the explosion animation is just a class added from JS; CSS handles the rest
- **Game loop** — `requestAnimationFrame` runs ~60 times per second and moves every balloon a little bit each frame
- **Delta time** — movement uses the time between frames so the game runs at the same speed on any screen
- **Direction vectors** — each balloon moves toward a target point using basic Pythagorean math to keep speed constant
- **Timers** — `setInterval` picks a new random target every 1–2 seconds; `setTimeout` waits for the explosion animation before removing the element
- **Named constants** — all tunable values (`RADIO_MIN`, `VEL_MAX`, `PUNTOS_RAPIDO`…) sit at the top of `app.js` so difficulty is easy to tweak