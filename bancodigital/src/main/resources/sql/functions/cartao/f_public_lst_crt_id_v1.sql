CREATE OR REPLACE FUNCTION public_lst_crt_id_v1(
		p_cartao_id BIGINT
)

RETURNS SETOF cartao

LANGUAGE 'plpgsql'

AS $BODY$

BEGIN
		RETURN QUERY
		SELECT *
		FROM cartao
		WHERE cartao_id = p_cartao_id;

END;
$BODY$
