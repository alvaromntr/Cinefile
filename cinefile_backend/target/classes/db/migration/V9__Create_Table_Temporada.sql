CREATE TABLE temporada(
temporada_id UUID PRIMARY KEY NOT NULL,
usuario_id UUID NOT NULL,
nota DECIMAL(3,1) CHECK (nota >= 0 AND nota <= 10),
data_avaliacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
FOREIGN KEY (usuario_id) REFERENCES Usuario(id),
FOREIGN KEY (temporada_id) REFERENCES Temporada(temporada_id)
);