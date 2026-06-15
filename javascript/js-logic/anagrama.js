/*
====================================
              ANAGRAMAS
====================================

Crea una función llamada esAnagrama que reciba
dos palabras como parámetros.

La función debe devolver:

- true si las palabras son anagramas.
- false si no lo son.

Dos palabras son anagramas si tienen exactamente
las mismas letras con la misma cantidad de veces,
pero en distinto orden.

Ejemplos:

esAnagrama("roma", "amor")
-> true

esAnagrama("listen", "silent")
-> true

esAnagrama("hola", "adios")
-> false


Pistas:

1. Puedes pasar las palabras a minúsculas usando:
   .toLowerCase()

2. Puedes convertir un string en array usando:
   .split("")

   Ejemplo:
   "hola".split("")
   -> ["h","o","l","a"]

3. Puedes ordenar arrays usando:
   .sort()

4. Puedes unir un array otra vez usando:
   .join("")


====================================
*/

function esAnagrama(w1, w2) {

    let firstWord = w1.toLowerCase().split("").sort().join("");
    let secondWord = w2.toLowerCase().split("").sort().join("");
    /*
    sort() para ordenar las letras en orden alfabético y después comparar las cadenas
    join() para unir en en un mismo String todas las letras que hemos separado con split
     */

    return firstWord === secondWord;
}

console.log(esAnagrama("amor", "roma"));
console.log(esAnagrama("hola", "adios"));