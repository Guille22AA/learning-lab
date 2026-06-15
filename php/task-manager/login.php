<?php
include("conexion.php");
session_start();

function selectID(string $usuario, pdo $conexion){
    $sql = "SELECT ID FROM USUARIOS WHERE USUARIO = ?";
    $pst = $conexion->prepare($sql);
    $pst->execute([$usuario]);

    $fila = $pst->fetch(PDO::FETCH_ASSOC);

    return $fila["id"] ?? null;
}

$usuarios = [
    "admin" => 1234
];

if (isset($_COOKIE["usuario"])) {
    $_SESSION["usuario"] = $_COOKIE["usuario"];
    $_SESSION["id"] = selectID($_SESSION["usuario"], $conexion);
    header("Location: index.php");
}

if (isset($_POST["iniciar"]) && !isset($_COOKIE["usuario"])) {
    $usuario = $_POST["usuario"] ?? "";
    $password = $_POST["password"] ?? "";

    foreach($usuarios as $clave => $valor) {
        if($clave==$usuario && $valor==$password){
            $_SESSION["usuario"] = $usuario;
            $_SESSION["id"] = selectID($_SESSION["usuario"], $conexion);

            if(isset($_POST["recordar"])) {
                setcookie("usuarioCK", $usuario, time()+3600);
            } 
            header("Location: index.php");
        }
    }

    echo "Usuario o contraseña incorrecta.";
}

?>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login</title>
</head>
<body>
    <h2>Inicio de Sesión</h2>
    <form method="POST">
        Usuario: <input type="text" name="usuario"><br>
        Contraseña: <input type="password" name="password"><br>
        Recordar usuario: <input type="checkbox" name="recordar"><br>
        <button type="submit" name="iniciar">Login</button>
    </form>
</body>
</html>