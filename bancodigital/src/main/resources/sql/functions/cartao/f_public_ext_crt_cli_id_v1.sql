CREATE OR REPLACE FUNCTION public_ext_crt_cli_id_v1(
	p_cliente_id BIGINT
)
RETURNS BOOLEAN

LANGUAGE 'plpgsql'

AS $BODY$

BEGIN
		RETURN EXISTS (
			SELECT 1
			FROM cartao c
			JOIN conta co ON c.conta_id = co.id
			WHERE co.cliente_id = p_cliente_id
		);

END;
$BODY$
