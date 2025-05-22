CREATE OR REPLACE FUNCTION public_lst_usu_id_v1(
		p_usuario_id BIGINT
)

RETURNS usuario

LANGUAGE 'plpgsql'

AS $BODY$

DECLARE
		v_usuario_encontrado usuario;

BEGIN
		SELECT * INTO v_usuario_encontrado
		FROM usuario
		WHERE usuario_id = p_usuario_id;

		RETURN v_usuario_encontrado;

END;
$BODY$
