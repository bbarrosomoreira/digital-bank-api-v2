CREATE OR REPLACE FUNCTION public.public_emitir_cartao_v1(
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
RETURNS TABLE(
        id_cartao BIGINT
)
LANGUAGE 'plpgsql'

AS $BODY$

DECLARE
		v_id_cartao BIGINT;
BEGIN
		INSERT INTO cartao (
			tipo_cartao,
			numero_cartao,
			conta_id,
			status,
			senha,
			data_emissao,
			data_vencimento,
			taxa_utilizacao,
			limite,
			limite_atual,
			total_fatura,
			total_fatura_paga
		)
		VALUES (
			p_tipo_cartao,
		    p_numero_cartao,
		    p_conta_id,
		    p_status,
		    p_senha,
		    p_data_emissao,
		    p_data_vencimento,
		    p_taxa_utilizacao,
			p_limite,
			p_limite_atual,
		    p_total_fatura,
		    p_total_fatura_paga
		)
		RETURNING id INTO v_id_cartao;

		RETURN QUERY SELECT v_id_cartao;

END;
$BODY$;