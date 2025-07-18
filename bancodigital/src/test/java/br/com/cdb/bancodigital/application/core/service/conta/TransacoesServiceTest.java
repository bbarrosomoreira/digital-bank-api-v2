package br.com.cdb.bancodigital.application.core.service.conta;

import br.com.cdb.bancodigital.application.core.domain.dto.response.SaqueResponse;
import br.com.cdb.bancodigital.application.core.domain.entity.Cliente;
import br.com.cdb.bancodigital.application.core.domain.entity.Conta;
import br.com.cdb.bancodigital.application.core.domain.entity.Usuario;
import br.com.cdb.bancodigital.application.core.domain.entity.enums.CategoriaCliente;
import br.com.cdb.bancodigital.application.core.domain.entity.enums.Moeda;
import br.com.cdb.bancodigital.application.core.domain.entity.enums.Role;
import br.com.cdb.bancodigital.application.core.domain.entity.enums.TipoConta;
import br.com.cdb.bancodigital.application.port.in.SecurityUseCase;
import br.com.cdb.bancodigital.application.port.out.repository.ContaRepository;
import br.com.cdb.bancodigital.config.exceptions.custom.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TransacoesServiceTest {

    @InjectMocks
    private TransacoesService transacoesService;

    @Mock
    private ContaRepository contaRepository;

    @Mock
    private SecurityUseCase securityUseCase;

    private Usuario usuarioLogado;
    private Cliente cliente;
    private Conta conta;


    @BeforeEach
    void setUp() {
        usuarioLogado = new Usuario(
                8L,
                "jdoe@gmail.com",
                "$2a$10$T4EjoSehA.vE7QJkjqiq5ORlszKwgpkFHX7dLIMw9wsifmz0AaX2C",
                Role.CLIENTE);
        cliente = new Cliente(
                5L,
                "John Doe",
                "80239451066",
                CategoriaCliente.COMUM,
                java.time.LocalDate.of(2000, 9, 16),
                usuarioLogado);
        conta = new Conta(
                2L,
                TipoConta.CORRENTE,
                "CC-6524",
                BigDecimal.valueOf(96.00),
                Moeda.BRL,
                cliente,
                java.time.LocalDate.of(2025, 5, 21),
                BigDecimal.valueOf(12),
                null,
                null);
    }

    @Test
    void devePermitirSaqueQuandoContaValidaSaldoSuficienteEAcessoAutorizado() {
        // Arrange (dados simulados)
        BigDecimal valorSaque = new BigDecimal("10.00");

        when(contaRepository.findById(conta.getId())).thenReturn(Optional.of(conta));
        doNothing().when(securityUseCase).validateAccess(usuarioLogado, cliente);

        // Act
        SaqueResponse response = transacoesService.sacar(conta.getId(), usuarioLogado, valorSaque);

        // Assert
        assertNotNull(response);
        assertEquals(valorSaque, response.valor());
        assertEquals(new BigDecimal("86.00"), conta.getSaldo());
    }

    // Mais testes aqui: conta não encontrada, saldo insuficiente, erro ao salvar...
    @Test
    void deveLancarExcecaoQuandoContaNaoEncontrada() {
        // Arrange
        Long idConta = 999L;
        BigDecimal valorSaque = new BigDecimal("10.00");

        when(contaRepository.findById(idConta)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            transacoesService.sacar(idConta, usuarioLogado, valorSaque);
        });

        assertEquals("Conta ID 999 não encontrada.", exception.getMessage());
    }


}
