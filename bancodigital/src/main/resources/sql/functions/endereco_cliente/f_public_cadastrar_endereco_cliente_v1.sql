CREATE OR REPLACE FUNCTION public.public_cadastrar_endereco_cliente_v1(
	p_cep VARCHAR(10),
	p_rua VARCHAR(255),
	p_numero INT,
	p_complemento VARCHAR(100),
	p_bairro VARCHAR(100),
	p_cidade VARCHAR(100),
	p_estado CHAR(2),
	p_cliente_id BIGINT
)
RETURNS TABLE(
        id_endereco_cliente BIGINT
)
LANGUAGE 'plpgsql'


AS $BODY$

DECLARE
		v_id_endereco_cliente BIGINT;
BEGIN
		INSERT INTO endereco_cliente (
			cep,
			rua,
			numero,
			complemento,
			bairro,
			cidade,
			estado,
			cliente_id
		)
		VALUES (
		    p_cep,
		    p_rua,
		    p_numero,
		    p_complemento,
		    p_bairro,
		    p_cidade,
		    p_estado,
		    p_cliente_id
		)
		RETURNING id INTO v_id_endereco_cliente;

		RETURN QUERY SELECT v_id_endereco_cliente;

END;
$BODY$;