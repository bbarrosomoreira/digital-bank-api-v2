package br.com.cdb.bancodigital.application.core.service.conta;

import br.com.cdb.bancodigital.application.core.domain.dto.response.*;
import br.com.cdb.bancodigital.application.core.domain.entity.Cliente;
import br.com.cdb.bancodigital.application.core.domain.entity.Conta;
import br.com.cdb.bancodigital.application.core.domain.entity.Usuario;
import br.com.cdb.bancodigital.application.core.domain.entity.enums.CategoriaCliente;
import br.com.cdb.bancodigital.application.core.domain.entity.enums.Moeda;
import br.com.cdb.bancodigital.application.core.domain.entity.enums.Role;
import br.com.cdb.bancodigital.application.core.domain.entity.enums.TipoConta;
import br.com.cdb.bancodigital.application.port.in.SecurityUseCase;
import br.com.cdb.bancodigital.application.port.out.repository.ContaRepository;
import br.com.cdb.bancodigital.config.exceptions.custom.InvalidInputParameterException;
import br.com.cdb.bancodigital.config.exceptions.custom.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransacoesServiceTest {

    @InjectMocks
    private TransacoesService transacoesService;

    @Mock
    private ContaRepository contaRepository;

    @Mock
    private SecurityUseCase securityUseCase;

    private Usuario usuarioLogado;
    private Cliente cliente;
    private Conta conta;
    private Conta contaDestino;


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
        contaDestino = new Conta(
                6L,
                TipoConta.POUPANCA,
                "CP-8981",
                BigDecimal.valueOf(108.97),
                Moeda.BRL,
                cliente,
                java.time.LocalDate.of(2025, 5, 29),
                null,
                null,
                BigDecimal.valueOf(0.0090));
    }

    // transferir
    @Test
    void devePermitirTransferenciaQuandoContaValidaSaldoSuficienteEAcessoAutorizado() {
        BigDecimal valorTransferencia = new BigDecimal("10.00");

        when(contaRepository.findById(conta.getId())).thenReturn(Optional.of(conta));
        when(contaRepository.findById(contaDestino.getId())).thenReturn(Optional.of(contaDestino));
        doNothing().when(securityUseCase).validateAccess(usuarioLogado, cliente);

        // Act
        TransferenciaResponse response = transacoesService.transferir(conta.getId(), usuarioLogado, contaDestino.getId(), valorTransferencia);

        // Assert
        assertNotNull(response);
        assertEquals(valorTransferencia, response.valor());
        assertEquals(new BigDecimal("86.00"), conta.getSaldo());
        assertEquals(new BigDecimal("118.97"), contaDestino.getSaldo());
        verify(contaRepository).save(conta);
        verify(contaRepository).save(contaDestino);
    }
    @Test
    void deveLancarExcecaoQuandoContaOrigemNaoEncontradaENaoTransferir() {
        // Arrange
        Long idConta = 999L;
        BigDecimal valorTransferencia = new BigDecimal("10.00");

        when(contaRepository.findById(idConta)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            transacoesService.transferir(idConta, usuarioLogado, contaDestino.getId(), valorTransferencia);
        });

        assertEquals("Conta ID 999 não encontrada.", exception.getMessage());
    }
    @Test
    void deveLancarExcecaoQuandoContaDestinoNaoEncontradaENaoTransferir() {
        // Arrange
        Long idContaDestino = 999L;
        BigDecimal valorTransferencia = new BigDecimal("10.00");

        when(contaRepository.findById(conta.getId())).thenReturn(Optional.of(conta));
        when(contaRepository.findById(idContaDestino)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            transacoesService.transferir(conta.getId(), usuarioLogado, idContaDestino, valorTransferencia);
        });

        assertEquals("Conta ID 999 não encontrada.", exception.getMessage());
    }
    @Test
    void deveLancarExcecaoQuandoAcessoNaoAutorizadoENaoTransferir() {
        BigDecimal valorTransferencia = new BigDecimal("10.00");

        when(contaRepository.findById(conta.getId())).thenReturn(Optional.of(conta));
        when(contaRepository.findById(contaDestino.getId())).thenReturn(Optional.of(contaDestino));

        when(contaRepository.findById(conta.getId())).thenReturn(Optional.of(conta));
        doThrow(new AccessDeniedException("Você não tem permissão para acessar este recurso.")).when(securityUseCase)
                .validateAccess(usuarioLogado, cliente);

        AccessDeniedException exception = assertThrows(AccessDeniedException.class, () -> {
            transacoesService.transferir(conta.getId(), usuarioLogado, contaDestino.getId(), valorTransferencia);
        });

        assertEquals("Você não tem permissão para acessar este recurso.", exception.getMessage());
    }
    @Test
    void deveLancarExcecaoQuandoSaldoInsuficienteENaoTransferir() {
        BigDecimal valorTransferencia = new BigDecimal("1000.00");

        when(contaRepository.findById(conta.getId())).thenReturn(Optional.of(conta));
        when(contaRepository.findById(contaDestino.getId())).thenReturn(Optional.of(contaDestino));
        doNothing().when(securityUseCase).validateAccess(usuarioLogado, cliente);

        InvalidInputParameterException exception = assertThrows(InvalidInputParameterException.class, () -> {
            transacoesService.transferir(conta.getId(), usuarioLogado, contaDestino.getId(), valorTransferencia);
        });

        assertEquals("Saldo insuficiente para esta transação.", exception.getMessage());
    }

    // pix
    @Test
    void devePermitirPixQuandoContaValidaSaldoSuficienteEAcessoAutorizado() {
        BigDecimal valorTransferencia = new BigDecimal("10.00");

        when(contaRepository.findById(conta.getId())).thenReturn(Optional.of(conta));
        when(contaRepository.findById(contaDestino.getId())).thenReturn(Optional.of(contaDestino));
        doNothing().when(securityUseCase).validateAccess(usuarioLogado, cliente);

        // Act
        PixResponse response = transacoesService.pix(conta.getId(), usuarioLogado, contaDestino.getId(), valorTransferencia);

        // Assert
        assertNotNull(response);
        assertEquals(valorTransferencia, response.valor());
        assertEquals(new BigDecimal("86.00"), conta.getSaldo());
        assertEquals(new BigDecimal("118.97"), contaDestino.getSaldo());
        verify(contaRepository).save(conta);
        verify(contaRepository).save(contaDestino);
    }
    @Test
    void deveLancarExcecaoQuandoContaOrigemNaoEncontradaENaoFazerPix() {
        // Arrange
        Long idConta = 999L;
        BigDecimal valorPix = new BigDecimal("10.00");

        when(contaRepository.findById(idConta)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            transacoesService.pix(idConta, usuarioLogado, contaDestino.getId(), valorPix);
        });

        assertEquals("Conta ID 999 não encontrada.", exception.getMessage());
    }
    @Test
    void deveLancarExcecaoQuandoContaDestinoNaoEncontradaENaoFazerPix() {
        // Arrange
        Long idContaDestino = 999L;
        BigDecimal valorPix = new BigDecimal("10.00");

        when(contaRepository.findById(conta.getId())).thenReturn(Optional.of(conta));
        when(contaRepository.findById(idContaDestino)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            transacoesService.pix(conta.getId(), usuarioLogado, idContaDestino, valorPix);
        });

        assertEquals("Conta ID 999 não encontrada.", exception.getMessage());
    }
    @Test
    void deveLancarExcecaoQuandoAcessoNaoAutorizadoENaoFazerPix() {
        BigDecimal valorPix = new BigDecimal("10.00");

        when(contaRepository.findById(conta.getId())).thenReturn(Optional.of(conta));
        when(contaRepository.findById(contaDestino.getId())).thenReturn(Optional.of(contaDestino));

        when(contaRepository.findById(conta.getId())).thenReturn(Optional.of(conta));
        doThrow(new AccessDeniedException("Você não tem permissão para acessar este recurso.")).when(securityUseCase)
                .validateAccess(usuarioLogado, cliente);

        AccessDeniedException exception = assertThrows(AccessDeniedException.class, () -> {
            transacoesService.pix(conta.getId(), usuarioLogado, contaDestino.getId(), valorPix);
        });

        assertEquals("Você não tem permissão para acessar este recurso.", exception.getMessage());
    }
    @Test
    void deveLancarExcecaoQuandoSaldoInsuficienteENaoFazerPix() {
        BigDecimal valorPix = new BigDecimal("1000.00");

        when(contaRepository.findById(conta.getId())).thenReturn(Optional.of(conta));
        when(contaRepository.findById(contaDestino.getId())).thenReturn(Optional.of(contaDestino));
        doNothing().when(securityUseCase).validateAccess(usuarioLogado, cliente);

        InvalidInputParameterException exception = assertThrows(InvalidInputParameterException.class, () -> {
            transacoesService.transferir(conta.getId(), usuarioLogado, contaDestino.getId(), valorPix);
        });

        assertEquals("Saldo insuficiente para esta transação.", exception.getMessage());
    }

    // get saldo
    @Test
    void deveRetornarSaldoQuandoContaValidaEAcessoAutorizado() {
        // Arrange
        when(contaRepository.findById(conta.getId())).thenReturn(Optional.of(conta));
        doNothing().when(securityUseCase).validateAccess(usuarioLogado, cliente);

        // Act
        SaldoResponse response = transacoesService.getSaldo(conta.getId(), usuarioLogado);

        // Assert
        assertNotNull(response);
        assertEquals(conta.getSaldo(), response.saldo());
    }

    // depositar
    @Test
    void devePermitirDepositoQuandoContaValidaEValorValido() {
        // Arrange
        BigDecimal valorDeposito = new BigDecimal("10.00");

        when(contaRepository.findById(conta.getId())).thenReturn(Optional.of(conta));

        // Act
        DepositoResponse response = transacoesService.depositar(conta.getId(), valorDeposito);

        // Assert
        assertNotNull(response);
        assertEquals(new BigDecimal("106.00"), conta.getSaldo());
        verify(contaRepository).save(conta);
    }
    @Test
    void deveLancarExcecaoQuandoContaNaoEncontradaENaoDepositar() {
        // Arrange
        Long idConta = 999L;
        BigDecimal valorDeposito = new BigDecimal("10.00");

        when(contaRepository.findById(idConta)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            transacoesService.depositar(idConta, valorDeposito);
        });

        assertEquals("Conta ID 999 não encontrada.", exception.getMessage());
    }
