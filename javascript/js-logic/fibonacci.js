/*
====================================
            FIBONACCI
====================================

La sucesión de Fibonacci es una serie de números donde:

- El primer número es 0
- El segundo número es 1
- A partir de ahí, cada número es la suma de los dos anteriores

Ejemplo de la serie:

0, 1, 1, 2, 3, 5, 8, 13, 21, 34, ...

====================================

EJERCICIO:

Crea una función llamada fibonacci que reciba un número n.

La función debe devolver:

- Un array con los primeros n números de la sucesión de Fibonacci.

Ejemplos:

fibonacci(1)
-> [0]

fibonacci(2)
-> [0, 1]

fibonacci(5)
-> [0, 1, 1, 2, 3]

fibonacci(10)
-> [0, 1, 1, 2, 3, 5, 8, 13, 21, 34]

====================================

PISTA:

- Usa un array para ir guardando los resultados
- Los dos primeros valores están “dados”
- A partir de ahí, cada nuevo valor es:
  array[i - 1] + array[i - 2]
*/

function fibonacci(n) {

    let result = [];

    for (let i = 0; i < n; i++) {

        if (i === 0) {
            result.push(0);
        } 
        else if (i === 1) {
            result.push(1);
        } 
        else {
            result.push(result[i - 1] + result[i - 2]);
        }
    }

    return result;
}


console.log(fibonacci(1));  
console.log(fibonacci(2));  
console.log(fibonacci(5));  
console.log(fibonacci(10)); 