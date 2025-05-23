CREATE OR REPLACE FUNCTION public_lst_con_usu_id_v1(
		p_usuario_id BIGINT
)

RETURNS SETOF conta

LANGUAGE 'plpgsql'

AS $BODY$

BEGIN
		RETURN QUERY
		SELECT c.*
		FROM conta c
		JOIN cliente cl ON c.cliente_id = cl.id
		WHERE cl.usuario_id = p_usuario_id;

END;
$BODY$