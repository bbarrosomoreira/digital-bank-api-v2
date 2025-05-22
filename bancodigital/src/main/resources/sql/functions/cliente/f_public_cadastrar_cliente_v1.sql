CREATE OR REPLACE FUNCTION public.public_cadastrar_cliente_v1(
	p_nome VARCHAR(100),
	p_cpf VARCHAR(11),
	p_categoria VARCHAR(20),
	p_data_nascimento DATE,
	p_usuario_id BIGINT
)
RETURNS TABLE(
        id_cliente BIGINT
)
LANGUAGE 'plpgsql'

AS $BODY$

DECLARE
		v_id_cliente BIGINT;
BEGIN
		INSERT INTO cliente (
			nome,
			cpf,
			categoria,
			data_nascimento,
			usuario_id
		)
		VALUES (
		    p_nome,
		    p_cpf,
		    p_categoria,
		    p_data_nascimento,
		    p_usuario_id
		)
		RETURNING id INTO v_id_cliente;

		RETURN QUERY SELECT v_id_cliente;

END;
$BODY$;