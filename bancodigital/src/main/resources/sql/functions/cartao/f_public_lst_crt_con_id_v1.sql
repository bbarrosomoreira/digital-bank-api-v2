CREATE OR REPLACE FUNCTION public_lst_crt_con_id_v1(
		p_conta_id BIGINT
)

RETURNS SETOF cartao

LANGUAGE 'plpgsql'

AS $BODY$

BEGIN
		RETURN QUERY
		SELECT *
		FROM cartao
		WHERE conta_id = p_conta_id;

END;
$BODY$
