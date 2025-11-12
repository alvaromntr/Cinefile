CREATE TABLE Watchlist(
usuario_id UUID NOT NULL,
obra_id BIGSERIAL NOT NULL,
data_adicionado TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
PRIMARY KEY (usuario_id, obra_id),
FOREIGN KEY (usuario_id) REFERENCES Usuario(id),
FOREIGN KEY (obra_id) REFERENCES Obras(obraid)
);