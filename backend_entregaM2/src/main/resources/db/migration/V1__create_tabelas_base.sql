
CREATE TABLE usuario (
    id UUID PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(120) UNIQUE NOT NULL,
    senha_hash VARCHAR(100) NOT NULL,
    foto_perfil VARCHAR(255),
    ativo BOOLEAN DEFAULT true,
    role VARCHAR(20) NOT NULL DEFAULT 'USUARIO'
);

CREATE TABLE obra (
    id BIGSERIAL PRIMARY KEY,
    tmdb_id BIGINT UNIQUE,
    titulo VARCHAR(255),
    descricao VARCHAR(2000),
    tipo VARCHAR(50) CHECK (tipo IN ('FILME', 'SERIE')),
    ano_lancamento INTEGER,
    poster_url TEXT,
    duracao INTEGER
);

CREATE TABLE categoria (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(50) UNIQUE NOT NULL
);

CREATE TABLE configuracoes (
    id BIGSERIAL PRIMARY KEY,
    usuario_id UUID NOT NULL UNIQUE,
    receber_notificacoes_email BOOLEAN DEFAULT true,
    tema_interface VARCHAR(20) DEFAULT 'light',
    idioma_preferido VARCHAR(10) DEFAULT 'pt-BR',
    FOREIGN KEY (usuario_id) REFERENCES usuario(id) ON DELETE CASCADE
);