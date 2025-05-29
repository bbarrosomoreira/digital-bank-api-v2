CREATE OR REPLACE FUNCTION public_lst_crt_cli_id_v1(
		p_cliente_id BIGINT
)

RETURNS SETOF cartao

LANGUAGE 'plpgsql'

AS $BODY$

BEGIN
		RETURN QUERY
		SELECT c.*
		FROM cartao c
		JOIN conta conta ON c.conta_id = conta.id
		WHERE conta.cliente_id = p_cliente_id;

END;
$BODY$
