/*
====================================
            FIZZBUZZ CON FUNCIÓN
====================================

Crea una función llamada fizzBuzz que reciba
un número como parámetro.

La función debe devolver:

- "Fizz" si el número es divisible entre 3.
- "Buzz" si el número es divisible entre 5.
- "FizzBuzz" si es divisible entre 3 y 5.
- El propio número si no cumple ninguna condición.

Después usa un bucle del 1 al 100 para llamar
a la función y mostrar los resultados.

Ejemplo:

fizzBuzz(3)  -> "Fizz"
fizzBuzz(5)  -> "Buzz"
fizzBuzz(15) -> "FizzBuzz"
fizzBuzz(7)  -> 7

====================================
*/

function fizzBuzz(number) {

  if (number % 3 === 0 && number % 5 === 0) {
    return "FizzBuzz";
  } else if (number % 3 === 0) {
    return "Fizz";
  } else if (number % 5 === 0) {
    return "Buzz";
  } else {
    return number;
  }
}

for (let i = 1; i<= 100; i++) {
  console.log(fizzBuzz(i));
}
