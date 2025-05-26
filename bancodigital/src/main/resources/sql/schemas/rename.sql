DROP FUNCTION IF EXISTS public.public_abrir_conta_v1(character varying, numeric, character varying, bigint, date, character varying, numeric, numeric, numeric);

CREATE OR REPLACE FUNCTION public.add_con_v1(
	p_numero_conta character varying,
	p_saldo numeric,
	p_moeda character varying,
	p_cliente_id bigint,
	p_data_criacao date,
	p_tipo_conta character varying,
	p_tarifa_manutencao numeric,
	p_taxa_rendimento numeric,
	p_saldo_em_reais numeric)
    RETURNS TABLE(id_conta bigint)
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
    ROWS 1000

AS $BODY$

DECLARE
		v_id_conta BIGINT;
BEGIN
		INSERT INTO conta (
			numero_conta,
			saldo,
			moeda,
			cliente_id,
			data_criacao,
			tipo_conta,
			tarifa_manutencao,
			taxa_rendimento,
			saldo_em_reais
		)
		VALUES (
		    p_numero_conta,
		    p_saldo,
		    p_moeda,
		    p_cliente_id,
		    p_data_criacao,
		    p_tipo_conta,
		    p_tarifa_manutencao,
		    p_taxa_rendimento,
		    p_saldo_em_reais
		)
		RETURNING id INTO v_id_conta;

		RETURN QUERY SELECT v_id_conta;

END;
$BODY$;




DROP FUNCTION IF EXISTS public.public_atualizar_endereco_cliente_v1(bigint, character varying, character varying, integer, character varying, character varying, character varying, character, bigint);

CREATE OR REPLACE FUNCTION public.upd_end_cli_v1(
	p_id bigint,
	p_cep character varying,
	p_rua character varying,
	p_numero integer,
	p_complemento character varying,
	p_bairro character varying,
	p_cidade character varying,
	p_estado character,
	p_cliente_id bigint)
    RETURNS boolean
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
AS $BODY$

DECLARE
		v_linhas_afetadas BIGINT;
BEGIN
		UPDATE endereco_cliente
		SET
			cep = p_cep,
			rua = p_rua,
			numero = p_numero,
			complemento = p_complemento,
			bairro = p_bairro,
			cidade = p_cidade,
			estado = p_estado
		WHERE id = p_id;

		GET DIAGNOSTICS v_linhas_afetadas = ROW_COUNT;

		RETURN v_linhas_afetadas > 0;

END;
$BODY$;


DROP FUNCTION IF EXISTS public.public_atualizar_endereco_cliente_v2(bigint, character varying, character varying, integer, character varying, character varying, character varying, character, bigint);

CREATE OR REPLACE FUNCTION public.upd_end_cli_v2(
	p_id bigint,
	p_cep character varying,
	p_rua character varying,
	p_numero integer,
	p_complemento character varying,
	p_bairro character varying,
	p_cidade character varying,
	p_estado character,
	p_cliente_id bigint)
    RETURNS integer
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
AS $BODY$

BEGIN
		UPDATE endereco_cliente
		SET
			cep = p_cep,
			rua = p_rua,
			numero = p_numero,
			complemento = p_complemento,
			bairro = p_bairro,
			cidade = p_cidade,
			estado = p_estado
		WHERE id = p_id;

		RETURN FOUND::INT;

END;
$BODY$;


DROP FUNCTION IF EXISTS public.public_cadastrar_cliente_v1(character varying, character varying, character varying, date, bigint);

CREATE OR REPLACE FUNCTION public.add_cli_v1(
	p_nome character varying,
	p_cpf character varying,
	p_categoria character varying,
	p_data_nascimento date,
	p_usuario_id bigint)
    RETURNS TABLE(id_cliente bigint)
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
    ROWS 1000

AS $BODY$

DECLARE
		v_id_cliente BIGINT;
