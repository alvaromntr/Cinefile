CREATE TABLE usuario (
    id UUID PRIMARY KEY,
    email VARCHAR(50) NOT NULL ,
    username VARCHAR(50) UNIQUE NOT NULL,
    senha_hash VARCHAR(50) NOT NULL,
    foto_perfil VARCHAR(150) NULL
);
