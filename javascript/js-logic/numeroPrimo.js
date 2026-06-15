/*
====================================
          NÚMEROS PRIMOS
====================================

Crea una función llamada esPrimo que reciba un número n.

La función debe devolver:

- true si el número es primo
- false si no lo es

Un número primo es aquel que:

- Es mayor que 1
- Solo es divisible entre 1 y él mismo

Ejemplos:

esPrimo(2)  -> true
esPrimo(3)  -> true
esPrimo(4)  -> false
esPrimo(7)  -> true
esPrimo(10) -> false

====================================

PISTA:

Un número n NO es primo si existe algún número
entre 2 y n-1 que lo divide exactamente.

Puedes comprobarlo con el operador %:

n % i === 0  -> significa que es divisible
*/

function esPrimo(n) {

    if (n <= 1) return false;

    for (let i = 2; i < n; i++) {
        if (n % i === 0) {
            return false;
        }
    }

    return true;
}

console.log(esPrimo(2));  // true
console.log(esPrimo(3));  // true
console.log(esPrimo(4));  // false
console.log(esPrimo(10)); // false