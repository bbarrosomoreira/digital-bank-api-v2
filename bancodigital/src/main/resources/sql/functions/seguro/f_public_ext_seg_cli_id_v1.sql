CREATE OR REPLACE FUNCTION public_ext_seg_cli_id_v1(
	p_cliente_id BIGINT
)
RETURNS BOOLEAN

LANGUAGE 'plpgsql'

AS $BODY$

BEGIN
		RETURN EXISTS (
			SELECT 1
			FROM seguro s
			JOIN cartao c ON s.cartao_id = c.id
			JOIN conta co ON c.conta_id = co.id
			WHERE co.cliente_id = p_cliente_id
		);

END;
$BODY$
