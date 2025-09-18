CREATE TABLE usuario (
  id INT PRIMARY KEY,
  username VARCHAR(100) UNIQUE NOT NULL,
  senha_hash VARCHAR(255) NOT NULL,
  foto_perfil VARCHAR(255) NULL
);
