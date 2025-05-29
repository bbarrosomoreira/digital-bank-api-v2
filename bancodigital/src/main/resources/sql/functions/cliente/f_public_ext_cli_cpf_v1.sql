CREATE OR REPLACE FUNCTION public_ext_cli_cpf_v1(
	p_cpf VARCHAR(11)
)
RETURNS BOOLEAN

LANGUAGE 'plpgsql'

AS $BODY$

DECLARE
	v_existe BOOLEAN;

BEGIN
		SELECT EXISTS (
			SELECT 1 FROM cliente WHERE cpf = p_cpf
		) INTO v_existe;

		RETURN v_existe;

END;
$BODY$
