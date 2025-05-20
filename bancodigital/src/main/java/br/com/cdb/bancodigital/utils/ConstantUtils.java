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

    // AuthService
    public static final String LOG_VERIFICANDO_EMAIL_CADASTRADO = "Verificando se o e-mail já está cadastrado";
    public static final String LOG_EMAIL_JA_CADASTRADO = "E-mail já cadastrado";
    public static final String EXC_EMAIL_JA_CADASTRADO = "E-mail já cadastrado";
    public static final String LOG_EMAIL_DISPONIVEL = "E-mail disponível";
    public static final String LOG_CRIANDO_NOVO_USUARIO = "Criando novo usuário";
    public static final String LOG_USUARIO_CRIADO_SUCESSO = "Usuário criado com sucesso";
    public static final String LOG_GERANDO_TOKEN = "Gerando token";
    public static final String LOG_TOKEN_GERADO_SUCESSO = "Token gerado com sucesso";
    public static final String LOG_ERRO_GERAR_TOKEN_ARG = "Erro ao gerar token: argumento inválido";
    public static final String EXC_ERRO_GERAR_TOKEN_ARG = "Erro ao gerar token: argumento inválido.";
    public static final String LOG_ERRO_GERAR_TOKEN_JWT = "Erro ao gerar token JWT";
    public static final String EXC_ERRO_GERAR_TOKEN_JWT = "Erro ao gerar token JWT.";
    public static final String LOG_ERRO_INESPERADO_AUTENTICAR = "Erro inesperado ao autenticar usuário";
    public static final String EXC_ERRO_INESPERADO_AUTENTICAR = "Erro inesperado ao autenticar usuário.";
    public static final String LOG_AUTENTICANDO_USUARIO = "Autenticando usuário";
    public static final String LOG_USUARIO_AUTENTICADO_SUCESSO = "Usuário autenticado com sucesso";
    public static final String LOG_BUSCANDO_USUARIO_BANCO = "Buscando usuário no banco de dados";
    public static final String LOG_USUARIO_ENCONTRADO_SUCESSO = "Usuário encontrado com sucesso";

    // JwtService
    public static final String JWT_GERANDO_TOKEN = "Iniciando geração de token para o usuário.";
    public static final String JWT_TOKEN_GERADO_SUCESSO = "Token gerado com sucesso.";
    public static final String JWT_EXTRAINDO_USERNAME = "Extraindo username do token.";
    public static final String JWT_EXTRAINDO_CLAIM = "Extraindo claim do token.";
    public static final String JWT_EXTRAINDO_TODOS_CLAIMS = "Extraindo todos os claims do token.";
    public static final String JWT_VALIDANDO_TOKEN = "Validando token para o usuário.";
    public static final String JWT_TOKEN_VALIDO = "Token válido.";
    public static final String JWT_TOKEN_INVALIDO = "Token inválido.";
    public static final String JWT_TOKEN_EXPIRADO = "Token expirado.";
    public static final String JWT_EXTRAINDO_EXPIRACAO = "Extraindo data de expiração do token.";

    // Roles
    public static final String ROLE_ADMIN = "hasRole('ADMIN')";
    public static final String ROLE_CLIENTE = "hasRole('CLIENTE')";

    // Endpoints
    public static final String USUARIO = "/usuario";
    public static final String DELETE_USUARIO = "/{id_usuario}";
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
    public static final String CATEGORIA_EP = "/categoria";

    // Gerais
    public static final String FIM_CHAMADA = "Chamada concluída em {} ms.";
    public static final String FIM_TENTATIVA_CHAMADA = "Tentativa de chamada concluída em {} ms.";
    public static final String SUCESSO_BUSCA = "Informação(ões) encontrada(s) com sucesso";
    public static final String RETORNO_VAZIO = "Retornando Optional.empty()";
    public static final String ERRO_UPDATE = "Atualização não concluída. Nenhuma linha foi afetada.";
    public static final String ERRO_DELETE = "Exclusão não concluída. Nenhuma linha foi afetada.";
    public static final String VALOR_INESPERADO = "Valor inesperado: {}";

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
    public static final String CATEGORIA = "categoria";
    public static final String INICIO_SALVAR_CLIENTE = "Iniciando operação de salvar cliente.";
    public static final String ERRO_SALVAR_CLIENTE = "Erro ao salvar cliente.";
    public static final String INICIO_CADASTRO_CLIENTE = "Iniciando cadastro de cliente";
    public static final String SUCESSO_CADASTRO_CLIENTE = "Cliente cadastrado com sucesso: ID: {}";
    public static final String ERRO_CADASTRO_CLIENTE = "Erro ao cadastrar cliente";
    public static final String INICIO_BUSCA_CLIENTE = "Iniciando busca de cliente(s)";
    public static final String SUCESSO_BUSCA_CLIENTE = "Informações de cliente(s) obtidas com sucesso";
    public static final String CLIENTE_ENCONTRADO = "Cliente encontrado com sucesso";
    public static final String ERRO_BUSCA_CLIENTE = "Cliente não encontrado.";
    public static final String ERRO_CLIENTE_NAO_ENCONTRADO_USUARIO_LOGADO = "Cliente não encontrado para o usuário logado.";
    public static final String INICIO_DELETE_CLIENTE = "Iniciando exclusão de cliente ID: {}";
    public static final String SUCESSO_DELETE_CLIENTE = "Cliente excluído com sucesso ID: {}";
    public static final String ERRO_DELETE_CLIENTE = "Erro ao excluir cliente com ID: {}";
    public static final String INICIO_ATUALIZACAO_CLIENTE = "Iniciando atualização de cliente ID: {}";
    public static final String SUCESSO_ATUALIZACAO_CLIENTE = "Cliente atualizado com sucesso ID: {}";
    public static final String INICIO_ATUALIZACAO_CATEGORIA_CLIENTE = "Iniciando atualização de categoria do cliente ID: {}";
    public static final String SUCESSO_ATUALIZACAO_CATEGORIA_CLIENTE = "Categoria do cliente atualizada com sucesso";
    public static final String INICIO_CRIAR_CLIENTE_BANCO_DADOS = "Iniciando criação de cliente no banco de dados";
    public static final String SUCESSO_CRIAR_CLIENTE_BANCO_DADOS = "Cliente criado no banco de dados com sucesso";
    public static final String ERRO_CRIAR_CLIENTE_BANCO_DADOS = "Erro ao criar cliente no banco de dados";
    public static final String ERRO_CLIENTE_NULO = "Cliente não pode ser nulo";
    public static final String INICIO_VERIFICAR_CLIENTE_CPF = "Iniciando verificação se existe no banco de dados cliente com o CPF solicitado.";
    public static final String SUCESSO_VERIFICAR_CLIENTE_CPF = "Verificação de cliente com CPF concluída com sucesso.";
    public static final String ERRO_VERIFICAR_CLIENTE_CPF = "Erro ao verificar cliente com CPF.";
    public static final String VAZIO_VERIFICAR_CLIENTE_CPF = "Nenhum cliente encontrado com o CPF solicitado.";
    public static final String INICIO_UPDATE_CLIENTE = "Iniciando atualização de cliente ID: {}";
    public static final String SUCESSO_UPDATE_CLIENTE = "Cliente atualizado com sucesso ID: {}";
    public static final String ERRO_INESPERADO_UPDATE_CLIENTE = "Erro inesperado ao atualizar cliente com ID: {}";
    public static final String ERRO_INESPERADO_DELETE_CLIENTE = "Erro inesperado ao deletar cliente com ID: {}";

    // ClienteService
    public static final String CLIENTE_POSSUI_VINCULOS = "Cliente ID {} possui vínculos com contas, cartões ou seguros";
    public static final String ERRO_CLIENTE_POSSUI_VINCULOS = "Cliente possui vínculos com contas, cartões ou seguros e não pode ser deletado.";
    public static final String ERRO_UPDATE_CLIENTE = "Erro ao acessar o banco de dados ao atualizar cliente ID {}: ";
    public static final String CLIENTE_SEM_CONTAS = "Cliente ID {} não possui contas.";
    public static final String CLIENTE_SEM_SEGUROS = "Cliente Id {} não possui seguros.";
    public static final String ERRO_CLIENTE_JA_NA_CATEGORIA = "Cliente com ID {} já está na categoria {}. Nenhuma atualização necessária.";
    public static final String ERRO_VALIDACAO_ATUALIZACAO_CATEGORIA = "Erro de validação ao atualizar categoria do cliente com ID {}: ";
    public static final String ERRO_BANCO_DADOS_ATUALIZACAO_CATEGORIA = "Erro ao acessar o banco de dados ao atualizar categoria do cliente com ID {}: ";
    public static final String ERRO_INTERNO_ATUALIZACAO_CATEGORIA = "Erro ao atualizar Política de Taxas do cliente. Tente novamente mais tarde.";

    // EnderecoCliente
    public static final String INICIO_SALVAR_ENDERECO = "Iniciando operação de salvar endereço.";
    public static final String ERRO_SALVAR_ENDERECO = "Erro ao salvar endereço.";
    public static final String INICIO_CRIAR_ENDERECO = "Iniciando criação de endereço.";
    public static final String SUCESSO_CRIAR_ENDERECO = "Endereço criado com sucesso. ID: {}.";
    public static final String ERRO_CRIAR_ENDERECO = "Erro ao criar endereço.";
    public static final String INICIO_BUSCA_ENDERECO = "Iniciando busca de endereço para cliente com ID: {}";
    public static final String ENDERECO_ENCONTRADO = "Endereço encontrado: ID {}";
    public static final String ERRO_BUSCA_ENDERECO = "Endereço não encontrado.";
    public static final String ERRO_ENDERECO_NULO = "Endereço não pode ser nulo";
    public static final String INICIO_UPDATE_ENDERECO = "Iniciando atualização de endereço com ID: {}.";
    public static final String SUCESSO_UPDATE_ENDERECO = "Endereço atualizado com sucesso. ID: {}.";
    public static final String INICIO_BUSCA_ERRO_ENDERECO = "Buscando endereço ou lançando erro para cliente com ID: {}";
    public static final String ERRO_INESPERADO_UPDATE_ENDERECO = "Erro inesperado ao atualizar endereço com ID: {}";

    // Conta
    public static final String ID_CONTA = "ID da conta: {}";
    public static final String ERRO_CONTA_NULA = "Conta não pode ser nula";
    public static final String INICIO_SALVAR_CONTA = "Iniciando operação de salvar conta.";
    public static final String ERRO_SALVAR_CONTA = "Erro ao salvar conta.";
    public static final String INICIO_ABERTURA_CONTA = "Iniciando abertura de conta para cliente ID: {}";
    public static final String SUCESSO_ABERTURA_CONTA = "Abertura de conta realizada com sucesso para cliente ID: {}";
    public static final String ERRO_ABERTURA_CONTA = "Erro ao abrir conta";
    public static final String INICIO_LISTAGEM_TIPO_CONTA = "Listando tipos de contas disponíveis.";
    public static final String INICIO_BUSCA_CONTA = "Iniciando busca de conta(s)";
    public static final String SUCESSO_BUSCA_CONTA = "Informações de conta(s) obtidas com sucesso";
    public static final String CONTA_ENCONTRADA = "Conta encontrada com sucesso";
    public static final String ERRO_BUSCA_CONTA = "Conta ID %d não encontrada.";
    public static final String INICIO_DELETE_CONTA = "Iniciando exclusão de conta para cliente ID: {}";
    public static final String SUCESSO_DELETE_CONTA = "Conta excluída com sucesso para cliente ID: {}";
    public static final String INICIO_TRANSACAO_CONTA = "Iniciando transação de {} na conta ID: {}";
    public static final String TRANSFERENCIA = "TRANSFERÊNCIA";
    public static final String DEPOSITO = "DEPÓSITO";
    public static final String SAQUE = "SAQUE";
    public static final String PIX = "PIX";
    public static final String APLICACAO_TARIFA_MANUTENCAO = "APLICAÇÃO TARIFA MANUTENÇÃO";
    public static final String APLICACAO_RENDIMENTO = "APLICAÇÃO RENDIMENTO";
    public static final String SUCESSO_TRANSACAO_CONTA = "Transação {} realizada com sucesso";
    public static final String ERRO_TRANSACAO_CONTA = "Erro ao realizar transação na conta ID: {}";
    public static final String INICIO_LEITURA_SALDO = "Iniciando consulta de saldo da conta ID: {}";
    public static final String SUCESSO_LEITURA_SALDO = "Saldo consultado com sucesso";
    public static final String SALDO_SUFICIENTE = "Saldo suficiente para a transação";
    public static final String INICIO_CRIAR_CONTA_BANCO_DADOS = "Iniciando criação de conta no banco de dados";
    public static final String SUCESSO_CRIAR_CONTA_BANCO_DADOS = "Conta criada no banco de dados com sucesso";
    public static final String ERRO_CRIAR_CONTA_BANCO_DADOS = "Erro ao criar conta no banco de dados";
    public static final String INICIO_VERIFICAR_CONTA_CLIENTE = "Iniciando verificação se existe no banco de dados conta vinculada ao cliente.";
    public static final String SUCESSO_VERIFICAR_CONTA_CLIENTE = "Verificação de conta vinculada ao cliente concluída com sucesso.";
    public static final String ERRO_VERIFICAR_CONTA_CLIENTE = "Erro ao verificar conta vinculada ao cliente.";
    public static final String VAZIO_VERIFICAR_CONTA_CLIENTE = "Nenhuma conta encontrada vinculada ao cliente.";
    public static final String INICIO_UPDATE_CONTA = "Iniciando atualização de conta ID: {}";
    public static final String SUCESSO_UPDATE_CONTA = "Conta atualizada com sucesso ID: {}";
    public static final String ERRO_INESPERADO_UPDATE_CONTA = "Erro inesperado ao atualizar conta com ID: {}";
    public static final String ERRO_INESPERADO_DELETE_CONTA = "Erro inesperado ao deletar conta com ID: {}";
    public static final String INICIO_BUSCA_CONTA_POR_CLIENTE = "Iniciando busca de contas para o cliente com ID: {}.";
    public static final String SUCESSO_BUSCA_CONTA_POR_CLIENTE = "Busca de contas para o cliente com ID: {} concluída com sucesso.";
    public static final String ERRO_BUSCA_CONTA_POR_CLIENTE = "Erro ao buscar contas para o cliente com ID: {}.";
    public static final String INICIO_BUSCA_CONTA_POR_USUARIO = "Iniciando busca de contas para o usuário com ID: {}.";
    public static final String SUCESSO_BUSCA_CONTA_POR_USUARIO = "Busca de contas para o usuário com ID: {} concluída com sucesso.";
    public static final String ERRO_BUSCA_CONTA_POR_USUARIO = "Erro ao buscar contas para o usuário com ID: {}.";
    public static final String INICIO_BUSCA_CONTA_POR_TIPO = "Iniciando busca de contas do tipo '{}' para o cliente com ID: {}.";
    public static final String SUCESSO_BUSCA_CONTA_POR_TIPO = "Busca de contas do tipo '{}' para o cliente com ID: {} concluída com sucesso.";
    public static final String ERRO_BUSCA_CONTA_POR_TIPO = "Erro ao buscar contas do tipo '{}' para o cliente com ID: {}.";

    // Mensagens de validação - DTO
    public static final String TIPO_CONTA_OBRIGATORIO = "Tipo de conta é obrigatório";
    public static final String MOEDA_CONTA_OBRIGATORIO = "Moeda da conta é obrigatório";
    public static final String CPF_OBRIGATORIO = "CPF é obrigatório";
    public static final String CPF_INVALIDO = "CPF inválido";
    public static final String NOME_OBRIGATORIO = "Nome é obrigatório";
    public static final String NOME_TAMANHO = "Nome deve ter entre 2 e 100 caracteres";
    public static final String NOME_FORMATO = "Nome deve conter apenas letras e espaços";
    public static final String DATA_NASCIMENTO_PASSADO = "Data de nascimento deve ser no passado";
    public static final String DATA_NASCIMENTO_NULA = "Data de nascimento não pode ser nula";
    public static final String CEP_OBRIGATORIO = "CEP é obrigatório";
    public static final String RUA_OBRIGATORIA = "Rua é um campo obrigatório";
    public static final String BAIRRO_OBRIGATORIO = "Bairro é um campo obrigatório";
    public static final String CIDADE_OBRIGATORIA = "Cidade é um campo obrigatório";
    public static final String CIDADE_TAMANHO = "Cidade deve ter no máximo 100 caracteres";
    public static final String ESTADO_OBRIGATORIO = "Estado é um campo obrigatório";
    public static final String ESTADO_TAMANHO = "Estado deve ter exatamente 2 caracteres (ex: SP)";
    public static final String CEP_FORMATO = "CEP deve estar no formato XXXXXXXX";
    public static final String NUMERO_OBRIGATORIO = "Número é um campo obrigatório - Se endereço não tiver número, digite 0";
    public static final String COMPLEMENTO_TAMANHO = "Complemento deve ter no máximo 100 caracteres";
    public static final String CATEGORIA_OBRIGATORIA = "Categoria é um campo obrigatório";
    public static final String SENHA_TAMANHO = "A senha deve ter exatamente 4 dígitos numéricos";
    public static final String VALOR_FRAUDE_OBRIGATORIO = "Valor da fraude é obrigatório";
    public static final String ID_CONTA_DESTINO_OBRIGATORIO = "ID da conta de destino é obrigatório";
    public static final String VALOR_POSITIVO = "O valor deve ser positivo";
    public static final String VALOR_MINIMO = "O valor mínimo é R$1,00";
    public static final String DESCRICAO_TAMANHO = "A descrição deve ter no máximo 100 caracteres.";
    public static final String MOEDA_OBRIGATORIA = "Moeda da conta é obrigatório";
    public static final String EMAIL_OBRIGATORIO = "O e-mail é obrigatório e deve ser válido";
    public static final String SENHA_OBRIGATORIA = "A senha é obrigatória e deve ter no mínimo 6 caracteres";
    public static final String SENHA_ANTIGA_INCORRETA = "A senha antiga está incorreta";
    public static final String TIPO_CARTAO_OBRIGATORIO = "O tipo do cartão é obrigatório";

    // Mensagens de validação - Enums
    public static final String TIPO_SEGURO_NULO = "Tipo de seguro não pode ser nulo.";
    public static final String TIPO_SEGURO_INVALIDO = "Tipo de seguro inválido: %s. Valores permitidos: %s.";
    public static final String TIPO_CONTA_NULO = "Tipo de conta não pode ser nulo.";
    public static final String TIPO_CONTA_INVALIDO = "Tipo de conta inválido: %s. Valores permitidos: %s.";
    public static final String TIPO_CARTAO_NULO = "Tipo de cartão não pode ser nulo.";
    public static final String TIPO_CARTAO_INVALIDO = "Tipo de cartão inválido: %s. Valores permitidos: %s.";
    public static final String STATUS_NULO = "Status não pode ser nulo.";
    public static final String STATUS_INVALIDO = "Status inválido: %s. Valores permitidos: %s.";
    public static final String ROLE_NULA = "Role não pode ser nula.";
    public static final String ROLE_INVALIDA = "Valor de role inválido: %s. Valores permitidos: %s.";
    public static final String MOEDA_NULA = "Moeda não pode ser nula.";
    public static final String MOEDA_INVALIDA = "Moeda inválida: %s. Valores permitidos: %s.";
    public static final String CATEGORIA_NULA = "Categoria não pode ser nula.";
    public static final String CATEGORIA_INVALIDA = "Categoria inválida: %s. Valores permitidos: %s.";

    // Cartão
    public static final String ID_CARTAO = "ID do cartão: {}";
    public static final String ERRO_CARTAO_NULO = "Cartão não pode ser nulo";
    public static final String INICIO_SALVAR_CARTAO = "Iniciando operação de salvar cartão.";
    public static final String ERRO_SALVAR_CARTAO = "Erro ao salvar cartão.";
    public static final String INICIO_EMISSAO_CARTAO = "Iniciando emissão de cartão para cliente ID: {}";
    public static final String SUCESSO_EMISSAO_CARTAO = "Emissão de cartão realizada com sucesso para cliente ID: {}";
    public static final String ERRO_EMISSAO_CARTAO = "Erro ao emitir cartão";
    public static final String INICIO_BUSCA_CARTAO = "Iniciando busca de informação de cartão(ões)";
    public static final String SUCESSO_BUSCA_CARTAO = "Informações de cartão(ões) obtidas com sucesso";
    public static final String CARTAO_ENCONTRADO = "Cartão encontrado com sucesso";
    public static final String CARTAO_ATIVO = "Cartão ativo";
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
    public static final String INICIO_UPDATE_CARTAO = "Iniciando atualização de cartão ID: {}";
    public static final String SUCESSO_UPDATE_CARTAO = "Cartão atualizado com sucesso ID: {}";
    public static final String INICIO_DELETE_CARTAO = "Iniciando exclusão de cartão ID: {}";
    public static final String SUCESSO_DELETE_CARTAO = "Cartão excluído com sucesso ID: {}";
    public static final String ERRO_INESPERADO_UPDATE_CARTAO = "Erro inesperado ao atualizar cartão com ID: {}";
    public static final String ERRO_INESPERADO_DELETE_CARTAO = "Erro inesperado ao deletar cartão com ID: {}";
    public static final String INICIO_BUSCA_CARTAO_POR_CONTA = "Iniciando busca de cartões por conta ID: {}";
    public static final String SUCESSO_BUSCA_CARTAO_POR_CONTA = "Busca de cartões para a conta com ID: {} concluída com sucesso.";
    public static final String ERRO_BUSCA_CARTAO_POR_CONTA = "Erro ao buscar cartões para a conta com ID: {}.";
    public static final String INICIO_VERIFICAR_CARTAO_POR_CONTA = "Iniciando verificação de cartões para a conta com ID: {}.";
    public static final String SUCESSO_VERIFICAR_CARTAO_POR_CONTA = "Verificação de cartões para a conta com ID: {} concluída. Existe: {}.";
    public static final String ERRO_VERIFICAR_CARTAO_POR_CONTA = "Erro ao verificar cartões para a conta com ID: {}.";
    public static final String INICIO_VERIFICAR_CARTAO_POR_CLIENTE = "Iniciando verificação de cartões para o cliente com ID: {}.";
    public static final String SUCESSO_VERIFICAR_CARTAO_POR_CLIENTE = "Verificação de cartões para o cliente com ID: {} concluída. Existe: {}.";
    public static final String ERRO_VERIFICAR_CARTAO_POR_CLIENTE = "Erro ao verificar cartões para o cliente com ID: {}.";
    public static final String INICIO_BUSCA_CARTAO_POR_USUARIO = "Iniciando busca de cartões para o usuário com ID: {}.";
    public static final String SUCESSO_BUSCA_CARTAO_POR_USUARIO = "Busca de cartões para o usuário com ID: {} concluída com sucesso.";
    public static final String ERRO_BUSCA_CARTAO_POR_USUARIO = "Erro ao buscar cartões para o usuário com ID: {}.";
    public static final String INICIO_BUSCA_CARTAO_POR_CLIENTE = "Iniciando busca de cartões por cliente ID: {}";
    public static final String SUCESSO_BUSCA_CARTAO_POR_CLIENTE = "Busca de cartões para o cliente com ID: {} concluída com sucesso.";
    public static final String ERRO_BUSCA_CARTAO_POR_CLIENTE = "Erro ao buscar cartões para o cliente com ID: {}.";
    public static final String INICIO_BUSCA_CARTAO_POR_TIPO = "Iniciando busca de cartões do tipo '{}' para o cliente com ID: {}.";
    public static final String SUCESSO_BUSCA_CARTAO_POR_TIPO = "Busca de cartões do tipo '{}' para o cliente com ID: {} concluída com sucesso.";
    public static final String ERRO_BUSCA_CARTAO_POR_TIPO = "Erro ao buscar cartões do tipo '{}' para o cliente com ID: {}.";

    // CartaoService
    public static final String BUSCANDO_CARTOES_VINCULADOS_CONTA = "Buscando cartões vinculados à conta ID: {}";
    public static final String BUSCANDO_CARTOES_VINCULADOS_CLIENTE = "Buscando cartões vinculados ao cliente ID: {}";
    public static final String CLIENTE_SEM_CARTOES = "Cliente Id {} não possui cartões.";
    public static final String CARTAO_DELETADO_SUCESSO = "Cartão ID {} deletado com sucesso";
    public static final String ERRO_DELETAR_CARTAO = "Falha ao deletar cartão ID {}";
    public static final String ERRO_DELETAR_CARTAO_MENSAGEM = "Erro ao deletar cartão: ";
    public static final String ERRO_SALDO_INSUFICIENTE = "Saldo insuficiente para esta transação.";
    public static final String ERRO_LIMITE_CONSUMIDO = "Limite não pode ser alterado, pois o limite consumido é maior do que o novo valor de limite.";

    // Seguro
    public static final String ID_SEGURO = "ID do seguro: {}";
    public static final String ERRO_SEGURO_NULO = "Seguro não pode ser nulo";
    public static final String INICIO_SALVAR_SEGURO = "Iniciando operação de salvar seguro.";
    public static final String ERRO_SALVAR_SEGURO = "Erro ao salvar seguro.";
    public static final String INICIO_CONTRATACAO_SEGURO = "Iniciando contratação de seguro.";
    public static final String SUCESSO_CONTRATACAO_SEGURO = "Contratação de seguro realizada com sucesso.";
    public static final String ERRO_CONTRATACAO_SEGURO = "Erro ao contratar seguro";
    public static final String INICIO_BUSCA_SEGURO = "Iniciando busca de informação de seguro(s)";
    public static final String SUCESSO_BUSCA_SEGURO = "Informações de seguro(s) obtidas com sucesso";
    public static final String ERRO_BUSCA_SEGURO = "Seguro ID %d não encontrado.";
    public static final String SEGURO_ENCONTRADO = "Seguro encontrado com sucesso";
    public static final String INICIO_LISTAGEM_TIPO_SEGURO = "Listando tipos de seguros disponíveis.";
    public static final String SUCESSO_LISTAGEM_TIPO_SEGURO = "Tipos de seguros listados com sucesso.";
    public static final String INICIO_DELETE_SEGURO = "Iniciando exclusão de seguro para cliente ID: {}";
    public static final String SUCESSO_DELETE_SEGURO = "Seguro excluído com sucesso para cliente ID: {}";
    public static final String INICIO_ACIONAMENTO_SEGURO = "Iniciando acionamento de seguro.";
    public static final String SEGURO_FRAUDE = "FRAUDE";
    public static final String SEGURO_VIAGEM = "VIAGEM";
    public static final String SUCESSO_ACIONAMENTO_SEGURO = "Acionamento de seguro {} realizado com sucesso";
    public static final String ERRO_ACIONAMENTO_SEGURO = "Erro ao acionar seguro";
    public static final String ERRO_SEGURO_DESATIVADO = "Seguro desativado - operação bloqueada";
    public static final String INICIO_DEBITO_PREMIO_SEGURO = "Iniciando débito de prêmio de seguro ID: {}";
    public static final String SUCESSO_DEBITO_PREMIO_SEGURO = "Débito de prêmio de seguro realizado com sucesso";
    public static final String ERRO_DEBITO_PREMIO_SEGURO = "Erro ao debitar prêmio de seguro";
    public static final String INICIO_CANCELAMENTO_SEGURO = "Iniciando cancelamento de seguro ID: {}";
    public static final String SUCESSO_CANCELAMENTO_SEGURO = "Cancelamento de seguro realizado com sucesso";
    public static final String ERRO_CANCELAMENTO_SEGURO = "Erro ao cancelar seguro";
    public static final String INICIO_CRIAR_SEGURO_BANCO_DADOS = "Iniciando criação de seguro no banco de dados";
    public static final String SUCESSO_CRIAR_SEGURO_BANCO_DADOS = "Seguro criado no banco de dados com sucesso";
    public static final String ERRO_CRIAR_SEGURO_BANCO_DADOS = "Erro ao criar seguro no banco de dados";
    public static final String INICIO_UPDATE_SEGURO = "Iniciando atualização de seguro ID: {}";
    public static final String SUCESSO_UPDATE_SEGURO = "Seguro atualizado com sucesso ID: {}";
    public static final String ERRO_INESPERADO_UPDATE_SEGURO = "Erro inesperado ao atualizar seguro com ID: {}";
    public static final String ERRO_INESPERADO_DELETE_SEGURO = "Erro inesperado ao deletar seguro com ID: {}";
    public static final String INICIO_BUSCA_SEGURO_POR_TIPO = "Iniciando busca de seguros do tipo '{}' para o cliente com ID: {}.";
    public static final String SUCESSO_BUSCA_SEGURO_POR_TIPO = "Busca de seguros do tipo '{}' para o cliente com ID: {} concluída com sucesso.";
    public static final String ERRO_BUSCA_SEGURO_POR_TIPO = "Erro ao buscar seguros do tipo '{}' para o cliente com ID: {}.";
    public static final String INICIO_VERIFICAR_SEGURO_POR_CARTAO = "Iniciando verificação de seguros para o cartão com ID: {}.";
    public static final String SUCESSO_VERIFICAR_SEGURO_POR_CARTAO = "Verificação de seguros para o cartão com ID: {} concluída. Existe: {}.";
    public static final String ERRO_VERIFICAR_SEGURO_POR_CARTAO = "Erro ao verificar seguros para o cartão com ID: {}.";
    public static final String INICIO_VERIFICAR_SEGURO_POR_CLIENTE = "Iniciando verificação de seguros para o cliente com ID: {}.";
    public static final String SUCESSO_VERIFICAR_SEGURO_POR_CLIENTE = "Verificação de seguros para o cliente com ID: {} concluída. Existe: {}.";
    public static final String ERRO_VERIFICAR_SEGURO_POR_CLIENTE = "Erro ao verificar seguros para o cliente com ID: {}.";
    public static final String INICIO_BUSCA_SEGURO_POR_CLIENTE = "Iniciando busca de seguros para o cliente com ID: {}.";
    public static final String SUCESSO_BUSCA_SEGURO_POR_CLIENTE = "Busca de seguros para o cliente com ID: {} concluída com sucesso.";
    public static final String ERRO_BUSCA_SEGURO_POR_CLIENTE = "Erro ao buscar seguros para o cliente com ID: {}.";
    public static final String INICIO_BUSCA_SEGURO_POR_USUARIO = "Iniciando busca de seguros para o usuário com ID: {}.";
    public static final String SUCESSO_BUSCA_SEGURO_POR_USUARIO = "Busca de seguros para o usuário com ID: {} concluída com sucesso.";
    public static final String ERRO_BUSCA_SEGURO_POR_USUARIO = "Erro ao buscar seguros para o usuário com ID: {}.";


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
    public static final String ERRO_BUSCA_POLITICA_TAXAS = "Parâmetros não encontrados para a categoria: %s";

    // CPF
    public static final String INICIO_CONSULTA_CPF = "Iniciando consulta de CPF";
    public static final String SUCESSO_CONSULTA_CPF = "Consulta de CPF concluída com sucesso.";

    // Formatos de Data
    public static final String FORMATO_DATA_DD_MM_YYYY = "dd-MM-yyyy";

    // Valores mínimos
    public static final String VALOR_MIN_SAQUE = "1.00";
    public static final String VALOR_MIN_DEPOSITO = "1.00";
    public static final String VALOR_MIN_TRANSFERENCIA = "1.00";
    public static final String VALOR_MIN_PIX = "1.00";

    // GlobalExceptionHandler
    public static final String ERRO_TRATADO = "Erro tratado - Status: {}, Caminho: {}, Mensagem: {}";
    public static final String ERRO_VALIDACAO = "Erro de validação";
    public static final String ACESSO_NEGADO = "Acesso negado";
    public static final String MENSAGEM_ACESSO_NEGADO = "Você não tem permissão para acessar este recurso.";
    public static final String ERRO_TIPO_ARGUMENTO = "Erro de tipo de argumento";
    public static final String MENSAGEM_TIPO_ARGUMENTO = "O valor '%s' não é válido para o parâmetro '%s'.";
    public static final String ERRO_TIMEOUT = "Erro de timeout - Caminho: {}, Mensagem: {}";
    public static final String TIMEOUT = "Timeout";
    public static final String ERRO_INTERNO = "Erro interno";
    public static final String MENSAGEM_ERRO_INTERNO = "Ocorreu um erro inesperado. Tente novamente mais tarde.";

    // Descrições e textos - Enums
    public static final String DESCRICAO_SEGURO_VIAGEM = "Cobertura para despesas médicas no exterior, cancelamento de voos e extravio de bagagem, com um valor base de R$10.000,00.";
    public static final String CONDICOES_SEGURO_VIAGEM = "Clientes Comum e Super: opcional por R$50,00 por mês. Clientes Premium: isento de tarifa.";
    public static final String DESCRICAO_SEGURO_FRAUDE = "Cobertura automática para fraudes no cartão, com um valor base de R$5.000,00";
    public static final String CONDICOES_SEGURO_FRAUDE = "Serviço gratuito para todas as categorias de clientes.";

    public static final String DESCRICAO_CONTA_CORRENTE = "Conta Corrente";
    public static final String DESCRICAO_CONTA_POUPANCA = "Conta Poupanca";
    public static final String DESCRICAO_CONTA_INTERNACIONAL = "Conta Internacional";

    public static final String DESCRICAO_CARTAO_CREDITO = "Cartão de Crédito";
    public static final String DESCRICAO_CARTAO_DEBITO = "Cartão de Débito";

    public static final String DESCRICAO_MOEDA_BRL = "Real Brasileiro";
    public static final String SIMBOLO_MOEDA_BRL = "R$";
    public static final String DESCRICAO_MOEDA_USD = "Dólar Americano";
    public static final String SIMBOLO_MOEDA_USD = "US$";
    public static final String DESCRICAO_MOEDA_EUR = "Euro";
    public static final String SIMBOLO_MOEDA_EUR = "€";

    public static final String DESCRICAO_CATEGORIA_COMUM = "Comum";
    public static final String DESCRICAO_CATEGORIA_SUPER = "Super";
    public static final String DESCRICAO_CATEGORIA_PREMIUM = "Premium";

    // Nomes - TipoSeguro
    public static final String NOME_SEGURO_VIAGEM = "Seguro de Viagem";
    public static final String NOME_SEGURO_FRAUDE = "Seguro de Fraude";

    // Receita Federal
    public static final String SIMULANDO_CONSULTA_CPF = "Simulando consulta de CPF na Receita Federal";
    public static final String STATUS_ATIVO = "ATIVO";
    public static final String CPF_VALIDO_ATIVO = "CPF válido e ativo";
    public static final String ERRO_CONSULTA_CPF = "Erro ao consultar CPF na API que simula a da Receita Federal";

    // Validações
    public static final String CPF_JA_CADASTRADO = "CPF já cadastrado no sistema.";
    public static final String CLIENTE_MAIOR_18 = "Cliente deve ser maior de 18 anos para se cadastrar.";
    public static final String CARTAO_INATIVO_OPERACAO_BLOQUEADA = "Cartão desativado - operação bloqueada";
    public static final String CARTAO_FATURA_ABERTA = "Cartão não pode ser desativado com fatura em aberto.";
    public static final String SENHA_INCORRETA = "A senha informada está incorreta!";
    public static final String LIMITE_INSUFICIENTE = "Limite insuficiente para esta transação. Limite atual: %s";
    public static final String CARTAO_SEGUROS_VINCULADOS = "Cartão não pode ser excluído com seguros vinculados.";

    // CEP
    public static final String CEP_INVALIDO = "CEP inválido: valor nulo ou vazio.";
    public static final String ERRO_CEP_INVALIDO = "CEP inválido ou erro inesperado";
    public static final String ERRO_COMUNICACAO_BRASIL_API = "Erro ao comunicar com a BrasilAPI. Usando fallback. Erro: {}";

    // Fallback
    public static final String INICIO_FALLBACK_CEP = "Usando fallback para o CEP.";
    public static final String FALLBACK_RUA = "Rua não disponível";
    public static final String FALLBACK_BAIRRO = "Bairro não disponível";
    public static final String FALLBACK_CIDADE = "Cidade não disponível";
    public static final String FALLBACK_ESTADO = "Estado não disponível";

    // SecurityService
    public static final String INICIO_VERIFICACAO_ADMIN = "Verificando se usuário ID: {} é ADMIN.";
    public static final String RESULTADO_VERIFICACAO_ADMIN = "Usuário ID: {} é ADMIN? {}";
    public static final String INICIO_VERIFICACAO_OWNER = "Verificando se usuário ID: {} é proprietário do cliente ID: {}.";
    public static final String RESULTADO_VERIFICACAO_OWNER = "Usuário ID: {} é proprietário do cliente ID: {}? {}";
    public static final String INICIO_VALIDACAO_ACESSO = "Validando acesso do usuário ID: {} ao cliente ID: {}.";
    public static final String ACESSO_NEGADO_LOG = "Acesso negado para usuário ID: {} ao cliente ID: {}.";
    public static final String ACESSO_PERMITIDO_LOG = "Acesso permitido para usuário ID: {} ao cliente ID: {}.";

    // AdminService
    public static final String USUARIO_CRIADO_SUCESSO = "Usuário criado: ID {}";
    public static final String CLIENTE_CRIADO = "Cliente criado";
    public static final String VALIDACAO_CLIENTE_CONCLUIDA = "Validação do cliente concluída";
    public static final String ERRO_SALVAR_CLIENTE_BANCO = "Erro ao salvar cliente no banco: ID {}";
    public static final String DADOS_CEP_SUCESSO = "Dados do CEP encontrados com sucesso";
    public static final String ERRO_BUSCAR_CEP_BRASILAPI = "Erro ao buscar CEP na BrasilAPI: {}";
    public static final String ERRO_BUSCAR_CEP_MENSAGEM_EXCEPTION = "Erro ao consultar CEP. Tente novamente mais tarde.";
    public static final String ACESSO_VALIDADO = "Acesso validado";
    public static final String ACESSO_VALIDADO_USUARIO = "Acesso validado para usuário ID {}";
    public static final String CONTA_CRIADA = "Conta criada: ID {}";
    public static final String CONTA_SALVA_BANCO = "Conta salva no banco de dados: ID {}";
    public static final String ERRO_ABRIR_CONTA = "Erro ao abrir conta: {}";
    public static final String ERRO_ABRIR_CONTA_MENSAGEM_EXCEPTION = "Erro ao abrir conta. Tente novamente mais tarde.";
    public static final String CARTAO_CRIADO = "Cartão criado: ID {}";
    public static final String CARTAO_SALVO_BANCO = "Cartão salvo no banco de dados: ID {}";
    public static final String ERRO_SALVAR_CARTAO_BANCO = "Erro ao salvar o cartão no banco de dados";
    public static final String ERRO_SALVAR_CARTAO_BANCO_MENSAGEM_EXCEPTION = "Erro ao salvar cartão. Tente novamente mais tarde.";
    public static final String SEGURO_CRIADO = "Seguro criado: ID {}";
    public static final String SEGURO_SALVO_BANCO = "Seguro salvo no banco de dados: ID {}";
    public static final String ERRO_CONTRATAR_SEGURO = "Erro ao contratar seguro: {}";
    public static final String ERRO_CONTRATAR_SEGURO_MENSAGEM_EXCEPTION = "Erro ao contratar seguro. Tente novamente mais tarde.";
    public static final String BUSCANDO_CLIENTE_ID = "Buscando cliente por ID: {}";
    public static final String CRIANDO_USUARIO = "Criando usuário";
    public static final String CRIANDO_CLIENTE = "Criando cliente";
    public static final String SALVANDO_CLIENTE_BANCO = "Salvando cliente no banco de dados";
    public static final String SALVANDO_ENDERECO_CLIENTE = "Salvando endereço para o cliente";
    public static final String CPF_INVALIDO_RECEITA_FEDERAL = "CPF inválido ou inativo na Receita Federal";
    public static final String CPF_VALIDO_UNICO = "Cpf válido e único";
    public static final String CLIENTE_MAIOR_IDADE = "Cliente maior de idade";
    public static final String VERIFICANDO_POLITICA_TAXAS = "Verificando política de taxas";
    public static final String POLITICA_TAXAS_ENCONTRADA = "Política de taxas encontrada";
}
