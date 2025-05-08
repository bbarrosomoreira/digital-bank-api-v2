package br.com.cdb.bancodigital.utils;

public class SqlQueries {

    private SqlQueries() {
        throw new IllegalStateException("Utility class");
    }

    // UsuarioDAO
    public static final String SQL_CREATE_USUARIO = "INSERT INTO usuario (email, senha, role) VALUES (?, ?, ?) RETURNING id";
    public static final String SQL_READ_USUARIO_BY_EMAIL = "SELECT * FROM usuario WHERE email = ?";
    public static final String SQL_READ_USUARIO_BY_ID = "SELECT * FROM usuario WHERE id = ?";
    public static final String SQL_UPDATE_USUARIO = "UPDATE usuario SET email = ?, senha = ?, role = ? WHERE id = ?";
    public static final String SQL_DELETE_USUARIO = "DELETE FROM usuario WHERE id = ?";

    // ClienteDAO
    public static final String SQL_CREATE_CLIENTE = "INSERT INTO cliente (nome, cpf, categoria, data_nascimento, usuario_id) VALUES (?, ?, ?, ?, ?) RETURNING id";
    public static final String SQL_READ_ALL_CLIENTES = "SELECT * FROM cliente";
    public static final String SQL_READ_CLIENTE_BY_ID = "SELECT * FROM cliente WHERE id = ?";
    public static final String SQL_READ_CLIENTE_BY_USUARIO = "SELECT * FROM cliente WHERE usuario_id = ?";
    public static final String SQL_COUNT_CLIENTE = "SELECT COUNT(*) FROM cliente WHERE cpf = ?";
    public static final String SQL_UPDATE_CLIENTE = "UPDATE cliente SET nome = ?, cpf = ?, categoria = ?, data_nascimento = ? WHERE id = ?";
    public static final String SQL_DELETE_CLIENTE = "DELETE FROM cliente WHERE id = ?";

    // EnderecoClienteDAO
    public static final String SQL_CREATE_ENDERECO_CLIENTE = "INSERT INTO endereco_cliente (cep, rua, numero, complemento, bairro, cidade, estado, cliente_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?) RETURNING id";
    public static final String SQL_READ_ENDERECO_CLIENTE_BY_CLIENTE = "SELECT * FROM endereco_cliente WHERE cliente_id = ?";
    public static final String SQL_UPDATE_ENDERECO_CLIENTE = "UPDATE endereco_cliente SET cep = ?, rua = ?, numero = ?, complemento = ?, bairro = ?, cidade = ?, estado = ? WHERE id = ?";

    // ContaDAO
    public static final String SQL_CREATE_CONTA = "INSERT INTO conta (numero_conta, saldo, moeda, cliente_id, data_criacao, tipo_conta, tarifa_manutencao, taxa_rendimento, saldo_em_reais) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING id";
    public static final String SQL_READ_ALL_CONTAS = "SELECT * FROM conta";
    public static final String SQL_READ_CONTA_BY_ID = "SELECT * FROM conta WHERE id = ?";
    public static final String SQL_COUNT_CONTA = "SELECT COUNT(*) FROM conta WHERE cliente_id = ?";
    public static final String SQL_READ_CONTA_BY_CLIENTE = "SELECT * FROM conta WHERE cliente_id = ?";
    public static final String SQL_READ_CONTA_BY_CLIENTE_USUARIO = "SELECT c.* FROM conta c JOIN cliente cl ON c.cliente_id = cl.id WHERE cl.usuario_id = ?";
    public static final String SQL_READ_CONTA_BY_TIPO_CLIENTE = "SELECT * FROM conta WHERE cliente_id = ? AND tipo_conta = ?";
    public static final String SQL_UPDATE_CONTA = "UPDATE conta SET numero_conta = ?, saldo = ?, moeda = ?, cliente_id = ?, data_criacao = ?, tipo_conta = ?, tarifa_manutencao = ?, taxa_rendimento = ?, saldo_em_reais = ? WHERE id = ?";
    public static final String SQL_DELETE_CONTA = "DELETE FROM conta WHERE id = ?";

    // CartaoDAO
    public static final String SQL_CREATE_CARTAO = "INSERT INTO cartao (tipo_cartao, numero_cartao, conta_id, status, " +
            "senha, data_emissao, data_vencimento, taxa_utilizacao, limite, limite_atual, total_fatura, total_fatura_paga) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING id";
    public static final String SQL_READ_ALL_CARTOES = "SELECT * FROM cartao";
    public static final String SQL_READ_CARTAO_BY_ID = "SELECT * FROM cartao WHERE id = ?";
    public static final String SQL_READ_CARTAO_BY_CONTA = "SELECT * FROM cartao WHERE conta_id = ?";
    public static final String SQL_COUNT_CARTAO_CONTA = "SELECT COUNT(*) FROM cartao WHERE conta_id = ?";
    public static final String SQL_COUNT_CARTAO_CLIENTE = "SELECT COUNT(*) FROM cartao cartao JOIN conta conta ON cartao.conta_id = conta.id WHERE conta.cliente_id = ?";
    public static final String SQL_READ_CARTAO_BY_CONTA_CLIENTE_USUARIO = "SELECT c.* FROM cartao c JOIN conta conta ON c.conta_id = conta.id JOIN cliente cliente ON conta.cliente_id = cliente.id WHERE cliente.usuario_id = ?";
    public static final String SQL_READ_CARTAO_BY_CONTA_CLIENTE_ID = "SELECT c.* FROM cartao c JOIN conta conta ON c.conta_id = conta.id WHERE conta.cliente_id = ?";
    public static final String SQL_READ_CARTAO_BY_TIPO_CLIENTE = "SELECT c.* FROM cartao c JOIN conta conta ON c.conta_id = conta.id WHERE conta.cliente_id = ? AND c.tipo_cartao = ?";
    public static final String SQL_UPDATE_CARTAO = "UPDATE cartao SET tipo_cartao = ?, numero_cartao = ?, conta_id = ?, status = ?, senha = ?, data_emissao = ?, data_vencimento = ?, taxa_utilizacao = ?, limite = ?, limite_atual = ?, total_fatura = ?, total_fatura_paga = ? WHERE id = ?";
    public static final String SQL_DELETE_CARTAO = "DELETE FROM cartao WHERE id = ?";

    // SeguroDAO
    public static final String SQL_CREATE_SEGURO = "INSERT INTO seguro (tipo_seguro, num_apolice, cartao_id, data_contratacao, valor_apolice, descricao_condicoes, premio_apolice, status_seguro, data_acionamento, valor_fraude) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING id";
    public static final String SQL_READ_ALL_SEGUROS = "SELECT * FROM seguro";
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
    public static final String SQL_READ_ALL_POLITICA_DE_TAXAS = "SELECT * FROM politica_de_taxas";
    public static final String SQL_READ_POLITICA_DE_TAXAS_BY_CATEGORIA = "SELECT * FROM politica_de_taxas WHERE categoria = ?";

}
