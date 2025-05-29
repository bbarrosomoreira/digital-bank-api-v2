CREATE OR REPLACE FUNCTION public_upd_cli_v1(
	p_id BIGINT,
	p_nome VARCHAR(100),
	p_cpf VARCHAR(11),
	p_categoria VARCHAR(20),
	p_data_nascimento DATE,
	p_usuario_id BIGINT
)
RETURNS INT

LANGUAGE 'plpgsql'

AS $BODY$

BEGIN
		UPDATE cliente
		SET
			nome = p_nome,
			cpf = p_cpf,
			categoria = p_categoria,
			data_nascimento = p_data_nascimento,
			usuario_id = p_usuario_id

		WHERE id = p_id;

		RETURN FOUND::INT;

END;
$BODY$