//    @Test
//    void deveLancarExcecaoQuandoValorDepositoInvalidoENaoDepositar() {
//        BigDecimal valorDeposito = new BigDecimal("-10.00");
//
//        when(contaRepository.findById(conta.getId())).thenReturn(Optional.of(conta));
//        InvalidInputParameterException exception = assertThrows(InvalidInputParameterException.class, () ->
//        {
//            transacoesService.depositar(conta.getId(), valorDeposito);
//        });
//        assertEquals("Valor de depósito inválido.", exception.getMessage());
//    }

    // sacar
    @Test
    void devePermitirSaqueQuandoContaValidaSaldoSuficienteEAcessoAutorizado() {
        // Arrange
        BigDecimal valorSaque = new BigDecimal("10.00");

        when(contaRepository.findById(conta.getId())).thenReturn(Optional.of(conta));
        doNothing().when(securityUseCase).validateAccess(usuarioLogado, cliente);

        // Act
        SaqueResponse response = transacoesService.sacar(conta.getId(), usuarioLogado, valorSaque);

        // Assert
        assertNotNull(response);
        assertEquals(valorSaque, response.valor());
        assertEquals(new BigDecimal("86.00"), conta.getSaldo());
        verify(contaRepository).save(conta);

    }
    @Test
    void deveLancarExcecaoQuandoContaNaoEncontradaENaoSacar() {
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
    @Test
    void deveLancarExcecaoQuandoAcessoNaoAutorizadoENaoSacar() {
        BigDecimal valorSaque = new BigDecimal("10.00");

        when(contaRepository.findById(conta.getId())).thenReturn(Optional.of(conta));
        doThrow(new AccessDeniedException("Você não tem permissão para acessar este recurso.")).when(securityUseCase)
                .validateAccess(usuarioLogado, cliente);

        AccessDeniedException exception = assertThrows(AccessDeniedException.class, () -> {
            transacoesService.sacar(conta.getId(), usuarioLogado, valorSaque);
        });

        assertEquals("Você não tem permissão para acessar este recurso.", exception.getMessage());
    }
    @Test
    void deveLancarExcecaoQuandoSaldoInsuficienteENaoSacar() {
        BigDecimal valorSaque = new BigDecimal("1000.00");

        when(contaRepository.findById(conta.getId())).thenReturn(Optional.of(conta));
        doNothing().when(securityUseCase).validateAccess(usuarioLogado, cliente);

        InvalidInputParameterException exception = assertThrows(InvalidInputParameterException.class, () -> {
            transacoesService.sacar(conta.getId(), usuarioLogado, valorSaque);
        });

        assertEquals("Saldo insuficiente para esta transação.", exception.getMessage());
    }

    // debitar tarifa manutenção
    @Test
    void deveDebitarTarifaManutencaoQuandoContaValidaESaldoSuficiente() {
        // Arrange
        when(contaRepository.findById(conta.getId())).thenReturn(Optional.of(conta));

        // Act
        AplicarTxManutencaoResponse response = transacoesService.debitarTarifaManutencao(conta.getId());

        // Assert
        assertNotNull(response);
        assertEquals(conta.getTarifaManutencao(), response.valor());
        assertEquals(conta.getSaldo(), response.saldo());
        verify(contaRepository).save(conta);
    }
    @Test
    void deveLancarExcecaoQuandoSaldoInsuficienteENaoDebitar() {
        conta.setSaldo(BigDecimal.ZERO);

        when(contaRepository.findById(conta.getId())).thenReturn(Optional.of(conta));

        InvalidInputParameterException exception = assertThrows(InvalidInputParameterException.class, () -> {
            transacoesService.debitarTarifaManutencao(conta.getId());
        });

        assertEquals("Saldo insuficiente para esta transação.", exception.getMessage());
    }

    // creditar rendimento
    @Test
    void deveCreditarRendimentoQuandoContaValida() {
        // Arrange
        when(contaRepository.findById(contaDestino.getId())).thenReturn(Optional.of(contaDestino));

        // Act
        AplicarTxRendimentoResponse response = transacoesService.creditarRendimento(contaDestino.getId());

        // Assert
        assertNotNull(response);
        assertEquals(contaDestino.getSaldo(), response.getSaldo());
        verify(contaRepository).save(contaDestino);
    }

}
