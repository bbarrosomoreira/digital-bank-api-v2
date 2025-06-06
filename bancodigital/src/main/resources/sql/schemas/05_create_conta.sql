CREATE TABLE IF NOT EXISTS conta (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY,
    numero_conta VARCHAR(255) NOT NULL UNIQUE,
    saldo NUMERIC(19,2) NOT NULL,
    moeda VARCHAR(10) NOT NULL CHECK (moeda IN ('BRL','EUR','USD')),
    cliente_id BIGINT NOT NULL,
    data_criacao DATE NOT NULL,
    tipo_conta VARCHAR(20) NOT NULL CHECK (tipo_conta IN ('CORRENTE', 'POUPANCA', 'INTERNACIONAL')),

    -- campos específicos por tipo
    tarifa_manutencao NUMERIC(19,2), -- para CORRENTE e INTERNACIONAL
    taxa_rendimento NUMERIC(5,4),   -- para POUPANCA
    saldo_em_reais NUMERIC(19,2),    -- para INTERNACIONAL

	PRIMARY KEY (id),
    CONSTRAINT fk_conta_cliente FOREIGN KEY (cliente_id) REFERENCES cliente(id)
);