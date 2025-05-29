CREATE OR REPLACE FUNCTION public_ext_crt_con_id_v1(
	p_conta_id BIGINT
)
RETURNS BOOLEAN

LANGUAGE 'plpgsql'

AS $BODY$

DECLARE
	v_existe BOOLEAN;

BEGIN
		SELECT EXISTS (
			SELECT 1 FROM cartao WHERE conta_id = p_conta_id
		) INTO v_existe;

		RETURN v_existe;

END;
$BODY$
