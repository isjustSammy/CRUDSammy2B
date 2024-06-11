create table Registro(
UUID_Registro VARCHAR2(50),
Correo VARCHAR2 (50),
Contrasena VARCHAR2(50)
);

create table Ticket(
UUID_Ticket VARCHAR2(50) not null,
Numero_Ticket INT not null,
Titulo_Ticket VARCHAR2(50),
Descripcion_Ticket VARCHAR(50),
Autor_Ticket VARCHAR2(50),
Correo_autor VARCHAR2(50),
Fecha DATE,
Estado_Ticket VARCHAR2(50),
FechaF DATE
);










































































