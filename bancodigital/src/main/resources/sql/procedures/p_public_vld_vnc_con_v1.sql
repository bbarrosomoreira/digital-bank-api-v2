-- PROCEDURE: public.vld_vnc_con_v1(bigint)

-- DROP PROCEDURE IF EXISTS public.vld_vnc_con_v1(bigint);

CREATE OR REPLACE PROCEDURE public.vld_vnc_con_v1(
	IN p_conta_id bigint)
LANGUAGE 'plpgsql'
AS $BODY$
DECLARE
    v_possui_cartao BOOLEAN;
BEGIN
	-- Verifica se há cartões vinculados
    SELECT EXISTS (
        SELECT 1 FROM cartao
        WHERE conta_id = p_conta_id
    ) INTO v_possui_cartao;

    -- Se houver qualquer vínculo, lança exceção
    IF v_possui_cartao THEN
        RAISE EXCEPTION 'Conta % possui vínculos com cartão(ões).', p_conta_id;
    END IF;

    -- Pode usar RAISE NOTICE para debug
    RAISE NOTICE 'Conta % não possui vínculos.', p_conta_id;

END;
$BODY$;
ALTER PROCEDURE public.vld_vnc_con_v1(bigint)
    OWNER TO postgres;