BEGIN
		INSERT INTO cliente (
			nome,
			cpf,
			categoria,
			data_nascimento,
			usuario_id
		)
		VALUES (
		    p_nome,
		    p_cpf,
		    p_categoria,
		    p_data_nascimento,
		    p_usuario_id
		)
		RETURNING id INTO v_id_cliente;

		RETURN QUERY SELECT v_id_cliente;

END;
$BODY$;


DROP FUNCTION IF EXISTS public.public_cadastrar_endereco_cliente_v1(character varying, character varying, integer, character varying, character varying, character varying, character, bigint);

CREATE OR REPLACE FUNCTION public.add_end_cli_v1(
	p_cep character varying,
	p_rua character varying,
	p_numero integer,
	p_complemento character varying,
	p_bairro character varying,
	p_cidade character varying,
	p_estado character,
	p_cliente_id bigint)
    RETURNS TABLE(id_endereco_cliente bigint)
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
    ROWS 1000

AS $BODY$

DECLARE
		v_id_endereco_cliente BIGINT;
BEGIN
		INSERT INTO endereco_cliente (
			cep,
			rua,
			numero,
			complemento,
			bairro,
			cidade,
			estado,
			cliente_id
		)
		VALUES (
		    p_cep,
		    p_rua,
		    p_numero,
		    p_complemento,
		    p_bairro,
		    p_cidade,
		    p_estado,
		    p_cliente_id
		)
		RETURNING id INTO v_id_endereco_cliente;

		RETURN QUERY SELECT v_id_endereco_cliente;

END;
$BODY$;



DROP FUNCTION IF EXISTS public.public_contratar_seguro_v1(character varying, character varying, bigint, date, numeric, character varying, numeric, character varying, date, numeric);

CREATE OR REPLACE FUNCTION public.add_seg_v1(
	p_tipo_seguro character varying,
	p_num_apolice character varying,
	p_cartao_id bigint,
	p_data_contratacao date,
	p_valor_apolice numeric,
	p_descricao_condicoes character varying,
	p_premio_apolice numeric,
	p_status_seguro character varying,
	p_data_acionamento date,
	p_valor_fraude numeric)
    RETURNS TABLE(id_seguro bigint)
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
    ROWS 1000

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
$BODY$;


DROP FUNCTION IF EXISTS public.public_criar_usuario_v1(character varying, character varying, character varying);

CREATE OR REPLACE FUNCTION public.add_usu_v1(
	p_email character varying,
	p_senha character varying,
	p_role character varying)
    RETURNS TABLE(id_usuario bigint)
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
    ROWS 1000

AS $BODY$

DECLARE
		v_id_usuario BIGINT;
BEGIN
		INSERT INTO usuario (
			email,
			senha,
			role
		)
		VALUES (
			p_email,
		    p_senha,
		    p_role
		)
		RETURNING id INTO v_id_usuario;

		RETURN QUERY SELECT v_id_usuario;

END;
$BODY$;


DROP FUNCTION IF EXISTS public.public_dlt_cli_v1(bigint);

CREATE OR REPLACE FUNCTION public.dlt_cli_v1(
	p_id bigint)
    RETURNS integer
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
AS $BODY$

BEGIN
		DELETE FROM cliente
		WHERE id = p_id;

		RETURN FOUND::INT;

END;
$BODY$;


DROP FUNCTION IF EXISTS public.public_dlt_con_v1(bigint);

CREATE OR REPLACE FUNCTION public.dlt_con_v1(
	p_id bigint)
    RETURNS integer
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
AS $BODY$

BEGIN
		DELETE FROM conta
		WHERE id = p_id;

		RETURN FOUND::INT;

END;
$BODY$;


DROP FUNCTION IF EXISTS public.public_dlt_crt_v1(bigint);

CREATE OR REPLACE FUNCTION public.dlt_crt_v1(
	p_id bigint)
    RETURNS integer
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
AS $BODY$

BEGIN
		DELETE FROM cartao
		WHERE id = p_id;

		RETURN FOUND::INT;

END;
$BODY$;


DROP FUNCTION IF EXISTS public.public_dlt_seg_v1(bigint);

