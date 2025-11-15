CREATE TABLE obra_categoria (
    obra_id BIGINT NOT NULL,
    categoria_id BIGINT NOT NULL,
    PRIMARY KEY (obra_id, categoria_id),
    FOREIGN KEY (obra_id) REFERENCES obra(id) ON DELETE CASCADE,
    FOREIGN KEY (categoria_id) REFERENCES categoria(id) ON DELETE CASCADE
);

CREATE TABLE temporada (
    id UUID PRIMARY KEY ,
    numero INTEGER NOT NULL,
    quantidade_episodios INTEGER,
    descricao TEXT,
    obra_id BIGINT NOT NULL,
    FOREIGN KEY (obra_id) REFERENCES obra(id) ON DELETE CASCADE
);

CREATE TABLE watchlist (
    id BIGSERIAL PRIMARY KEY,
    usuario_id UUID NOT NULL,
    obra_id BIGINT NOT NULL,
    data_adicionado TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (usuario_id) REFERENCES usuario(id) ON DELETE CASCADE,
    FOREIGN KEY (obra_id) REFERENCES obra(id) ON DELETE CASCADE,
    UNIQUE(usuario_id, obra_id)
);

CREATE TABLE assistido (
    id BIGSERIAL PRIMARY KEY,
    usuario_id UUID NOT NULL,
    obra_id BIGINT NOT NULL,
    data_assistido TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (usuario_id) REFERENCES usuario(id) ON DELETE CASCADE,
    FOREIGN KEY (obra_id) REFERENCES obra(id) ON DELETE CASCADE,
    UNIQUE(usuario_id, obra_id)
);