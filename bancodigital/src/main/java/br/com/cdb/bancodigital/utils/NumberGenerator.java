package br.com.cdb.bancodigital.utils;

import br.com.cdb.bancodigital.model.enums.TipoConta;

import java.security.SecureRandom;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class NumberGenerator {

    private NumberGenerator() {
        throw new IllegalStateException("Utility class");
    }

    public static String gerarNumeroConta(TipoConta tipoConta) {
        SecureRandom secureRandom = new SecureRandom();
        return switch (tipoConta) {
            case CORRENTE -> {
                yield "CC-" + (1000 + secureRandom.nextInt(9000));
            }
            case POUPANCA -> {
                yield "CP-" + (5000 + secureRandom.nextInt(9000));
            }
            case INTERNACIONAL -> {
                yield "CI-" + (10000 + secureRandom.nextInt(9000));
            }
        };
    }

    public static String gerarNumeroCartao() {
        SecureRandom secureRandom = new SecureRandom();
        StringBuilder numeroCartao = new StringBuilder();

        // Define o primeiro dígito (Visa = 4, Mastercard = 5)
        int primeiroDigito = secureRandom.nextBoolean() ? 4 : 5;
        numeroCartao.append(primeiroDigito);

        // Gera os outros 15 dígitos
        for (int i = 1; i < 16; i++) {
            numeroCartao.append(secureRandom.nextInt(10)); // Números de 0 a 9
        }

        // Aplica a formatação para "XXXX XXXX XXXX XXXX"
        return numeroCartao.toString().replaceAll("(.{4})", "$1 ").trim();
    }

    private static final Set<String> numerosApoliceGerados = new HashSet<>();

    public static String gerarNumeroApolice() {
        String numeroApolice;
        do {
            numeroApolice = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        } while (!numerosApoliceGerados.add(numeroApolice)); // Gera até encontrar um número único
        return numeroApolice;

    }
}
