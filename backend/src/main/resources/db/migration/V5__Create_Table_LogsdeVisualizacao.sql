CREATE TABLE logsdevisualizacao (
    log_id UUID PRIMARY KEY,
    usuario_id UUID NOT NULL,
    obra_id BIGSERIAL NOT NULL,
    data_assistido DATE NOT NULL,
    FOREIGN KEY (usuario_id) REFERENCES usuario(id),
    FOREIGN KEY (obra_id) REFERENCES obras(obraid)
);
