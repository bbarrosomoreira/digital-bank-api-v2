CREATE OR REPLACE FUNCTION public_upd_con_v1(
	p_id BIGINT,
    p_numero_conta VARCHAR(255),
    p_saldo NUMERIC(19,2),
    p_moeda VARCHAR(10),
    p_cliente_id BIGINT,
    p_data_criacao DATE,
    p_tipo_conta VARCHAR(20),
    p_tarifa_manutencao NUMERIC(19,2),
    p_taxa_rendimento NUMERIC(5,4),
    p_saldo_em_reais NUMERIC(19,2)
)
RETURNS INT

LANGUAGE 'plpgsql'

AS $BODY$

BEGIN
		UPDATE conta
		SET
			numero_conta = p_numero_conta,
			saldo = p_saldo,
			moeda = p_moeda,
			cliente_id = p_cliente_id,
			data_criacao = p_data_criacao,
			tipo_conta = p_tipo_conta,
			tarifa_manutencao = p_tarifa_manutencao,
			taxa_rendimento = p_taxa_rendimento,
			saldo_em_reais = p_saldo_em_reais

		WHERE id = p_id;

		RETURN FOUND::INT;

END;
$BODY$
