CREATE TABLE comentario(
comentario_id BIGSERIAL PRIMARY KEY,
usuario_id UUID NOT NULL,
obra_id BIGSERIAL NOT NULL,
texto TEXT NOT NULL,
data_comentario TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
FOREIGN KEY (usuario_id) REFERENCES Usuario(id),
FOREIGN KEY (obra_id) REFERENCES Obras(obraid)
);