CREATE OR REPLACE FUNCTION public.dlt_seg_v1(
	p_id bigint)
    RETURNS integer
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
AS $BODY$

BEGIN
		DELETE FROM seguro
		WHERE id = p_id;

		RETURN FOUND::INT;

END;
$BODY$;


DROP FUNCTION IF EXISTS public.public_dlt_usu_v1(bigint);

CREATE OR REPLACE FUNCTION public.dlt_usu_v1(
	p_id bigint)
    RETURNS integer
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
AS $BODY$

BEGIN
		DELETE FROM usuario
		WHERE id = p_id;

		RETURN FOUND::INT;

END;
$BODY$;


DROP FUNCTION IF EXISTS public.public_emitir_cartao_v1(character varying, character varying, bigint, character varying, character varying, date, date, numeric, numeric, numeric, numeric, numeric);

CREATE OR REPLACE FUNCTION public.add_crt_v1(
	p_tipo_cartao character varying,
	p_numero_cartao character varying,
	p_conta_id bigint,
	p_status character varying,
	p_senha character varying,
	p_data_emissao date,
	p_data_vencimento date,
	p_taxa_utilizacao numeric,
	p_limite numeric,
	p_limite_atual numeric,
	p_total_fatura numeric,
	p_total_fatura_paga numeric)
    RETURNS TABLE(id_cartao bigint)
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
    ROWS 1000

AS $BODY$

DECLARE
		v_id_cartao BIGINT;
BEGIN
		INSERT INTO cartao (
			tipo_cartao,
			numero_cartao,
			conta_id,
			status,
			senha,
			data_emissao,
			data_vencimento,
			taxa_utilizacao,
			limite,
			limite_atual,
			total_fatura,
			total_fatura_paga
		)
		VALUES (
			p_tipo_cartao,
		    p_numero_cartao,
		    p_conta_id,
		    p_status,
		    p_senha,
		    p_data_emissao,
		    p_data_vencimento,
		    p_taxa_utilizacao,
			p_limite,
			p_limite_atual,
		    p_total_fatura,
		    p_total_fatura_paga
		)
		RETURNING id INTO v_id_cartao;

		RETURN QUERY SELECT v_id_cartao;

END;
$BODY$;


DROP FUNCTION IF EXISTS public.public_ext_cli_cpf_v1(character varying);

CREATE OR REPLACE FUNCTION public.ext_cli_cpf_v1(
	p_cpf character varying)
    RETURNS boolean
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
AS $BODY$

DECLARE
	v_existe BOOLEAN;

BEGIN
		SELECT EXISTS (
			SELECT 1 FROM cliente WHERE cpf = p_cpf
		) INTO v_existe;

		RETURN v_existe;

END;
$BODY$;


DROP FUNCTION IF EXISTS public.public_ext_con_cli_id_v1(bigint);

CREATE OR REPLACE FUNCTION public.ext_con_cli_id_v1(
	p_cliente_id bigint)
    RETURNS boolean
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
AS $BODY$

DECLARE
	v_existe BOOLEAN;

BEGIN
		SELECT EXISTS (
			SELECT 1 FROM conta WHERE cliente_id = p_cliente_id
		) INTO v_existe;

		RETURN v_existe;

END;
$BODY$;


DROP FUNCTION IF EXISTS public.public_ext_crt_cli_id_v1(bigint);

CREATE OR REPLACE FUNCTION public.ext_crt_cli_id_v1(
	p_cliente_id bigint)
    RETURNS boolean
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
AS $BODY$

BEGIN
		RETURN EXISTS (
			SELECT 1
			FROM cartao c
			JOIN conta co ON c.conta_id = co.id
			WHERE co.cliente_id = p_cliente_id
		);

END;
$BODY$;



DROP FUNCTION IF EXISTS public.public_ext_crt_con_id_v1(bigint);

CREATE OR REPLACE FUNCTION public.ext_crt_con_id_v1(
	p_conta_id bigint)
    RETURNS boolean
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
AS $BODY$

DECLARE
	v_existe BOOLEAN;

