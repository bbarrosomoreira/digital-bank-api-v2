CREATE OR REPLACE FUNCTION public_abrir_conta_v1(
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
RETURNS TABLE (
		 id_conta BIGINT
)
LANGUAGE 'plpgsql'

AS $BODY$

DECLARE
		v_id_conta BIGINT;
BEGIN
		INSERT INTO conta (
			numero_conta,
			saldo,
			moeda,
			cliente_id,
			data_criacao,
			tipo_conta,
			tarifa_manutencao,
			taxa_rendimento,
			saldo_em_reais
		)
		VALUES (
		    p_numero_conta,
		    p_saldo,
		    p_moeda,
		    p_cliente_id,
		    p_data_criacao,
		    p_tipo_conta,
		    p_tarifa_manutencao,
		    p_taxa_rendimento,
		    p_saldo_em_reais
		)
		RETURNING id INTO v_id_conta;

		RETURN QUERY SELECT v_id_conta;

END;
$BODY$