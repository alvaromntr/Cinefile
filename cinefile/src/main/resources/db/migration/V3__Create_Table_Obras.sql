CREATE TABLE IF NOT EXISTS obras (
    obra_id UUID PRIMARY KEY,
    titulo VARCHAR(255),
    descricao TEXT,
    tipo VARCHAR(50),
    ano_lancamento INT,
    poster_url TEXT,
    duracao INT,
    active BOOLEAN DEFAULT TRUE
);
