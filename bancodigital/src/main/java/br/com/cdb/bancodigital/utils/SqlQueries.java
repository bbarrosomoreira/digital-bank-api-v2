package br.com.cdb.bancodigital.utils;

public class SqlQueries {

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
    public static final String SQL_READ_CLIENTE_BY_CPF = "SELECT * FROM cliente WHERE cpf = ?";
    public static final String SQL_COUNT_CLIENTE = "SELECT COUNT(*) FROM cliente WHERE cpf = ?";
    public static final String SQL_UPDATE_CLIENTE = "UPDATE cliente SET nome = ?, cpf = ?, categoria = ?, data_nascimento = ? WHERE id = ?";
    public static final String SQL_DELETE_CLIENTE = "DELETE FROM cliente WHERE id = ?";

    // EnderecoClienteDAO
    public static final String SQL_CREATE_ENDERECO_CLIENTE = "INSERT INTO endereco_cliente (cep, rua, numero, complemento, bairro, cidade, estado, cliente_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?) RETURNING id";
    public static final String SQL_READ_ENDERECO_CLIENTE_BY_CLIENTE = "SELECT * FROM endereco_cliente WHERE cliente_id = ?";
    public static final String SQL_UPDATE_ENDERECO_CLIENTE = "UPDATE endereco_cliente SET cep = ?, rua = ?, numero = ?, complemento = ?, bairro = ?, cidade = ?, estado = ? WHERE id = ?";

    // ContaDAO
}
