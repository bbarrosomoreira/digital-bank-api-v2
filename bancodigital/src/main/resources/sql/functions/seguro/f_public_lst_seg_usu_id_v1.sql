CREATE OR REPLACE FUNCTION public_lst_seg_usu_id_v1(
		p_usuario_id BIGINT
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
		JOIN cliente cl ON co.cliente_id = cl.id
		WHERE cl.usuario_id = p_usuario_id;

END;
$BODY$
