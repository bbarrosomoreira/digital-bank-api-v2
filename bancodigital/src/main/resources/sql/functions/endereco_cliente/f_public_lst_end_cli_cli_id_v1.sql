CREATE OR REPLACE FUNCTION public_listar_endereco_cliente_por_cliente_id_v1(
		p_cliente_id BIGINT
)

RETURNS endereco_cliente

LANGUAGE 'plpgsql'

AS $BODY$

DECLARE
		v_endereco_encontrado endereco_cliente;

BEGIN
		SELECT * INTO v_endereco_encontrado
		FROM endereco_cliente
		WHERE cliente_id = p_cliente_id;

		RETURN v_endereco_encontrado;

END;
$BODY$