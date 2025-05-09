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
    public static final String INICIO_REGISTRAR = "Iniciando registro de novo usuário";
    public static final String SUCESSO_REGISTRAR = "Usuário registrado com sucesso";
    public static final String INICIO_AUTENTICACAO = "Iniciando autenticação de usuário";
    public static final String SUCESSO_AUTENTICACAO = "Usuário autenticado com sucesso";

    // Roles
    public static final String ROLE_ADMIN = "hasRole('ADMIN')";
    public static final String ROLE_CLIENTE = "hasRole('CLIENTE')";

    public static final String FIM_CHAMADA = "Chamada concluída em {} ms.";
    public static final String SUCESSO_BUSCA = "Informação(ões) encontrada(s) com sucesso";

    // Usuário
    public static final String USUARIO_LOGADO = "Usuário logado: ID: {}";

    // Cliente
    public static final String INICIO_CADASTRO_CLIENTE = "Iniciando cadastro de cliente";
    public static final String SUCESSO_CADASTRO_CLIENTE = "Cliente cadastrado com sucesso: ID: {}";
    public static final String ERRO_CADASTRO_CLIENTE = "Erro ao cadastrar cliente";
    public static final String INICIO_BUSCA_CLIENTE = "Iniciando busca de cliente(s)";
    public static final String SUCESSO_BUSCA_CLIENTE = "Informações de cliente(s) encontradas com sucesso";
    public static final String CLIENTE_ENCONTRADO = "Cliente encontrado com sucesso";
    public static final String ERRO_BUSCA_CLIENTE = "Cliente ID %d não encontrado.";
    public static final String INICIO_EXCLUSAO_CLIENTE = "Iniciando exclusão de cliente ID: {}";
    public static final String SUCESSO_EXCLUSAO_CLIENTE = "Cliente excluído com sucesso ID: {}";
    public static final String ERRO_EXCLUSAO_CLIENTE = "Erro ao excluir cliente";
    public static final String INICIO_ATUALIZACAO_CLIENTE = "Iniciando atualização de cliente ID: {}";
    public static final String SUCESSO_ATUALIZACAO_CLIENTE = "Cliente atualizado com sucesso ID: {}";
    public static final String INICIO_ATUALIZACAO_CATEGORIA_CLIENTE = "Iniciando atualização de categoria do cliente ID: {}";
    public static final String SUCESSO_ATUALIZACAO_CATEGORIA_CLIENTE = "Categoria do cliente atualizada com sucesso";

    // Conta
    public static final String INICIO_ABERTURA_CONTA = "Iniciando abertura de conta para cliente ID: {}";
    public static final String SUCESSO_ABERTURA_CONTA = "Conta criada com sucesso para cliente ID: {}";
    public static final String ERRO_ABERTURA_CONTA = "Erro ao abrir conta";


    // Cartão
    public static final String INICIO_EMISSAO_CARTAO = "Iniciando emissão de cartão para cliente ID: {}";
    public static final String SUCESSO_EMISSAO_CARTAO = "Cartão emitido com sucesso para cliente ID: {}";
    public static final String ERRO_EMISSAO_CARTAO = "Erro ao emitir cartão";
    public static final String INICIO_BUSCA_CARTAO = "Iniciando busca de informação de cartão(ões)";
    public static final String SUCESSO_BUSCA_CARTAO = "Informações de cartão(ões) encontradas com sucesso";
    public static final String ERRO_BUSCA_CARTAO = "Cartão ID %d não encontrado.";
    public static final String INICIO_EXCLUSAO_CARTAO = "Iniciando exclusão de cartão para cliente ID: {}";
    public static final String SUCESSO_EXCLUSAO_CARTAO = "Cartão excluído com sucesso para cliente ID: {}";
    public static final String ERRO_EXCLUSAO_CARTAO = "Erro ao excluir cartão";
    public static final String INICIO_PAGAMENTO_CARTAO = "Iniciando pagamento com cartão ID: {}";
    public static final String SUCESSO_PAGAMENTO_CARTAO = "Pagamento realizado com sucesso";
    public static final String INICIO_ALTERACAO_LIMITE = "Iniciando alteração de limite do cartão ID: {}";
    public static final String SUCESSO_ALTERACAO_LIMITE = "Limite do cartão alterado com sucesso";
    public static final String INICIO_ALTERACAO_STATUS = "Iniciando alteração de status do cartão ID: {}";
    public static final String SUCESSO_ALTERACAO_STATUS = "Status do cartão alterado com sucesso";
    public static final String INICIO_ALTERACAO_SENHA = "Iniciando alteração de senha do cartão ID: {}";
    public static final String SUCESSO_ALTERACAO_SENHA = "Senha do cartão alterada com sucesso";
    public static final String INICIO_LEITURA_FATURA = "Iniciando leitura de fatura do cartão ID: {}";
    public static final String SUCESSO_LEITURA_FATURA = "Fatura lida com sucesso";
    public static final String INICIO_PAGAMENTO_FATURA = "Iniciando pagamento de fatura do cartão ID: {}";
    public static final String SUCESSO_PAGAMENTO_FATURA = "Pagamento de fatura realizado com sucesso";
    public static final String INICIO_RESET_LIMITE = "Iniciando redefinição de limite do cartão ID: {}";
    public static final String SUCESSO_RESET_LIMITE = "Limite do cartão redefinido com sucesso";


    // Seguro
    public static final String INICIO_CONTRATACAO_SEGURO = "Iniciando contratação de seguro para cliente ID: {}.";
    public static final String SUCESSO_CONTRATACAO_SEGURO = "Seguro contratado com sucesso para cliente ID: {}.";
    public static final String ERRO_CONTRATACAO_SEGURO = "Erro ao contratar seguro";







}
