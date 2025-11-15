CREATE TABLE IF NOT EXISTS obras (
    obraid BIGSERIAL PRIMARY KEY, --
    titulo VARCHAR(255),
    descricao TEXT,
    tipo VARCHAR(50),
    anolancamento INT,
    poster_url TEXT,
    duracao INT
);
