CREATE TABLE Obras
(
    obra_id   INT AUTO_INCREMENT PRIMARY KEY,
    titulo    VARCHAR(255) NOT NULL,
    descricao TEXT tipo ENUM('filme', 'serie') NOT NULL,
    ano_lancamento INT,
    poster_url VARCHAR(255),
    duracao INT NULL -- minutos (só para filmes normalmente)
)