CREATE OR REPLACE FUNCTION public_lst_seg_id_v1(
		p_seguro_id BIGINT
)

RETURNS SETOF seguro

LANGUAGE 'plpgsql'

AS $BODY$

BEGIN
		RETURN QUERY
		SELECT *
		FROM seguro
		WHERE id = p_seguro_id;

END;
$BODY$
