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

    // Endpoints
    public static final String USUARIO = "/usuario";
    public static final String CLIENTE = "/cliente";
    public static final String CLIENTE_ID = "/{id_cliente}";
    public static final String CONTA = "/conta";
    public static final String CONTA_ID = "/{id_conta}";
    public static final String CONTA_ORIGEM_ID = "/{id_contaOrigem}";
    public static final String RENDIMENTOS_ENDPOINT = "/rendimentos";
    public static final String MANUTENCAO_ENDPOINT = "/manutencao";
    public static final String SAQUE_ENDPOINT = "/saque";
    public static final String DEPOSITO_ENDPOINT = "/deposito";
    public static final String TRANSFERENCIA_ENDPOINT = "/transferencia";
    public static final String PIX_ENDPOINT = "/pix";
    public static final String SALDO_ENDPOINT = "/saldo";
    public static final String CARTAO = "/cartao";
    public static final String CARTAO_ID = "/{id_cartao}";
    public static final String PAGAMENTO_ENDPOINT = "/pagamento";
    public static final String FATURA_ENDPOINT = "/fatura";
    public static final String LIMITE_ENDPOINT = "/limite";
    public static final String STATUS_ENDPOINT = "/status";
    public static final String SENHA_ENDPOINT = "/senha";
    public static final String LIMITE_DIARIO = "/limite-diario";
    public static final String SEGURO = "/seguro";
    public static final String SEGURO_ID = "/{id_seguro}";
    public static final String CANCELAR_ENDPOINT = "/cancelar";
    public static final String ACIONAR_ENDPOINT = "/acionar";
    public static final String FRAUDE_ENDPOINT = "/fraude";
    public static final String VIAGEM_ENDPOINT = "/viagem";
    public static final String PREMIO_ENDPOINT = "/premio";
    public static final String TIPOS = "/tipos";
    public static final String POLITICA_TAXAS = "/politica-taxa";
    public static final String CAMBIO = "/cambio";
    public static final String AUTENTICACAO = "/auth";
    public static final String ADMIN = "/admin";
    public static final String RECEITA_FEDERAL = "/receita-federal";
    public static final String GET_CPF = "/consultar-cpf";
    public static final String GET_USUARIO = "/me";
    public static final String SIGNIN = "/signin";
    public static final String LOGIN = "/login";
    public static final String CONVERSOR_REAL = "/conversor-real";
    public static final String CONVERSOR_MOEDAS = "/conversor-moedas";
    public static final String GET_CATEGORIA = "/{categoria}";
    public static final String CATEGORIA = "/categoria";

    // Gerais
    public static final String FIM_CHAMADA = "Chamada concluída em {} ms.";
    public static final String FIM_TENTATIVA_CHAMADA = "Tentativa de chamada concluída em {} ms.";
    public static final String SUCESSO_BUSCA = "Informação(ões) encontrada(s) com sucesso";
    public static final String RETORNO_VAZIO = "Retornando Optional.empty()";
    public static final String ERRO_UPDATE = "Atualização não concluída.";
    public static final String ERRO_DELETE = "Exclusão não concluída.";

    // Usuário
    public static final String USUARIO_LOGADO = "Usuário logado: ID: {}";
    public static final String INICIO_BUSCA_USUARIO = "Iniciando busca de usuário";
    public static final String SUCESSO_BUSCA_USUARIO = "Informações de usuário obtidas com sucesso";
    public static final String USUARIO_ENCONTRADO = "Usuário encontrado com sucesso";
    public static final String ERRO_CRIAR_USUARIO = "Erro ao criar usuário";
    public static final String ERRO_BUSCA_USUARIO = "Usuário não encontrado.";
    public static final String ERRO_USUARIO_NULO = "Usuário não pode ser nulo";
    public static final String INICIO_UPDATE_USUARIO = "Iniciando atualização de usuário ID: {}";
    public static final String SUCESSO_UPDATE_USUARIO = "Usuário atualizado com sucesso ID: {}";
    public static final String INICIO_DELETE_USUARIO = "Iniciando exclusão de usuário ID: {}";
    public static final String SUCESSO_DELETE_USUARIO = "Usuário excluído com sucesso ID: {}";
    public static final String ERRO_INESPERADO_UPDATE_USUARIO = "Erro inesperado ao atualizar usuário com ID: {}";
    public static final String ERRO_INESPERADO_DELETE_USUARIO = "Erro inesperado ao excluir usuário com ID: {}";
    public static final String INICIO_CRIAR_USUARIO_BANCO_DADOS = "Iniciando criação de usuário no banco de dados";
    public static final String SUCESSO_CRIAR_USUARIO_BANCO_DADOS = "Usuário criado no banco de dados com sucesso";
    public static final String ERRO_CRIAR_USUARIO_BANCO_DADOS = "Erro ao criar usuário no banco de dados";

    // Cliente
    public static final String ID_CLIENTE = "ID do cliente: {}";
    public static final String INICIO_SALVAR_CLIENTE = "Iniciando operação de salvar cliente.";
    public static final String ERRO_SALVAR_CLIENTE = "Erro ao salvar cliente.";
    public static final String INICIO_CADASTRO_CLIENTE = "Iniciando cadastro de cliente";
    public static final String SUCESSO_CADASTRO_CLIENTE = "Cliente cadastrado com sucesso: ID: {}";
    public static final String ERRO_CADASTRO_CLIENTE = "Erro ao cadastrar cliente";
    public static final String INICIO_BUSCA_CLIENTE = "Iniciando busca de cliente(s)";
    public static final String SUCESSO_BUSCA_CLIENTE = "Informações de cliente(s) obtidas com sucesso";
    public static final String CLIENTE_ENCONTRADO = "Cliente encontrado com sucesso";
    public static final String ERRO_BUSCA_CLIENTE = "Cliente não encontrado.";
    public static final String INICIO_EXCLUSAO_CLIENTE = "Iniciando exclusão de cliente ID: {}";
    public static final String SUCESSO_EXCLUSAO_CLIENTE = "Cliente excluído com sucesso ID: {}";
    public static final String ERRO_EXCLUSAO_CLIENTE = "Erro ao excluir cliente";
    public static final String INICIO_ATUALIZACAO_CLIENTE = "Iniciando atualização de cliente ID: {}";
    public static final String SUCESSO_ATUALIZACAO_CLIENTE = "Cliente atualizado com sucesso ID: {}";
    public static final String INICIO_ATUALIZACAO_CATEGORIA_CLIENTE = "Iniciando atualização de categoria do cliente ID: {}";
    public static final String SUCESSO_ATUALIZACAO_CATEGORIA_CLIENTE = "Categoria do cliente atualizada com sucesso";
    public static final String INICIO_CRIAR_CLIENTE_BANCO_DADOS = "Iniciando criação de cliente no banco de dados";
    public static final String SUCESSO_CRIAR_CLIENTE_BANCO_DADOS = "Cliente criado no banco de dados com sucesso";
    public static final String ERRO_CRIAR_CLIENTE_BANCO_DADOS = "Erro ao criar cliente no banco de dados";
    public static final String ERRO_CLIENTE_NULO = "Cliente não pode ser nulo";

    // EnderecoCliente
    public static final String INICIO_SALVAR_ENDERECO = "Iniciando operação de salvar endereço.";
    public static final String ERRO_SALVAR_ENDERECO = "Erro ao salvar endereço.";
    public static final String INICIO_CRIAR_ENDERECO = "Iniciando criação de endereço.";
    public static final String SUCESSO_CRIAR_ENDERECO = "Endereço criado com sucesso. ID: {}.";
    public static final String ERRO_CRIAR_ENDERECO = "Erro ao criar endereço.";
    public static final String INICIO_BUSCA_ENDERECO = "Iniciando busca de endereço para cliente com ID: {}";
    public static final String ENDERECO_ENCONTRADO = "Endereço encontrado.";
    public static final String ERRO_BUSCA_ENDERECO = "Endereço não encontrado.";
    public static final String ERRO_ENDERECO_NULO = "Endereço não pode ser nulo";
    public static final String INICIO_UPDATE_ENDERECO = "Iniciando atualização de endereço com ID: {}.";
    public static final String SUCESSO_UPDATE_ENDERECO = "Endereço atualizado com sucesso. ID: {}.";
    public static final String ERRO_UPDATE_ENDERECO = "Erro ao atualizar endereço. Nenhuma linha foi afetada.";
    public static final String INICIO_BUSCA_ERRO_ENDERECO = "Buscando endereço ou lançando erro para cliente com ID: {}";
    public static final String ERRO_INESPERADO_UPDATE_ENDERECO = "Erro inesperado ao atualizar endereço com ID: {}";

    // Conta
    public static final String ID_CONTA = "ID da conta: {}";
    public static final String INICIO_ABERTURA_CONTA = "Iniciando abertura de conta para cliente ID: {}";
    public static final String SUCESSO_ABERTURA_CONTA = "Conta criada com sucesso para cliente ID: {}";
    public static final String ERRO_ABERTURA_CONTA = "Erro ao abrir conta";
    public static final String INICIO_LISTAGEM_TIPO_CONTA = "Listando tipos de contas disponíveis.";
    public static final String INICIO_BUSCA_CONTA = "Iniciando busca de conta(s)";
    public static final String SUCESSO_BUSCA_CONTA = "Informações de conta(s) obtidas com sucesso";
    public static final String CONTA_ENCONTRADA = "Conta encontrada com sucesso";
    public static final String ERRO_BUSCA_CONTA = "Conta ID %d não encontrada.";
    public static final String INICIO_EXCLUSAO_CONTA = "Iniciando exclusão de conta para cliente ID: {}";
    public static final String SUCESSO_EXCLUSAO_CONTA = "Conta excluída com sucesso para cliente ID: {}";
    public static final String INICIO_TRANSACAO_CONTA = "Iniciando transação de {} na conta ID: {}";
    public static final String TRANSFERENCIA = "TRANSFERÊNCIA";
    public static final String DEPOSITO = "DEPÓSITO";
    public static final String SAQUE = "SAQUE";
    public static final String PIX = "PIX";
    public static final String APLICACAO_TARIFA_MANUTENCAO = "APLICAÇÃO TARIFA MANUTENÇÃO";
    public static final String APLICACAO_RENDIMENTO = "APLICAÇÃO RENDIMENTO";
    public static final String SUCESSO_TRANSACAO_CONTA = "Transação {} realizada com sucesso";
    public static final String INICIO_LEITURA_SALDO = "Iniciando consulta de saldo da conta ID: {}";
    public static final String SUCESSO_LEITURA_SALDO = "Saldo consultado com sucesso";
    public static final String INICIO_CRIAR_CONTA_BANCO_DADOS = "Iniciando criação de conta no banco de dados";
    public static final String SUCESSO_CRIAR_CONTA_BANCO_DADOS = "Conta criada no banco de dados com sucesso";
    public static final String ERRO_CRIAR_CONTA_BANCO_DADOS = "Erro ao criar conta no banco de dados";

    // Cartão
    public static final String ID_CARTAO = "ID do cartão: {}";
    public static final String INICIO_EMISSAO_CARTAO = "Iniciando emissão de cartão para cliente ID: {}";
    public static final String SUCESSO_EMISSAO_CARTAO = "Cartão emitido com sucesso para cliente ID: {}";
    public static final String ERRO_EMISSAO_CARTAO = "Erro ao emitir cartão";
    public static final String INICIO_BUSCA_CARTAO = "Iniciando busca de informação de cartão(ões)";
    public static final String SUCESSO_BUSCA_CARTAO = "Informações de cartão(ões) obtidas com sucesso";
    public static final String CARTAO_ENCONTRADO = "Cartão encontrado com sucesso";
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
    public static final String INICIO_CRIAR_CARTAO_BANCO_DADOS = "Iniciando criação de cartao no banco de dados";
    public static final String SUCESSO_CRIAR_CARTAO_BANCO_DADOS = "Cartao criado no banco de dados com sucesso";
    public static final String ERRO_CRIAR_CARTAO_BANCO_DADOS = "Erro ao criar cartao no banco de dados";

    // Seguro
    public static final String ID_SEGURO = "ID do seguro: {}";
    public static final String INICIO_CONTRATACAO_SEGURO = "Iniciando contratação de seguro.";
    public static final String SUCESSO_CONTRATACAO_SEGURO = "Seguro contratado com sucesso.";
    public static final String ERRO_CONTRATACAO_SEGURO = "Erro ao contratar seguro";
    public static final String INICIO_BUSCA_SEGURO = "Iniciando busca de informação de seguro(s)";
    public static final String SUCESSO_BUSCA_SEGURO = "Informações de seguro(s) obtidas com sucesso";
    public static final String SEGURO_ENCONTRADO = "Seguro encontrado com sucesso";
    public static final String INICIO_LISTAGEM_TIPO_SEGURO = "Listando tipos de seguros disponíveis.";
    public static final String SUCESSO_LISTAGEM_TIPO_SEGURO = "Tipos de seguros listados com sucesso.";
    public static final String INICIO_EXCLUSAO_SEGURO = "Iniciando exclusão de seguro para cliente ID: {}";
    public static final String SUCESSO_EXCLUSAO_SEGURO = "Seguro excluído com sucesso para cliente ID: {}";
    public static final String INICIO_ACIONAMENTO_SEGURO = "Iniciando acionamento de seguro tipo {}";
    public static final String SEGURO_FRAUDE = "FRAUDE";
    public static final String SEGURO_VIAGEM = "VIAGEM";
    public static final String SUCESSO_ACIONAMENTO_SEGURO = "Acionamento de seguro {} realizado com sucesso";
    public static final String INICIO_DEBITO_PREMIO_SEGURO = "Iniciando débito de prêmio de seguro ID: {}";
    public static final String SUCESSO_DEBITO_PREMIO_SEGURO = "Débito de prêmio de seguro realizado com sucesso";
    public static final String INICIO_CANCELAMENTO_SEGURO = "Iniciando cancelamento de seguro ID: {}";
    public static final String SUCESSO_CANCELAMENTO_SEGURO = "Cancelamento de seguro realizado com sucesso";
    public static final String INICIO_CRIAR_SEGURO_BANCO_DADOS = "Iniciando criação de seguro no banco de dados";
    public static final String SUCESSO_CRIAR_SEGURO_BANCO_DADOS = "Seguro criado no banco de dados com sucesso";
    public static final String ERRO_CRIAR_SEGURO_BANCO_DADOS = "Erro ao criar seguro no banco de dados";

    // Cambio
    public static final String INICIO_CONVERSAO = "Iniciando conversão de moedas";
    public static final String SUCESSO_CONVERSAO = "Conversão de moedas realizada com sucesso";
    public static final String ERRO_CONVERSAO = "Conversão de moedas falhou. Verifique os parâmetros fornecidos.";

    // Politica de Taxas
    public static final String INICIO_BUSCA_POLITICA_TAXAS = "Iniciando busca de política de taxas para a categoria: {}.";
    public static final String SUCESSO_BUSCA_POLITICA_TAXAS = "Parâmetros de taxas encontrados com sucesso para a categoria: {}.";
    public static final String INICIO_LISTAGEM_POLITICA_TAXAS = "Iniciando listagem de todas as políticas de taxas.";
    public static final String SUCESSO_LISTAGEM_POLITICA_TAXAS = "Políticas de taxas listadas com sucesso.";
    public static final String ERRO_LISTAGEM_POLITICA_TAXAS = "Erro ao listar políticas de taxas.";
    public static final String ERRO_POLITICA_TAXAS_NULA = "Política de taxas não pode ser nula";
    public static final String ERRO_BUSCA_POLITICA_TAXAS = "Política de taxas não encontrada.";

    // CPF
    public static final String INICIO_CONSULTA_CPF = "Iniciando consulta de CPF";
    public static final String SUCESSO_CONSULTA_CPF = "Consulta de CPF concluída com sucesso.";


}
