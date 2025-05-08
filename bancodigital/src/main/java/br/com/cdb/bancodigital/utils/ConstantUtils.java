package br.com.cdb.bancodigital.utils;

public class ConstantUtils {

    private ConstantUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static final String ERRO_CLIENTE_NAO_ENCONTRADO = "Cliente ID %d não encontrado.";

    public static final String ERRO_CONEXAO_BANCO = "Erro ao conectar ao banco de dados";

    public static final String USUARIO_LOGADO = "Usuário logado: ID: {}";

    public static final String CLIENTE_ENCONTRADO = "Cliente encontrado com sucesso";

    public static final String CARTAO_ENCONTRADO = "Cartão(ões) encontrado(s) com sucesso";

}
