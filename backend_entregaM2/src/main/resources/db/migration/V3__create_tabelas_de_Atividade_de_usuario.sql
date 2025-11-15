CREATE TABLE comentario (
    id BIGSERIAL PRIMARY KEY,
    texto TEXT NOT NULL,
    data_comentario TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    usuario_id UUID NOT NULL,
    obra_id BIGINT NOT NULL,
    FOREIGN KEY (usuario_id) REFERENCES usuario(id) ON DELETE CASCADE,
    FOREIGN KEY (obra_id) REFERENCES obra(id) ON DELETE CASCADE
);

CREATE TABLE avaliacao (
    id UUID PRIMARY KEY,
    nota INTEGER CHECK (nota >= 1 AND nota <= 5),
    comentario TEXT,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    obra_id BIGINT NOT NULL,
    usuario_id UUID NOT NULL,
    temporada_id UUID,
    FOREIGN KEY (obra_id) REFERENCES obra(id) ON DELETE CASCADE,
    FOREIGN KEY (usuario_id) REFERENCES usuario(id) ON DELETE CASCADE,
    FOREIGN KEY (temporada_id) REFERENCES temporada(id) ON DELETE CASCADE
);

CREATE TABLE log_visualizacao (
    id UUID PRIMARY KEY,
    usuario_id UUID NOT NULL,
    obra_id BIGINT NOT NULL,
    data_visualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (usuario_id) REFERENCES usuario(id) ON DELETE CASCADE,
    FOREIGN KEY (obra_id) REFERENCES obra(id) ON DELETE CASCADE
);