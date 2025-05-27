-- PROCEDURE: public.upd_cnd_cli_cat_v1(bigint, character varying)

-- DROP PROCEDURE IF EXISTS public.upd_cnd_cli_cat_v1(bigint, character varying);

CREATE OR REPLACE PROCEDURE public.upd_cnd_cli_cat_v1(
	IN p_cliente_id bigint,
	IN p_nova_categoria character varying)
LANGUAGE 'plpgsql'
AS $BODY$
DECLARE
    v_politica RECORD;
	v_possui_conta BOOLEAN;
    v_possui_cartao BOOLEAN;
BEGIN
    -- 1. Atualiza a categoria do cliente
    UPDATE cliente
    SET categoria = p_nova_categoria
    WHERE id = p_cliente_id;

    -- 2. Busca política da nova categoria
    SELECT * INTO v_politica
    FROM politica_de_taxas
    WHERE categoria = p_nova_categoria;

    -- 3. Verifica se cliente possui conta
	SELECT EXISTS (
		SELECT 1 FROM conta WHERE cliente_id = p_cliente_id
	) INTO v_possui_conta;

	IF v_possui_conta THEN
		-- Atualiza conta(s)
	    UPDATE conta
	    SET
			tarifa_manutencao = CASE
				WHEN tipo_conta = 'CORRENTE' THEN v_politica.tarifa_manutencao_mensal_conta_corrente
				WHEN tipo_conta = 'INTERNACIONAL' THEN v_politica.tarifa_manutencao_conta_internacional
				ELSE tarifa_manutencao
			END,
			taxa_rendimento = CASE
				WHEN tipo_conta = 'POUPANCA' THEN v_politica.rendimento_percentual_mensal_conta_poupanca
				ELSE taxa_rendimento
			END
	    WHERE cliente_id = p_cliente_id;

	    -- 4. Verifica se cliente possui cartão
		SELECT EXISTS (
			SELECT 1 FROM cartao
			WHERE conta_id IN (
				SELECT id FROM conta WHERE cliente_id = p_cliente_id
			)
		) INTO v_possui_cartao;

		IF v_possui_cartao THEN
			-- Atualiza cartão(ões)
		    UPDATE cartao
		    SET
				limite = CASE
					WHEN tipo_cartao = 'CREDITO' THEN v_politica.limite_cartao_credito
					WHEN tipo_cartao = 'DEBITO' THEN v_politica.limite_diario_debito
					ELSE limite
				END
		    WHERE conta_id IN (
		        SELECT id FROM conta WHERE cliente_id = p_cliente_id
		    );

		    -- 5. Atualiza seguro(s)
		    UPDATE seguro
		    SET
				premio_apolice = CASE
					WHEN tipo_seguro = 'VIAGEM' THEN v_politica.tarifa_seguro_viagem
					WHEN tipo_seguro = 'FRAUDE' THEN v_politica.tarifa_seguro_fraude
					ELSE premio_apolice
				END,
				valor_apolice = CASE
					WHEN tipo_seguro = 'FRAUDE' THEN v_politica.valor_apolice_fraude
					WHEN tipo_seguro = 'VIAGEM' THEN v_politica.valor_apolice_viagem
					ELSE valor_apolice
				END
		    WHERE cartao_id IN (
		        SELECT id FROM cartao
		        WHERE conta_id IN (
		            SELECT id FROM conta WHERE cliente_id = p_cliente_id
		        )
		    );
		ELSE
			RAISE NOTICE 'Cliente % não possui cartão. Nenhuma atualização em cartão/seguro.', p_cliente_id;
		END IF;
	ELSE
		RAISE NOTICE 'Cliente % não possui conta. Nenhuma atualização em conta/cartão/seguro.', p_cliente_id;
	END IF;

    RAISE NOTICE 'Atualização concluída para cliente %', p_cliente_id;

	EXCEPTION
		WHEN NO_DATA_FOUND THEN
     	RAISE EXCEPTION 'Categoria % não possui política cadastrada', p_nova_categoria;

   		WHEN OTHERS THEN
     	RAISE EXCEPTION 'Erro inesperado: %', SQLERRM;

END;
$BODY$;
ALTER PROCEDURE public.upd_cnd_cli_cat_v1(bigint, character varying)
    OWNER TO postgres;
