CREATE OR REPLACE FUNCTION public_lst_seg_crt_id_v1(
		p_cartao_id BIGINT
)

RETURNS SETOF seguro

LANGUAGE 'plpgsql'

AS $BODY$

BEGIN
		RETURN QUERY
		SELECT *
		FROM seguro
		WHERE cartao_id = p_cartao_id;

END;
$BODY$
