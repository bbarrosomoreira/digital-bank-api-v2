package br.com.cdb.bancodigital.utils;

import br.com.cdb.bancodigital.application.core.domain.entity.enums.TipoConta;

import java.security.SecureRandom;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class NumberGenerator {

    private NumberGenerator() {
        throw new IllegalStateException("Utility class");
    }

    private static final Set<String> numerosContaGerados = new HashSet<>();
    private static final Set<String> numerosCartaoGerados = new HashSet<>();
    private static final Set<String> numerosApoliceGerados = new HashSet<>();

    public static String gerarNumeroConta(TipoConta tipoConta) {
        SecureRandom secureRandom = new SecureRandom();
        String numeroConta;
        do {
            numeroConta = switch (tipoConta) {
                case CORRENTE -> "CC-" + (1000 + secureRandom.nextInt(9000));
                case POUPANCA -> "CP-" + (5000 + secureRandom.nextInt(9000));
                case INTERNACIONAL -> "CI-" + (10000 + secureRandom.nextInt(9000));
            };
        } while (!numerosContaGerados.add(numeroConta)); // Gera até encontrar um número único
        return numeroConta;
    }

    public static String gerarNumeroCartao() {
        SecureRandom secureRandom = new SecureRandom();
        String numeroCartaoFormatado;
        String numeroCartao;
        do {
            StringBuilder sb = new StringBuilder();
            int primeiroDigito = secureRandom.nextBoolean() ? 4 : 5;
            sb.append(primeiroDigito);
            for (int i = 1; i < 16; i++) {
                sb.append(secureRandom.nextInt(10));
            }
            numeroCartao = sb.toString();
            numeroCartaoFormatado = numeroCartao.replaceAll("(.{4})", "$1 ").trim();
        } while (!numerosCartaoGerados.add(numeroCartao)); // Gera até encontrar um número único
        return numeroCartaoFormatado;
    }

    public static String gerarNumeroApolice() {
        String numeroApolice;
        do {
            numeroApolice = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        } while (!numerosApoliceGerados.add(numeroApolice)); // Gera até encontrar um número único
        return numeroApolice;
    }
}
