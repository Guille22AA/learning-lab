<?php
include("conexion.php");
session_start();

if(!isset($_SESSION["usuario"])){
    header("Location: login.php");
    exit;
}


if(isset($_POST["insertar"])){
    $titulo = $_POST["titulo"] ?? "";
    $descripcion = $_POST["descripcion"] ?? "";
    $prioridad = $_POST["prioridad"];
    $fechaLimite = $_POST["fecha_limite"] ?? null;
    $completada = $_POST["completada"];

    $sql = "INSERT INTO TAREAS (TITULO, DESCRIPCION, PRIORIDAD, FECHA_LIMITE, COMPLETADA, ID_USUARIO) VALUES (?, ?, ?, ?, ?, ?)";
    $resultado = $conexion->prepare($sql);
    $resultado->execute([$titulo, $descripcion, $prioridad, $fechaLimite, $completada, $_SESSION["id"]]);


    $fila = $resultado->fetch(PDO::FETCH_ASSOC);

    if($fila == null) {
        echo "Error al insertar.";
        exit;
    } else {
        header("Location: index.php");
        exit;
    }
}

?>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Crear</title>
</head>
<body>
    <h3>Inserte una nueva tarea:</h3>
    <form method="POST">
        Título: <input type="text" name="titulo" requried><br>
        Descripción: <textarea name="descripcion"></textarea><br>
        Prioridad: 
        <select name="prioridad">
            <option value="baja">Baja</option>
            <option value="media">Media</option>
            <option value="alta">Alta</option>
        </select><br>
        Fecha límite: <input type="date" name="fecha_limite"><br>
        Completada: <input type="checkbox" name="completada"><br>

        <button type="submit" name="insertar">Insertar</button>
    </form>
</body>
</html>