CREATE OR REPLACE FUNCTION public_ext_con_cli_id_v1(
	p_cliente_id BIGINT
)
RETURNS BOOLEAN

LANGUAGE 'plpgsql'

AS $BODY$

DECLARE
	v_existe BOOLEAN;

BEGIN
		SELECT EXISTS (
			SELECT 1 FROM conta WHERE cliente_id = p_cliente_id
		) INTO v_existe;

		RETURN v_existe;

END;
$BODY$
