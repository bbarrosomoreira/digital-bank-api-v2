-- FUNCTION: public.ext_usu_ema_v1(character varying)

-- DROP FUNCTION IF EXISTS public.ext_usu_ema_v1(character varying);

CREATE OR REPLACE FUNCTION public.ext_usu_ema_v1(
	p_email character varying)
    RETURNS boolean
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
AS $BODY$

DECLARE
		v_existe_usuario BOOLEAN;

BEGIN
		SELECT EXISTS (
			SELECT 1 FROM usuario WHERE email = p_email
		) INTO v_existe_usuario;

		RETURN v_existe_usuario;

END;
$BODY$;

ALTER FUNCTION public.ext_usu_ema_v1(character varying)
    OWNER TO postgres;
