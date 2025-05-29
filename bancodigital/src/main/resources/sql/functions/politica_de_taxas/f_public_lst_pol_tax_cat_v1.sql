CREATE OR REPLACE FUNCTION public_listar_politica_de_taxas_por_categoria_v1(
		p_categoria VARCHAR(20)
)

RETURNS politica_de_taxas

LANGUAGE 'plpgsql'

AS $BODY$

DECLARE
		v_politica_encontrada politica_de_taxas;

BEGIN
		SELECT * INTO v_politica_encontrada
		FROM politica_de_taxas
		WHERE categoria = p_categoria;

		RETURN v_politica_encontrada;

END;
$BODY$