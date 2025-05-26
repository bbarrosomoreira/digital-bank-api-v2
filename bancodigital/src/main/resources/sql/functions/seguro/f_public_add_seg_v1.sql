CREATE OR REPLACE FUNCTION public_contratar_seguro_v1(
    p_tipo_seguro VARCHAR(20),
    p_num_apolice VARCHAR(50),
    p_cartao_id BIGINT,
    p_data_contratacao DATE,
    p_valor_apolice NUMERIC(19,2),
    p_descricao_condicoes VARCHAR(300),
    p_premio_apolice NUMERIC(19,2),
    p_status_seguro VARCHAR(50),
    p_data_acionamento DATE,
    p_valor_fraude NUMERIC(19,2)
)
RETURNS TABLE (
		 id_seguro BIGINT
)
LANGUAGE 'plpgsql'

AS $BODY$

DECLARE
		v_id_seguro BIGINT;
BEGIN
		INSERT INTO seguro (
			tipo_seguro,
			num_apolice,
			cartao_id,
			data_contratacao,
			valor_apolice,
			descricao_condicoes,
			premio_apolice,
			status_seguro,
			data_acionamento,
			valor_fraude
		)
		VALUES (
			p_tipo_seguro,
			p_num_apolice,
			p_cartao_id,
			p_data_contratacao,
			p_valor_apolice,
			p_descricao_condicoes,
			p_premio_apolice,
			p_status_seguro,
			p_data_acionamento,
			p_valor_fraude
		)
		RETURNING id INTO v_id_seguro;

		RETURN QUERY SELECT v_id_seguro;

END;
$BODY$