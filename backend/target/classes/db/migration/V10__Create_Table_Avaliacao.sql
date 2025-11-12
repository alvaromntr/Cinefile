CREATE TABLE Avaliacao(
avaliacao_id UUID PRIMARY KEY,
usuario_id UUID NOT NULL,
obra_id BIGSERIAL NOT NULL,
nota DECIMAL(3,1) CHECK (nota >= 0 AND nota <= 10),
data_avaliacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
FOREIGN KEY (usuario_id) REFERENCES Usuario(id),
FOREIGN KEY (obra_id) REFERENCES Obras(obraid)
);