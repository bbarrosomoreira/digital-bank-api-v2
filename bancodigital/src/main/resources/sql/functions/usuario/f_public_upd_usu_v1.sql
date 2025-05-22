CREATE OR REPLACE FUNCTION public_upd_usu_v1(
	p_id BIGINT,
	p_email VARCHAR(255),
	p_senha VARCHAR(255),
	p_role VARCHAR(20)
)
RETURNS INT

LANGUAGE 'plpgsql'

AS $BODY$

BEGIN
		UPDATE usuario
		SET
			email = p_email,
			senha = p_senha,
			role = p_role

		WHERE id = p_id;

		RETURN FOUND::INT;

END;
$BODY$
