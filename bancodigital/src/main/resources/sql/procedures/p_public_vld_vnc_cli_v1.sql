-- PROCEDURE: public.vld_vnc_cli_v1(bigint)

-- DROP PROCEDURE IF EXISTS public.vld_vnc_cli_v1(bigint);

CREATE OR REPLACE PROCEDURE public.vld_vnc_cli_v1(
	IN p_cliente_id bigint)
LANGUAGE 'plpgsql'
AS $BODY$
DECLARE
	v_possui_conta BOOLEAN;
    v_possui_cartao BOOLEAN;
	v_possuiu_seguro BOOLEAN;
BEGIN
    -- Verifica se há contas vinculadas
	SELECT EXISTS (
		SELECT 1 FROM conta WHERE cliente_id = p_cliente_id
	) INTO v_possui_conta;

	-- Verifica se há cartões vinculados
    SELECT EXISTS (
        SELECT 1 FROM cartao c
        JOIN conta ct ON c.conta_id = ct.id
        WHERE ct.cliente_id = p_cliente_id
    ) INTO v_possui_cartao;

    -- Verifica se há seguros vinculados
    SELECT EXISTS (
        SELECT 1 FROM seguro s
        JOIN cartao c ON s.cartao_id = c.id
        JOIN conta ct ON c.conta_id = ct.id
        WHERE ct.cliente_id = p_cliente_id
    ) INTO v_possuiu_seguro;

    -- Se houver qualquer vínculo, lança exceção
    IF v_possui_conta OR v_possui_cartao OR v_possuiu_seguro THEN
        RAISE EXCEPTION 'Cliente % possui vínculos - conta(s), cartão(ões), seguro(s).', p_cliente_id;
    END IF;

    -- Pode usar RAISE NOTICE para debug
    RAISE NOTICE 'Cliente % não possui vínculos.', p_cliente_id;

END;
$BODY$;
ALTER PROCEDURE public.vld_vnc_cli_v1(bigint)
    OWNER TO postgres;
