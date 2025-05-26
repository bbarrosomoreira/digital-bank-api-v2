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
    FROM politica_taxa
    WHERE categoria = p_nova_categoria;

    -- 3. Verifica se cliente possui conta
	SELECT EXISTS (
		SELECT 1 FROM conta WHERE cliente_id = p_cliente_id
	) INTO v_possui_conta;

	IF v_possui_conta THEN
		-- Atualiza conta(s)
	    UPDATE conta
	    SET tarifa_manutencao_mensal_conta_corrente = v_politica.tarifa_manutencao_mensal_conta_corrente,
	        rendimento_percentual_mensal_conta_poupanca = v_politica.rendimento_percentual_mensal_conta_poupanca,
			tarifa_manutencao_conta_internacional = v_politica.tarifa_manutencao_conta_internacional
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
		    SET limite_cartao_credito = v_politica.limite_cartao_credito,
		        limite_diario_debito = v_politica.limite_diario_debito
		    WHERE conta_id IN (
		        SELECT id FROM conta WHERE cliente_id = p_cliente_id
		    );

		    -- 5. Atualiza seguro(s)
		    UPDATE seguro
		    SET tarifa_seguro_viagem = v_politica.tarifa_seguro_viagem,
		        tarifa_seguro_fraude = v_politica.tarifa_seguro_fraude,
				valor_apolice_fraude = v_politica.valor_apolice_fraude,
				valor_apolice_viagem = v_politica.valor_apolice_viagem
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
