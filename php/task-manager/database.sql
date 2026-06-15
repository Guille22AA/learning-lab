CREATE TABLE usuarios (
    id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    usuario VARCHAR2(100) NOT NULL,
    password VARCHAR2(255) NOT NULL
);

CREATE TABLE tareas (
    id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    id_usuario INT NOT NULL,
    titulo VARCHAR2(255) NOT NULL,
    descripcion CLOB,
    prioridad VARCHAR2(50),
    fecha_limite DATE,
    completada NUMBER(1) DEFAULT 0,
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id)
);