BEGIN
		SELECT EXISTS (
			SELECT 1 FROM cartao WHERE conta_id = p_conta_id
		) INTO v_existe;

		RETURN v_existe;

END;
$BODY$;


DROP FUNCTION IF EXISTS public.public_ext_seg_cli_id_v1(bigint);

CREATE OR REPLACE FUNCTION public.ext_seg_cli_id_v1(
	p_cliente_id bigint)
    RETURNS boolean
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
AS $BODY$

BEGIN
		RETURN EXISTS (
			SELECT 1
			FROM seguro s
			JOIN cartao c ON s.cartao_id = c.id
			JOIN conta co ON c.conta_id = co.id
			WHERE co.cliente_id = p_cliente_id
		);

END;
$BODY$;


DROP FUNCTION IF EXISTS public.public_ext_seg_crt_id_v1(bigint);

CREATE OR REPLACE FUNCTION public.ext_seg_crt_id_v1(
	p_cartao_id bigint)
    RETURNS boolean
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
AS $BODY$

BEGIN
		RETURN EXISTS (
			SELECT 1
			FROM seguro
			WHERE cartao_id = p_cartao_id
		);

END;
$BODY$;


DROP FUNCTION IF EXISTS public.public_listar_endereco_cliente_por_cliente_id_v1(bigint);

CREATE OR REPLACE FUNCTION public.lst_end_cli_cli_id_v1(
	p_cliente_id bigint)
    RETURNS endereco_cliente
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
AS $BODY$

DECLARE
		v_endereco_encontrado endereco_cliente;

BEGIN
		SELECT * INTO v_endereco_encontrado
		FROM endereco_cliente
		WHERE cliente_id = p_cliente_id;

		RETURN v_endereco_encontrado;

END;
$BODY$;


DROP FUNCTION IF EXISTS public.public_listar_politica_de_taxas_por_categoria_v1(character varying);

CREATE OR REPLACE FUNCTION public.lst_pol_tax_cat_v1(
	p_categoria character varying)
    RETURNS politica_de_taxas
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
AS $BODY$

DECLARE
		v_politica_encontrada politica_de_taxas;

BEGIN
		SELECT * INTO v_politica_encontrada
		FROM politica_de_taxas
		WHERE categoria = p_categoria;

		RETURN v_politica_encontrada;

END;
$BODY$;


DROP FUNCTION IF EXISTS public.public_listar_politica_de_taxas_v1();

CREATE OR REPLACE FUNCTION public.lst_pol_tax_v1(
	)
    RETURNS SETOF politica_de_taxas
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
    ROWS 1000

AS $BODY$

BEGIN
		RETURN QUERY

		SELECT * FROM politica_de_taxas;

END;
$BODY$;


DROP FUNCTION IF EXISTS public.public_lst_all_cli_v1();

CREATE OR REPLACE FUNCTION public.lst_all_cli_v1(
	)
    RETURNS SETOF cliente
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
    ROWS 1000

AS $BODY$

BEGIN
		RETURN QUERY

		SELECT * FROM cliente;

END;
$BODY$;


DROP FUNCTION IF EXISTS public.public_lst_all_con_v1();

CREATE OR REPLACE FUNCTION public.lst_all_con_v1(
	)
    RETURNS SETOF conta
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
    ROWS 1000

AS $BODY$

BEGIN
		RETURN QUERY

		SELECT * FROM conta;

END;
$BODY$;


DROP FUNCTION IF EXISTS public.public_lst_all_crt_v1();

CREATE OR REPLACE FUNCTION public.lst_all_crt_v1(
	)
    RETURNS SETOF cartao
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
    ROWS 1000

AS $BODY$

BEGIN
		RETURN QUERY

		SELECT * FROM cartao;

END;
$BODY$;


DROP FUNCTION IF EXISTS public.public_lst_all_seg_v1();

CREATE OR REPLACE FUNCTION public.lst_all_seg_v1(
	)
    RETURNS SETOF seguro
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
    ROWS 1000

AS $BODY$

BEGIN
		RETURN QUERY

		SELECT * FROM seguro;

