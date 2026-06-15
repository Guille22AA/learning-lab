/*
====================================
        ÁREA DE UN POLÍGONO
====================================

Crea una función llamada areaPoligono que reciba:

- base (número)
- altura (número)
- tipo de polígono (string)

La función debe devolver el área del polígono.

====================================

CASOS:

- "triangulo" → (base * altura) / 2
- "rectangulo" → base * altura
- "cuadrado" → base * altura (o base²)

====================================

EJEMPLOS:

areaPoligono(10, 5, "triangulo")
-> 25

areaPoligono(10, 5, "rectangulo")
-> 50

areaPoligono(5, 5, "cuadrado")
-> 25

====================================

PISTA:
Usa condicionales (if / switch) según el tipo.
*/

function areaPoligono(base, altura, tipo) {

    if (tipo === "triangulo") {
        return (base * altura) / 2;
    }

    if (tipo === "rectangulo") {
        return base * altura;
    }

    if (tipo === "cuadrado") {
        return base * altura;
    }

    return "Tipo de polígono no válido";
}


console.log(areaPoligono(10, 5, "triangulo"));  
console.log(areaPoligono(10, 5, "rectangulo")); 
console.log(areaPoligono(5, 5, "cuadrado"));   