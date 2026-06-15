<?php
include("conexion.php");
session_start();

if(!isset($_SESSION["usuario"])){
    header("Location: login.php");
    exit;
}

$tareaId = $_GET["id"];

$sql = "DELETE FROM TAREAS WHERE ID = ?";
$resultado = $conexion->prepare($sql);
$resultado->execute([$tareaId]);

header("Location: index.php");

?>