END;
$BODY$;


DROP FUNCTION IF EXISTS public.public_lst_cli_id_v1(bigint);

CREATE OR REPLACE FUNCTION public.lst_cli_id_v1(
	p_cliente_id bigint)
    RETURNS cliente
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
AS $BODY$

DECLARE
		v_cliente_encontrado cliente;

BEGIN
		SELECT * INTO v_cliente_encontrado
		FROM cliente
		WHERE id = p_cliente_id;

		RETURN v_cliente_encontrado;

END;
$BODY$;

DROP FUNCTION IF EXISTS public.public_lst_cli_usu_id_v1(bigint);

CREATE OR REPLACE FUNCTION public.lst_cli_usu_id_v1(
	p_usuario_id bigint)
    RETURNS cliente
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
AS $BODY$

DECLARE
		v_cliente_encontrado cliente;

BEGIN
		SELECT * INTO v_cliente_encontrado
		FROM cliente
		WHERE usuario_id = p_usuario_id;

		RETURN v_cliente_encontrado;

END;
$BODY$;

DROP FUNCTION IF EXISTS public.public_lst_con_cli_id_v1(bigint);

CREATE OR REPLACE FUNCTION public.lst_con_cli_id_v1(
	p_cliente_id bigint)
    RETURNS conta
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
AS $BODY$

DECLARE
		v_conta_encontrado conta;

BEGIN
		SELECT * INTO v_conta_encontrado
		FROM conta
		WHERE cliente_id = p_cliente_id;

		RETURN v_conta_encontrado;

END;
$BODY$;

DROP FUNCTION IF EXISTS public.public_lst_con_id_v1(bigint);

CREATE OR REPLACE FUNCTION public.lst_con_id_v1(
	p_conta_id bigint)
    RETURNS conta
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
AS $BODY$

DECLARE
		v_conta_encontrado conta;

BEGIN
		SELECT * INTO v_conta_encontrado
		FROM conta
		WHERE id = p_conta_id;

		RETURN v_conta_encontrado;

END;
$BODY$;

DROP FUNCTION IF EXISTS public.public_lst_con_usu_id_v1(bigint);

CREATE OR REPLACE FUNCTION public.lst_con_usu_id_v1(
	p_usuario_id bigint)
    RETURNS SETOF conta
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
    ROWS 1000

AS $BODY$

BEGIN
		RETURN QUERY
		SELECT c.*
		FROM conta c
		JOIN cliente cl ON c.cliente_id = cl.id
		WHERE cl.usuario_id = p_usuario_id;

END;
$BODY$;

DROP FUNCTION IF EXISTS public.public_lst_crt_cli_id_v1(bigint);

CREATE OR REPLACE FUNCTION public.lst_crt_cli_id_v1(
	p_cliente_id bigint)
    RETURNS SETOF cartao
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
    ROWS 1000

AS $BODY$

BEGIN
		RETURN QUERY
		SELECT c.*
		FROM cartao c
		JOIN conta conta ON c.conta_id = conta.id
		WHERE conta.cliente_id = p_cliente_id;

END;
$BODY$;

DROP FUNCTION IF EXISTS public.public_lst_crt_con_id_v1(bigint);

CREATE OR REPLACE FUNCTION public.lst_crt_con_id_v1(
	p_conta_id bigint)
    RETURNS SETOF cartao
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
    ROWS 1000

AS $BODY$

BEGIN
		RETURN QUERY
		SELECT *
		FROM cartao
		WHERE conta_id = p_conta_id;

END;
$BODY$;

DROP FUNCTION IF EXISTS public.public_lst_crt_id_v1(bigint);

CREATE OR REPLACE FUNCTION public.lst_crt_id_v1(
	p_cartao_id bigint)
    RETURNS SETOF cartao
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
    ROWS 1000

AS $BODY$

BEGIN
		RETURN QUERY
		SELECT *
		FROM cartao
		WHERE id = p_cartao_id;

END;
$BODY$;

DROP FUNCTION IF EXISTS public.public_lst_crt_usu_id_v1(bigint);

