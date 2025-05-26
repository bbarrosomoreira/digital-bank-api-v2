CREATE OR REPLACE FUNCTION public.public_criar_usuario_v1(
	p_email VARCHAR(255),
	p_senha VARCHAR(255),
	p_role VARCHAR(20)
)
RETURNS TABLE(
        id_usuario BIGINT
)
LANGUAGE 'plpgsql'

AS $BODY$

DECLARE
		v_id_usuario BIGINT;
BEGIN
		INSERT INTO usuario (
			email,
			senha,
			role
		)
		VALUES (
			p_email,
		    p_senha,
		    p_role
		)
		RETURNING id INTO v_id_usuario;

		RETURN QUERY SELECT v_id_usuario;

END;
$BODY$;