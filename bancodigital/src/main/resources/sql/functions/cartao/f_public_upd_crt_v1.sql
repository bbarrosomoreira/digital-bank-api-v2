CREATE OR REPLACE FUNCTION public_upd_crt_v1(
	p_id BIGINT,
	p_tipo_cartao VARCHAR(31),
	p_numero_cartao VARCHAR(255),
	p_conta_id BIGINT,
	p_status VARCHAR(10),
	p_senha VARCHAR(255),
	p_data_emissao DATE,
	p_data_vencimento DATE,
	p_taxa_utilizacao NUMERIC(5,4),
	p_limite NUMERIC(19,2),
	p_limite_atual NUMERIC(19,2),
	p_total_fatura NUMERIC(19,2),
	p_total_fatura_paga NUMERIC(19,2)
)
RETURNS INT

LANGUAGE 'plpgsql'

AS $BODY$

BEGIN
		UPDATE cartao
		SET
			tipo_cartao = p_tipo_cartao,
			numero_cartao = p_numero_cartao,
			conta_id = p_conta_id,
			status = p_status,
			senha = p_senha,
			data_emissao = p_data_emissao,
			data_vencimento = p_data_vencimento,
			taxa_utilizacao = p_taxa_utilizacao,
			limite = p_limite,
			limite_atual = p_limite_atual,
			total_fatura = p_total_fatura,
			total_fatura_paga = p_total_fatura_paga

		WHERE id = p_id;

		RETURN FOUND::INT;

END;
$BODY$
