-- FUNCTION: public.dlt_cli_v1(bigint)

-- DROP FUNCTION IF EXISTS public.dlt_cli_v1(bigint);

CREATE OR REPLACE FUNCTION public.dlt_cli_v1(
	p_id bigint)
    RETURNS integer
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
AS $BODY$
BEGIN
	-- Exclui os vínculos com endereços
	DELETE FROM endereco_cliente
	WHERE cliente_id = p_id;

	-- Depois exclui o cliente
	DELETE FROM cliente
	WHERE id = p_id;

	-- Retorna 1 se o cliente foi excluído, 0 caso contrário
	RETURN FOUND::INT;
END;
$BODY$;

ALTER FUNCTION public.dlt_cli_v1(bigint)
    OWNER TO postgres;
