CREATE OR REPLACE FUNCTION public.lst_usu_ema_v1(
		p_email VARCHAR(255)
)

RETURNS usuario

LANGUAGE 'plpgsql'

AS $BODY$

DECLARE
		v_usuario_encontrado usuario;

BEGIN
		SELECT * INTO v_usuario_encontrado
		FROM usuario
		WHERE email = p_email;

		RETURN v_usuario_encontrado;

END;
$BODY$
