package br.com.cdb.bancodigital.utils;

public class ConstantUtils {

    private ConstantUtils() {
        throw new IllegalStateException("Utility class");
    }

    // Banco de Dados
    public static final String INICIO_CONEXAO_BANCO = "Iniciando conexão com o banco de dados";
    public static final String SUCESSO_CONEXAO_BANCO = "Conexão com o banco de dados estabelecida com sucesso";
    public static final String ERRO_CONEXAO_BANCO = "Erro ao estabelecer conexão com o banco de dados";

    // Autenticação e Autorização
    public static final String INICIO_FILTRO_JWT = "Iniciando o filtro de autenticação JWT";
    public static final String HEATHER_AUTHORIZATION = "Authorization";
    public static final String HEADER_BEARER = "Bearer ";
    public static final String HEADER_AUTENTICACAO_ENCONTRADO = "Header de autenticação encontrado";
    public static final String PROCESSANDO_TOKEN = "Processando o token JWT";
    public static final String SUCESSO_TOKEN_PROCESSADO = "Token JWT processado com sucesso";
    public static final String TOKEN_EXPIRADO = "Token expirado";
    public static final String TOKEN_INVALIDO = "Token inválido";
    public static final String TOKEN_MALFORMADO = "Token malformado ou não suportado";
    public static final String ASSINATURA_INVALIDA = "Assinatura inválida do token";
    public static final String ERRO_AUTENTICACAO = "Erro ao autenticar o usuário";
    public static final String FIM_FILTRO_JWT = "Filtro de autenticação JWT finalizado";
    public static final String SECURITY_FILTER_CHAIN = "Configurando SecurityFilterChain";
    public static final String ROTAS_LIBERADAS = "/auth/**";
    public static final String ACESSO_PUBLICO = "Permitindo acesso público às rotas" + ROTAS_LIBERADAS + " e exigindo autenticação para outras rotas";
    public static final String POLITICA_SESSION_STATELESS = "Definindo política de sessão como STATELESS";
    public static final String SUCESSO_SECURITY_FILTER_CHAIN = "SecurityFilterChain configurado com sucesso";
    public static final String AUTENTICACAO_PROVIDER = "Criando AuthenticationProvider...";
    public static final String SUCESSO_AUTENTICACAO_PROVIDER = "AuthenticationProvider criado com sucesso";
    public static final String CRIANDO_PASSWORD_ENCODER = "Criando PasswordEncoder (BCryptPasswordEncoder)...";
    public static final String AUTHENTICACAO_MANAGER = "Criando AuthenticationManager...";


    public static final String ERRO_CLIENTE_NAO_ENCONTRADO = "Cliente ID %d não encontrado.";


    public static final String USUARIO_LOGADO = "Usuário logado: ID: {}";

    public static final String CLIENTE_ENCONTRADO = "Cliente encontrado com sucesso";

    public static final String CARTAO_ENCONTRADO = "Cartão(ões) encontrado(s) com sucesso";

}
