package br.com.cdb.bancodigital.utils;

public class SqlQueries {

    private SqlQueries() {
        throw new IllegalStateException("Utility class");
    }

    // UsuarioDAO
    public static final String SQL_CREATE_USUARIO = "SELECT * FROM public_criar_usuario_v1(?, ?, ?)";
    public static final String SQL_READ_USUARIO_BY_EMAIL = "SELECT * FROM public_lst_usu_ema_v1(?)";
    public static final String SQL_READ_USUARIO_BY_ID = "SELECT * FROM public_lst_usu_id_v1(?)";
    public static final String SQL_UPDATE_USUARIO = "SELECT public_upd_usu_v1(?, ?, ?, ?)";
    public static final String SQL_DELETE_USUARIO = "SELECT public_dlt_usu_v1(?)";

    // ClienteDAO
    public static final String SQL_CREATE_CLIENTE = "SELECT * FROM public_cadastrar_cliente_v1(?, ?, ?, ?, ?)";
    public static final String SQL_READ_ALL_CLIENTES = "SELECT * FROM public_lst_all_cli_v1()";
    public static final String SQL_READ_CLIENTE_BY_ID = "SELECT * FROM public_lst_cli_id_v1(?)";
    public static final String SQL_READ_CLIENTE_BY_USUARIO = "SELECT * FROM public_lst_cli_usu_id_v1(?)";
    public static final String SQL_EXIST_CLIENTE = "SELECT public_ext_cli_cpf_v1(?)";
    public static final String SQL_UPDATE_CLIENTE = "SELECT public_upd_cli_v1(?, ?, ?, ?, ?, ?)";
    public static final String SQL_DELETE_CLIENTE = "SELECT public_dlt_cli_v1(?)";

    // EnderecoClienteDAO
    public static final String SQL_CREATE_ENDERECO_CLIENTE = "SELECT * FROM public_cadastrar_endereco_cliente_v1(?, ?, ?, ?, ?, ?, ?, ?)";
    public static final String SQL_READ_ENDERECO_CLIENTE_BY_CLIENTE = "SELECT * FROM public_listar_endereco_cliente_por_cliente_id_v1(?)";
    public static final String SQL_UPDATE_ENDERECO_CLIENTE = "SELECT public_atualizar_endereco_cliente_v1(?, ?, ?, ?, ?, ?, ?, ?, ?)";

    // ContaDAO
    public static final String SQL_CREATE_CONTA = "SELECT * FROM public_abrir_conta_v1(?, ?, ?, ?, ?, ?, ?, ?, ?)";
    public static final String SQL_READ_ALL_CONTAS = "SELECT * FROM public_lst_all_con_v1()";
    public static final String SQL_READ_CONTA_BY_ID = "SELECT * FROM public_lst_con_id_v1(?)";
    public static final String SQL_READ_CONTA_BY_CLIENTE = "SELECT * FROM public_lst_con_cli_id_v1(?)";
    public static final String SQL_READ_CONTA_BY_CLIENTE_USUARIO = "SELECT * FROM public_lst_con_usu_id_v1(?)";
    public static final String SQL_EXIST_CONTA = "SELECT public_ext_con_cli_id_v1(?)";
    public static final String SQL_UPDATE_CONTA = "SELECT public_upd_con_v1(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    public static final String SQL_DELETE_CONTA = "SELECT public_dlt_con_v1(?)";

    // CartaoDAO
    public static final String SQL_CREATE_CARTAO = "SELECT * FROM public_emitir_cartao_v1(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    public static final String SQL_READ_ALL_CARTOES = "SELECT * FROM public_lst_all_crt_v1()";
    public static final String SQL_READ_CARTAO_BY_ID = "SELECT * FROM public_lst_crt_id_v1(?)";
    public static final String SQL_READ_CARTAO_BY_CONTA = "SELECT * FROM public_lst_crt_con_id_v1(?)";
    public static final String SQL_READ_CARTAO_BY_CONTA_CLIENTE_ID = "SELECT * FROM public_lst_crt_cli_id_v1(?)";
    public static final String SQL_READ_CARTAO_BY_CONTA_CLIENTE_USUARIO = "SELECT * FROM public_lst_crt_usu_id_v1(?)";
    public static final String SQL_EXIST_CARTAO_CONTA = "SELECT public_ext_crt_con_id_v1(?)";
    public static final String SQL_EXIST_CARTAO_CLIENTE = "SELECT public_ext_crt_cli_id_v1(?)";
    public static final String SQL_UPDATE_CARTAO = "SELECT public_upd_crt_v1(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    public static final String SQL_DELETE_CARTAO = "SELECT public_dlt_crt_v1(?)";

    // SeguroDAO
    public static final String SQL_CREATE_SEGURO = "SELECT * FROM public_contratar_seguro_v1(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    public static final String SQL_READ_ALL_SEGUROS = "SELECT * FROM public_lst_all_seg_v1()";
    public static final String SQL_READ_SEGURO_BY_ID = "SELECT * FROM seguro WHERE id = ?";
    public static final String SQL_READ_SEGURO_BY_CARTAO = "SELECT * FROM seguro WHERE cartao_id = ?";
    public static final String SQL_READ_SEGURO_BY_CARTAO_CLIENTE_ID = "SELECT s.* FROM seguro s JOIN cartao c ON s.cartao_id = c.id JOIN conta ct ON c.conta_id = ct.id WHERE ct.cliente_id = ?";
    public static final String SQL_READ_SEGURO_BY_TIPO_CLIENTE = "SELECT s.* FROM seguro s JOIN cartao c ON s.cartao_id = c.id JOIN conta ct ON c.conta_id = ct.id WHERE ct.cliente_id = ? AND s.tipo_seguro = ?";
    public static final String SQL_COUNT_SEGURO_CARTAO = "SELECT COUNT(*) FROM seguro WHERE cartao_id = ?";
    public static final String SQL_COUNT_SEGURO_CLIENTE = "SELECT COUNT(*) FROM seguro s JOIN cartao c ON s.cartao_id = c.id JOIN conta ct ON c.conta_id = ct.id WHERE ct.cliente_id = ?";
    public static final String SQL_READ_SEGURO_BY_CARTAO_CLIENTE_USUARIO = "SELECT s.* FROM seguro s JOIN cartao c ON s.cartao_id = c.id JOIN conta ct ON c.conta_id = ct.id JOIN cliente cl ON ct.cliente_id = cl.id WHERE cl.usuario_id = ?";
    public static final String SQL_UPDATE_SEGURO = "UPDATE seguro SET tipo_seguro = ?, num_apolice = ?, cartao_id = ?, data_contratacao = ?, valor_apolice = ?, descricao_condicoes = ?, premio_apolice = ?, status_seguro = ?, data_acionamento = ?, valor_fraude = ? WHERE id = ?";
    public static final String SQL_DELETE_SEGURO = "DELETE FROM seguro WHERE id = ?";

    // PoliticaDeTaxasDAO
    public static final String SQL_READ_ALL_POLITICA_DE_TAXAS = "SELECT * FROM public_listar_politica_de_taxas_v1()";
    public static final String SQL_READ_POLITICA_DE_TAXAS_BY_CATEGORIA = "SELECT * FROM public_listar_politica_de_taxas_por_categoria_v1(?)";

}
