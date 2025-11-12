CREATE TABLE obra_categoria (
    obraid BIGINT NOT NULL,
    categoria_id INT NOT NULL,
    PRIMARY KEY (obraid, categoria_id),
    FOREIGN KEY (obraid) REFERENCES obras(obraid),
    FOREIGN KEY (categoria_id) REFERENCES categorias(categoria_id)
);
