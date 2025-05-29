CREATE OR REPLACE FUNCTION public_lst_crt_usu_id_v1(
		p_usuario_id BIGINT
)

RETURNS SETOF cartao

LANGUAGE 'plpgsql'

AS $BODY$

BEGIN
		RETURN QUERY
		SELECT c.*
		FROM cartao c
		JOIN conta conta ON c.conta_id = conta.id
		JOIN cliente cl ON conta.cliente_id = cl.id
		WHERE cl.usuario_id = p_usuario_id;

END;
$BODY$