CREATE OR REPLACE FUNCTION public.lst_crt_usu_id_v1(
	p_usuario_id bigint)
    RETURNS SETOF cartao
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
    ROWS 1000

AS $BODY$

BEGIN
		RETURN QUERY
		SELECT c.*
		FROM cartao c
		JOIN conta conta ON c.conta_id = conta.id
		JOIN cliente cl ON conta.cliente_id = cl.id
		WHERE cl.usuario_id = p_usuario_id;

END;
$BODY$;

DROP FUNCTION IF EXISTS public.public_lst_seg_cli_id_v1(bigint);

CREATE OR REPLACE FUNCTION public.lst_seg_cli_id_v1(
	p_cliente_id bigint)
    RETURNS SETOF seguro
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
    ROWS 1000

AS $BODY$

BEGIN
		RETURN QUERY
		SELECT s.*
		FROM seguro s
		JOIN cartao c ON s.cartao_id = c.id
		JOIN conta co ON c.conta_id = co.id
		WHERE co.cliente_id = p_cliente_id;

END;
$BODY$;

DROP FUNCTION IF EXISTS public.public_lst_seg_crt_id_v1(bigint);

CREATE OR REPLACE FUNCTION public.lst_seg_crt_id_v1(
	p_cartao_id bigint)
    RETURNS SETOF seguro
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
    ROWS 1000

AS $BODY$

BEGIN
		RETURN QUERY
		SELECT *
		FROM seguro
		WHERE cartao_id = p_cartao_id;

END;
$BODY$;

DROP FUNCTION IF EXISTS public.public_lst_seg_id_v1(bigint);

CREATE OR REPLACE FUNCTION public.lst_seg_id_v1(
	p_seguro_id bigint)
    RETURNS SETOF seguro
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
    ROWS 1000

AS $BODY$

BEGIN
		RETURN QUERY
		SELECT *
		FROM seguro
		WHERE id = p_seguro_id;

END;
$BODY$;

DROP FUNCTION IF EXISTS public.public_lst_seg_usu_id_v1(bigint);

CREATE OR REPLACE FUNCTION public.lst_seg_usu_id_v1(
	p_usuario_id bigint)
    RETURNS SETOF seguro
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
    ROWS 1000

AS $BODY$

BEGIN
		RETURN QUERY
		SELECT s.*
		FROM seguro s
		JOIN cartao c ON s.cartao_id = c.id
		JOIN conta co ON c.conta_id = co.id
		JOIN cliente cl ON co.cliente_id = cl.id
		WHERE cl.usuario_id = p_usuario_id;

END;
$BODY$;

DROP FUNCTION IF EXISTS public.public_lst_usu_ema_v1(character varying);

CREATE OR REPLACE FUNCTION public.lst_usu_ema_v1(
	p_email character varying)
    RETURNS usuario
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
AS $BODY$

DECLARE
		v_usuario_encontrado usuario;

BEGIN
		SELECT * INTO v_usuario_encontrado
		FROM usuario
		WHERE email = p_email;

		RETURN v_usuario_encontrado;

END;
$BODY$;

DROP FUNCTION IF EXISTS public.public_lst_usu_id_v1(bigint);

CREATE OR REPLACE FUNCTION public.lst_usu_id_v1(
	p_usuario_id bigint)
    RETURNS usuario
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
AS $BODY$

DECLARE
		v_usuario_encontrado usuario;

BEGIN
		SELECT * INTO v_usuario_encontrado
		FROM usuario
		WHERE id = p_usuario_id;

		RETURN v_usuario_encontrado;

END;
$BODY$;

DROP FUNCTION IF EXISTS public.public_upd_cli_v1(bigint, character varying, character varying, character varying, date, bigint);

CREATE OR REPLACE FUNCTION public.upd_cli_v1(
	p_id bigint,
	p_nome character varying,
	p_cpf character varying,
	p_categoria character varying,
	p_data_nascimento date,
	p_usuario_id bigint)
    RETURNS integer
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
AS $BODY$

