<?php
try{
    $conexion = new PDO("mysql: host=localhost; dbname=gestor_tareas;charset=utf8", "gestor_tareas", "oracle123");
} catch (PDOException $e) {
    die("Error: " . $e->getMessage());
}
?>