CREATE OR REPLACE FUNCTION public_lst_con_id_v1(
		p_conta_id BIGINT
)

RETURNS conta

LANGUAGE 'plpgsql'

AS $BODY$

DECLARE
		v_conta_encontrado conta;

BEGIN
		SELECT * INTO v_conta_encontrado
		FROM conta
		WHERE conta_id = p_conta_id;

		RETURN v_conta_encontrado;

END;
$BODY$