BEGIN
		UPDATE cliente
		SET
			nome = p_nome,
			cpf = p_cpf,
			categoria = p_categoria,
			data_nascimento = p_data_nascimento,
			usuario_id = p_usuario_id

		WHERE id = p_id;

		RETURN FOUND::INT;

END;
$BODY$;

DROP FUNCTION IF EXISTS public.public_upd_con_v1(bigint, character varying, numeric, character varying, bigint, date, character varying, numeric, numeric, numeric);

CREATE OR REPLACE FUNCTION public.upd_con_v1(
	p_id bigint,
	p_numero_conta character varying,
	p_saldo numeric,
	p_moeda character varying,
	p_cliente_id bigint,
	p_data_criacao date,
	p_tipo_conta character varying,
	p_tarifa_manutencao numeric,
	p_taxa_rendimento numeric,
	p_saldo_em_reais numeric)
    RETURNS integer
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
AS $BODY$

BEGIN
		UPDATE conta
		SET
			numero_conta = p_numero_conta,
			saldo = p_saldo,
			moeda = p_moeda,
			cliente_id = p_cliente_id,
			data_criacao = p_data_criacao,
			tipo_conta = p_tipo_conta,
			tarifa_manutencao = p_tarifa_manutencao,
			taxa_rendimento = p_taxa_rendimento,
			saldo_em_reais = p_saldo_em_reais

		WHERE id = p_id;

		RETURN FOUND::INT;

END;
$BODY$;

DROP FUNCTION IF EXISTS public.public_upd_crt_v1(bigint, character varying, character varying, bigint, character varying, character varying, date, date, numeric, numeric, numeric, numeric, numeric);

CREATE OR REPLACE FUNCTION public.upd_crt_v1(
	p_id bigint,
	p_tipo_cartao character varying,
	p_numero_cartao character varying,
	p_conta_id bigint,
	p_status character varying,
	p_senha character varying,
	p_data_emissao date,
	p_data_vencimento date,
	p_taxa_utilizacao numeric,
	p_limite numeric,
	p_limite_atual numeric,
	p_total_fatura numeric,
	p_total_fatura_paga numeric)
    RETURNS integer
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
AS $BODY$

BEGIN
		UPDATE cartao
		SET
			tipo_cartao = p_tipo_cartao,
			numero_cartao = p_numero_cartao,
			conta_id = p_conta_id,
			status = p_status,
			senha = p_senha,
			data_emissao = p_data_emissao,
			data_vencimento = p_data_vencimento,
			taxa_utilizacao = p_taxa_utilizacao,
			limite = p_limite,
			limite_atual = p_limite_atual,
			total_fatura = p_total_fatura,
			total_fatura_paga = p_total_fatura_paga

		WHERE id = p_id;

		RETURN FOUND::INT;

END;
$BODY$;

DROP FUNCTION IF EXISTS public.public_upd_seg_v1(bigint, character varying, character varying, bigint, date, numeric, character varying, numeric, character varying, date, numeric);

CREATE OR REPLACE FUNCTION public.upd_seg_v1(
	p_id bigint,
	p_tipo_seguro character varying,
	p_num_apolice character varying,
	p_cartao_id bigint,
	p_data_contratacao date,
	p_valor_apolice numeric,
	p_descricao_condicoes character varying,
	p_premio_apolice numeric,
	p_status_seguro character varying,
	p_data_acionamento date,
	p_valor_fraude numeric)
    RETURNS integer
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
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
$BODY$;

DROP FUNCTION IF EXISTS public.public_upd_usu_v1(bigint, character varying, character varying, character varying);

CREATE OR REPLACE FUNCTION public.upd_usu_v1(
	p_id bigint,
	p_email character varying,
	p_senha character varying,
	p_role character varying)
    RETURNS integer
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
AS $BODY$

BEGIN
		UPDATE usuario
		SET
			email = p_email,
			senha = p_senha,
			role = p_role

		WHERE id = p_id;

		RETURN FOUND::INT;

END;
$BODY$;
