CREATE TABLE configuracoes(
config_id BIGSERIAL PRIMARY KEY,
usuario_id UUID NOT NULL,
preferencias JSON,
FOREIGN KEY (usuario_id) REFERENCES Usuario(id)
);