package br.com.cdb.bancodigital.application.domain.entity;

import br.com.cdb.bancodigital.application.core.domain.entity.Conta;
import org.junit.jupiter.api.*;

import java.math.BigDecimal;

class ContaTest {

    private Conta conta;

    @BeforeAll
    static void iniciar() {
        System.out.println("Início da bateria de testes da Conta");
    }

    @BeforeEach
    void setup() {
        conta = Conta.criarContaParaTeste(new BigDecimal("100.00"));
    }

    @AfterEach
    public void limpar() {
        System.out.println("Teste finalizado.");
    }

    @AfterAll
    public static void encerrar() {
        System.out.println("Todos os testes de Conta finalizados.");
    }

    @Test
    void devePermitirSaqueQuandoValorDeSaqueForPositivoEMenorQueSaldo() {
        // Implementar teste para verificar se o saldo é suficiente para saque
        Assertions.assertTrue(conta.podeSacar(new BigDecimal("40.00")));
    }

    @Test
    void naoDevePermitirSacarValorNegativoOuZero() {
        // Implementar teste para verificar se o saque de valor negativo ou zero é proibido
        Assertions.assertFalse(conta.podeSacar(new BigDecimal("-10.00")));
        Assertions.assertFalse(conta.podeSacar(BigDecimal.ZERO));
    }

    @Test
    void naoDevePermitirSacarValorMaiorQueSaldo() {
        // Implementar teste para verificar se o saque de valor maior que o saldo é proibido
        Assertions.assertFalse(conta.podeSacar(new BigDecimal("600.00")));
    }
}
