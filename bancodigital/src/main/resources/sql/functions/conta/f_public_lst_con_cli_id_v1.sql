CREATE OR REPLACE FUNCTION public_lst_con_cli_id_v1(
		p_cliente_id BIGINT
)

RETURNS conta

LANGUAGE 'plpgsql'

AS $BODY$

DECLARE
		v_conta_encontrado conta;

BEGIN
		SELECT * INTO v_conta_encontrado
		FROM conta
		WHERE cliente_id = p_cliente_id;

		RETURN v_conta_encontrado;

END;
$BODY$