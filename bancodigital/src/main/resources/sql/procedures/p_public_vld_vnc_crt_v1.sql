-- PROCEDURE: public.vld_vnc_crt_v1(bigint)

-- DROP PROCEDURE IF EXISTS public.vld_vnc_crt_v1(bigint);

CREATE OR REPLACE PROCEDURE public.vld_vnc_crt_v1(
	IN p_cartao_id bigint)
LANGUAGE 'plpgsql'
AS $BODY$
DECLARE
    v_possui_seguro BOOLEAN;
BEGIN
	-- Verifica se há seguros vinculados
    SELECT EXISTS (
        SELECT 1 FROM seguro
        WHERE cartao_id = p_cartao_id
    ) INTO v_possui_seguro;

    -- Se houver qualquer vínculo, lança exceção
    IF v_possui_seguro THEN
        RAISE EXCEPTION 'Cartão % possui vínculos com seguro(s).', p_cartao_id;
    END IF;

    -- Pode usar RAISE NOTICE para debug
    RAISE NOTICE 'Cartão % não possui vínculos.', p_cartao_id;

END;
$BODY$;
ALTER PROCEDURE public.vld_vnc_crt_v1(bigint)
    OWNER TO postgres;
