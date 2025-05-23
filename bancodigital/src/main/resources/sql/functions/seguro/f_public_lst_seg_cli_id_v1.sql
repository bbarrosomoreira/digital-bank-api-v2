CREATE OR REPLACE FUNCTION public_lst_seg_cli_id_v1(
		p_cliente_id BIGINT
)

RETURNS SETOF seguro

LANGUAGE 'plpgsql'

AS $BODY$

BEGIN
		RETURN QUERY
		SELECT s.*
		FROM seguro s
		JOIN cartao c ON s.cartao_id = c.id
		JOIN conta co ON c.conta_id = co.id
		WHERE co.cliente_id = p_cliente_id;

END;
$BODY$
