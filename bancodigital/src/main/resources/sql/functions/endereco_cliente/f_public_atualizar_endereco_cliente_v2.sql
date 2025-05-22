CREATE OR REPLACE FUNCTION public_atualizar_endereco_cliente_v2(
	p_id BIGINT,
    p_cep VARCHAR(10),
    p_rua VARCHAR(255),
    p_numero INT,
    p_complemento VARCHAR(100),
    p_bairro VARCHAR(100),
    p_cidade VARCHAR(100),
    p_estado CHAR(2),
    p_cliente_id BIGINT
)
RETURNS INT

LANGUAGE 'plpgsql'

AS $BODY$

BEGIN
		UPDATE endereco_cliente
		SET
			cep = p_cep,
			rua = p_rua,
			numero = p_numero,
			complemento = p_complemento,
			bairro = p_bairro,
			cidade = p_cidade,
			estado = p_estado
		WHERE id = p_id;

		RETURN FOUND::INT;

END;
$BODY$