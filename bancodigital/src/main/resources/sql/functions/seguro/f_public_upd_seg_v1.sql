CREATE OR REPLACE FUNCTION public_upd_seg_v1(
	p_id BIGINT,
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
RETURNS INT

LANGUAGE 'plpgsql'

AS $BODY$

BEGIN
		UPDATE seguro
		SET
			tipo_seguro = p_tipo_seguro,
			num_apolice = p_num_apolice,
			cartao_id = p_cartao_id,
			data_contratacao = p_data_contratacao,
			valor_apolice = p_valor_apolice,
			descricao_condicoes = p_descricao_condicoes,
			premio_apolice = p_premio_apolice,
			status_seguro = p_status_seguro,
			data_acionamento = p_data_acionamento,
			valor_fraude = p_valor_fraude

		WHERE id = p_id;

		RETURN FOUND::INT;

END;
$BODY$
