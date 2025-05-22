CREATE TABLE IF NOT EXISTS endereco_cliente (
    id BIGSERIAL PRIMARY KEY,
    cep VARCHAR(10),
    rua VARCHAR(255),
    numero INT,
    complemento VARCHAR(100),
    bairro VARCHAR(100),
    cidade VARCHAR(100),
    estado CHAR(2),
    cliente_id BIGINT NOT NULL,
    CONSTRAINT fk_endereco_cliente FOREIGN KEY (cliente_id) REFERENCES cliente(id)
);