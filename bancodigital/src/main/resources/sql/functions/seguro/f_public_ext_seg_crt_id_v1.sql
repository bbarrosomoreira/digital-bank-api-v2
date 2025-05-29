CREATE OR REPLACE FUNCTION public_ext_seg_crt_id_v1(
	p_cartao_id BIGINT
)
RETURNS BOOLEAN

LANGUAGE 'plpgsql'

AS $BODY$

BEGIN
		RETURN EXISTS (
			SELECT 1
			FROM seguro
			WHERE cartao_id = p_cartao_id
		);

END;
$BODY$
