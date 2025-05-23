CREATE OR REPLACE FUNCTION public_lst_cli_usu_id_v1(
		p_usuario_id BIGINT
)

RETURNS cliente

LANGUAGE 'plpgsql'

AS $BODY$

DECLARE
		v_cliente_encontrado cliente;

BEGIN
		SELECT * INTO v_cliente_encontrado
		FROM cliente
		WHERE usuario_id = p_usuario_id;

		RETURN v_cliente_encontrado;

END;
$BODY$