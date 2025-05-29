package br.com.cdb.bancodigital.utils;

public class SqlQueries {

    private SqlQueries() {
        throw new IllegalStateException("Utility class");
    }

    // UsuarioDAO
    public static final String SQL_CREATE_USUARIO = "SELECT * FROM public.add_usu_v1(?, ?, ?)";
    public static final String SQL_EXIST_USUARIO_BY_EMAIL = "SELECT public.ext_usu_ema_v1(?)";
    public static final String SQL_READ_USUARIO_BY_EMAIL = "SELECT * FROM public.lst_usu_ema_v1(?)";
    public static final String SQL_READ_USUARIO_BY_ID = "SELECT * FROM public.lst_usu_id_v1(?)";
    public static final String SQL_UPDATE_USUARIO = "SELECT public.upd_usu_v1(?, ?, ?, ?)";
    public static final String SQL_DELETE_USUARIO = "SELECT public.dlt_usu_v1(?)";

    // ClienteDAO
    public static final String SQL_CREATE_CLIENTE = "SELECT * FROM public.add_cli_v1(?, ?, ?, ?, ?)";
    public static final String SQL_READ_ALL_CLIENTES = "SELECT * FROM public.lst_all_cli_v1()";
    public static final String SQL_READ_CLIENTE_BY_ID = "SELECT * FROM public.lst_cli_id_v1(?)";
    public static final String SQL_READ_CLIENTE_BY_USUARIO = "SELECT * FROM public.lst_cli_usu_id_v1(?)";
    public static final String SQL_EXIST_CLIENTE = "SELECT public.ext_cli_cpf_v1(?)";
    public static final String SQL_UPDATE_CLIENTE = "SELECT public.upd_cli_v1(?, ?, ?, ?, ?, ?)";
    public static final String SQL_UPDATE_CATEGORIA_CLIENTE = "CALL public.upd_cnd_cli_cat_v1(?, ?)";
    public static final String SQL_DELETE_CLIENTE = "SELECT public.dlt_cli_v1(?)";
    public static final String SQL_VALIDAR_VINCULOS_CLIENTE = "CALL public.vld_vnc_cli_v1(?)";

    // endClienteDAO
    public static final String SQL_CREATE_ENDERECO_CLIENTE = "SELECT * FROM public.add_end_cli_v1(?, ?, ?, ?, ?, ?, ?, ?)";
    public static final String SQL_READ_ENDERECO_CLIENTE_BY_CLIENTE = "SELECT * FROM public.lst_end_cli_cli_id_v1(?)";
    public static final String SQL_UPDATE_ENDERECO_CLIENTE = "SELECT public.upd_end_cli_v1(?, ?, ?, ?, ?, ?, ?, ?, ?)";

    // ContaDAO
    public static final String SQL_CREATE_CONTA = "SELECT * FROM public.add_con_v1(?, ?, ?, ?, ?, ?, ?, ?, ?)";
    public static final String SQL_READ_ALL_CONTAS = "SELECT * FROM public.lst_all_con_v1()";
    public static final String SQL_READ_CONTA_BY_ID = "SELECT * FROM public.lst_con_id_v1(?)";
    public static final String SQL_READ_CONTA_BY_CLIENTE = "SELECT * FROM public.lst_con_cli_id_v1(?)";
    public static final String SQL_READ_CONTA_BY_CLIENTE_USUARIO = "SELECT * FROM public.lst_con_usu_id_v1(?)";
    public static final String SQL_UPDATE_CONTA = "SELECT public.upd_con_v1(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    public static final String SQL_DELETE_CONTA = "SELECT public.dlt_con_v1(?)";
    public static final String SQL_VALIDAR_VINCULOS_CONTA = "CALL public.vld_vnc_con_v1(?)";

    // CartaoDAO
    public static final String SQL_CREATE_CARTAO = "SELECT * FROM public.add_crt_v1(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    public static final String SQL_READ_ALL_CARTOES = "SELECT * FROM public.lst_all_crt_v1()";
    public static final String SQL_READ_CARTAO_BY_ID = "SELECT * FROM public.lst_crt_id_v1(?)";
    public static final String SQL_READ_CARTAO_BY_CONTA = "SELECT * FROM public.lst_crt_con_id_v1(?)";
    public static final String SQL_READ_CARTAO_BY_CONTA_CLIENTE_ID = "SELECT * FROM public.lst_crt_cli_id_v1(?)";
    public static final String SQL_READ_CARTAO_BY_CONTA_CLIENTE_USUARIO = "SELECT * FROM public.lst_crt_usu_id_v1(?)";
    public static final String SQL_EXIST_CARTAO_CONTA = "SELECT public.ext_crt_con_id_v1(?)";
    public static final String SQL_EXIST_CARTAO_CLIENTE = "SELECT public.ext_crt_cli_id_v1(?)";
    public static final String SQL_UPDATE_CARTAO = "SELECT public.upd_crt_v1(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    public static final String SQL_DELETE_CARTAO = "SELECT public.dlt_crt_v1(?)";
    public static final String SQL_VALIDAR_VINCULOS_CARTAO = "CALL public.vld_vnc_crt_v1(?)";

    // SeguroDAO
    public static final String SQL_CREATE_SEGURO = "SELECT * FROM public.add_seg_v1(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    public static final String SQL_READ_ALL_SEGUROS = "SELECT * FROM public.lst_all_seg_v1()";
    public static final String SQL_READ_SEGURO_BY_ID = "SELECT * FROM public.lst_seg_id_v1(?)";
    public static final String SQL_READ_SEGURO_BY_CARTAO = "SELECT * FROM public.lst_seg_crt_id_v1(?)";
    public static final String SQL_READ_SEGURO_BY_CARTAO_CLIENTE_ID = "SELECT * FROM public.lst_seg_cli_id_v1(?)";
    public static final String SQL_READ_SEGURO_BY_CARTAO_CLIENTE_USUARIO = "SELECT * FROM public.lst_seg_usu_id_v1(?)";
    public static final String SQL_EXIST_SEGURO_CARTAO = "SELECT public.ext_seg_crt_id_v1(?)";
    public static final String SQL_EXIST_SEGURO_CLIENTE = "SELECT public.ext_seg_cli_id_v1(?)";
    public static final String SQL_UPDATE_SEGURO = "SELECT public.upd_seg_v1(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    public static final String SQL_DELETE_SEGURO = "SELECT public.dlt_seg_v1(?)";

    // PoliticaDeTaxasDAO
    public static final String SQL_READ_ALL_POLITICA_DE_TAXAS = "SELECT * FROM public.lst_pol_tax_v1()";
    public static final String SQL_READ_POLITICA_DE_TAXAS_BY_CATEGORIA = "SELECT * FROM public.lst_pol_tax_cat_v1(?)";

}
