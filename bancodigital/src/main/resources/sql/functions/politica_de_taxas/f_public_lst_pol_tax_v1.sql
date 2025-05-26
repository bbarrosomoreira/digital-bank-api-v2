CREATE OR REPLACE FUNCTION public_listar_politica_de_taxas_v1()

RETURNS SETOF politica_de_taxas

LANGUAGE 'plpgsql'

AS $BODY$

BEGIN
		RETURN QUERY

		SELECT * FROM politica_de_taxas;

END;
$BODY$