package br.com.cdb.bancodigital.utils;

import br.com.cdb.bancodigital.model.enums.TipoConta;

import java.util.Random;
import java.util.UUID;

public class NumberGenerator {

    public static String gerarNumeroConta(TipoConta tipoConta) {
        return switch (tipoConta) {
            case CORRENTE -> {
                yield "CC-" + (1000 + (int) (Math.random() * 9000));
            }
            case POUPANCA -> {
                yield "CP-" + (5000 + (int)(Math.random() *9000));
            }
            case INTERNACIONAL -> {
                yield "CI-" + (10000 + (int) (Math.random() * 9000));
            }
        };
    }

    public static String gerarNumeroCartao() {
        Random random = new Random();
        StringBuilder numeroCartao = new StringBuilder();

        // Define o primeiro dígito (Visa = 4, Mastercard = 5)
        int primeiroDigito = random.nextBoolean() ? 4 : 5;
        numeroCartao.append(primeiroDigito);

        // Gera os outros 15 dígitos
        for (int i = 1; i < 16; i++) {
            numeroCartao.append(random.nextInt(10)); // Números de 0 a 9
        }

        // Aplica a formatação para "XXXX XXXX XXXX XXXX"
        return numeroCartao.toString().replaceAll("(.{4})", "$1 ").trim();
    }

    public static String gerarNumeroApolice() {
        return UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
