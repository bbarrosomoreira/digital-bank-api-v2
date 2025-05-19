package br.com.cdb.bancodigital.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import br.com.cdb.bancodigital.model.*;
import br.com.cdb.bancodigital.utils.ConstantUtils;
import br.com.cdb.bancodigital.utils.Validator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.cdb.bancodigital.model.Conta;
import br.com.cdb.bancodigital.model.enums.Moeda;
import br.com.cdb.bancodigital.model.enums.TipoConta;
import br.com.cdb.bancodigital.exceptions.custom.ValidationException;
import br.com.cdb.bancodigital.dao.CartaoDAO;
import br.com.cdb.bancodigital.dao.ClienteDAO;
import br.com.cdb.bancodigital.dao.ContaDAO;
import br.com.cdb.bancodigital.dao.PoliticaDeTaxasDAO;
import br.com.cdb.bancodigital.dto.response.AplicarTxManutencaoResponse;
import br.com.cdb.bancodigital.dto.response.AplicarTxRendimentoResponse;
import br.com.cdb.bancodigital.dto.response.ContaResponse;
import br.com.cdb.bancodigital.dto.response.DepositoResponse;
import br.com.cdb.bancodigital.dto.response.PixResponse;
import br.com.cdb.bancodigital.dto.response.SaldoResponse;
import br.com.cdb.bancodigital.dto.response.SaqueResponse;
import br.com.cdb.bancodigital.dto.response.TransferenciaResponse;

@Service
@AllArgsConstructor
@Slf4j
public class ContaService {

    private final ContaDAO contaDAO;
    private final ClienteDAO clienteDAO;
    private final CartaoDAO cartaoDAO;
    private final PoliticaDeTaxasDAO politicaDeTaxaDAO;
    private final ConversorMoedasService conversorMoedasService;
    private final SecurityService securityService;

    // addConta de forma genérica
    @Transactional
    public ContaResponse abrirConta(Long id_cliente, Usuario usuarioLogado, TipoConta tipo, Moeda moeda, BigDecimal valorDeposito) {
        Objects.requireNonNull(tipo, "Tipo de conta não pode ser nulo");

        Cliente cliente = Validator.verificarClienteExistente(clienteDAO, id_cliente);
        securityService.validateAccess(usuarioLogado, cliente);
        Conta contaNova = criarContaPorTipo(tipo, cliente, moeda, valorDeposito);
        contaDAO.salvar(contaNova);

        return toResponse(contaNova);
    }

    private Conta criarContaPorTipo(TipoConta tipo, Cliente cliente, Moeda moeda, BigDecimal valorDeposito) {
        PoliticaDeTaxas parametros = Validator.verificarPoliticaExitente(politicaDeTaxaDAO, cliente.getCategoria());

        return switch (tipo) {
            case CORRENTE -> {
                yield criarContaCorrente(cliente, tipo, moeda, valorDeposito, parametros);
            }
            case POUPANCA -> {
                yield criarContaPoupanca(cliente, tipo, moeda, valorDeposito, parametros);
            }
            case INTERNACIONAL -> {
                yield criarContaInternacional(cliente, tipo, moeda, valorDeposito, parametros);
            }
        };
    }

    private Conta criarContaCorrente(Cliente cliente, TipoConta tipo, Moeda moeda, BigDecimal valorDeposito, PoliticaDeTaxas parametros) {
        Conta cc = new Conta(cliente, tipo);
        cc.setTarifaManutencao(parametros.getTarifaManutencaoMensalContaCorrente());
        cc.setMoeda(moeda);
        cc.setSaldo(valorDeposito);
        return cc;
    }

    private Conta criarContaPoupanca(Cliente cliente, TipoConta tipo, Moeda moeda, BigDecimal valorDeposito, PoliticaDeTaxas parametros) {
        Conta cp = new Conta(cliente, tipo);
        cp.setTaxaRendimento(parametros.getRendimentoPercentualMensalContaPoupanca());
        cp.setMoeda(moeda);
        cp.setSaldo(valorDeposito);
        return cp;
    }

    private Conta criarContaInternacional(Cliente cliente, TipoConta tipo, Moeda moeda, BigDecimal valorDeposito, PoliticaDeTaxas parametros) {
        Conta ci = new Conta(cliente, tipo);
        ci.setTarifaManutencao(parametros.getTarifaManutencaoContaInternacional());
        ci.setMoeda(moeda);
        ci.setSaldoEmReais(valorDeposito);
        BigDecimal saldoMoedaExtrangeira = conversorMoedasService.converterDeBrl(ci.getMoeda(), ci.getSaldoEmReais());
        ci.setSaldo(saldoMoedaExtrangeira);
        return ci;
    }

    // get contas
    public List<ContaResponse> getContas() {
        List<Conta> contas = contaDAO.buscarTodasContas();
        return contas.stream().map(this::toResponse).toList();
    }

    // get conta por usuário
    public List<ContaResponse> listarPorUsuario(Usuario usuarioLogado) {
        List<Conta> contas = contaDAO.buscarContaPorClienteUsuario(usuarioLogado);
        return contas.stream().map(this::toResponse).toList();
    }

    // get conta por cliente
    public List<ContaResponse> listarPorCliente(Long id_cliente, Usuario usuarioLogado) {
        Cliente cliente = Validator.verificarClienteExistente(clienteDAO, id_cliente);
        securityService.validateAccess(usuarioLogado, cliente);
        List<Conta> contas = contaDAO.buscarContaPorClienteId(id_cliente);
        return contas.stream().map(this::toResponse).toList();
    }

    // get uma conta
    public ContaResponse getContaById(Long id_conta, Usuario usuarioLogado) {
        Conta conta = Validator.verificarContaExistente(contaDAO, id_conta);
        Cliente cliente = conta.getCliente();
        securityService.validateAccess(usuarioLogado, cliente);
        return toResponse(conta);
    }

    // deletar contas de cliente
    @Transactional
    public void deleteContasByCliente(Long id_cliente) {
        List<Conta> contas = contaDAO.buscarContaPorClienteId(id_cliente);
        if (contas.isEmpty()) {
            log.info("Cliente Id {} não possui contas.", id_cliente);
            return;
        }
        for (Conta conta : contas) {
            try {
                Validator.verificarCartoesVinculados(cartaoDAO, conta);
                Validator.verificarSaldoRemanescente(conta);
                Long id = conta.getId();
                contaDAO.deletarContaPorId(conta.getId());
                log.info("Conta ID {} deletado com sucesso", id);

            } catch (DataIntegrityViolationException e) {
                log.error("Falha ao deletar conta ID {}", conta.getId(), e);
                throw new ValidationException("Erro ao deletar a conta devido a uma violação de integridade de dados.");
            }
        }
    }

    // transferencia
    @Transactional
    public TransferenciaResponse transferir(Long id_contaOrigem, Usuario usuarioLogado, Long id_contaDestino, BigDecimal valor) {
        Conta origem = Validator.verificarContaExistente(contaDAO, id_contaOrigem);
        Conta destino = Validator.verificarContaExistente(contaDAO, id_contaDestino);
        Cliente cliente = origem.getCliente();
        securityService.validateAccess(usuarioLogado, cliente);
        Validator.verificarSaldoSuficiente(valor, origem.getSaldo());
        origem.transferir(destino, valor);
        contaDAO.salvar(origem);
        contaDAO.salvar(destino);
        return TransferenciaResponse.toTransferenciaResponse(origem.getNumeroConta(), destino.getNumeroConta(), valor);

        // Registrar a transação
    }

    // pix
    @Transactional
    public PixResponse pix(Long id_contaOrigem, Usuario usuarioLogado, Long id_contaDestino, BigDecimal valor) {
        Conta origem = Validator.verificarContaExistente(contaDAO, id_contaOrigem);
        Conta destino = Validator.verificarContaExistente(contaDAO, id_contaDestino);
        Cliente cliente = origem.getCliente();
        securityService.validateAccess(usuarioLogado, cliente);
        Validator.verificarSaldoSuficiente(valor, origem.getSaldo());
        origem.pix(destino, valor);
        contaDAO.salvar(origem);
        contaDAO.salvar(destino);
        return PixResponse.toPixResponse(origem.getNumeroConta(), destino.getNumeroConta(), valor);

//		// Registrar a transação
    }

    // get saldo
    public SaldoResponse getSaldo(Long id_conta, Usuario usuarioLogado) {
        Conta conta = Validator.verificarContaExistente(contaDAO, id_conta);
        Cliente cliente = conta.getCliente();
        securityService.validateAccess(usuarioLogado, cliente);
        return SaldoResponse.toSaldoResponse(conta);
    }

    // deposito
    @Transactional
    public DepositoResponse depositar(Long id_conta, BigDecimal valor) {
        Conta conta = Validator.verificarContaExistente(contaDAO, id_conta);
        conta.depositar(valor);
        contaDAO.salvar(conta);
        return DepositoResponse.toDepositoResponse(conta.getNumeroConta(), valor, conta.getSaldo());

//		// Registrar a transação

    }

    // saque
    @Transactional
    public SaqueResponse sacar(Long id_conta, Usuario usuarioLogado, BigDecimal valor) {
        Conta conta = Validator.verificarContaExistente(contaDAO, id_conta);
        Cliente cliente = conta.getCliente();
        securityService.validateAccess(usuarioLogado, cliente);
        Validator.verificarSaldoSuficiente(valor, conta.getSaldo());
        conta.sacar(valor);
        contaDAO.salvar(conta);
        return SaqueResponse.toSaqueResponse(conta.getNumeroConta(), valor, conta.getSaldo());

//		// Registrar a transação
    }

    // txmanutencao
    @Transactional
    public AplicarTxManutencaoResponse debitarTarifaManutencao(Long id_conta) {
        Conta cc = Validator.verificarContaExistente(contaDAO, id_conta);
        Validator.verificarSaldoSuficiente(cc.getTarifaManutencao(), cc.getSaldo());
        cc.aplicarTarifaManutencao();
        contaDAO.salvar(cc);
        return AplicarTxManutencaoResponse.toAplicarTxManutencaoResponse(cc.getNumeroConta(), cc.getTarifaManutencao(), cc.getSaldo());
    }

    // rendimento
    @Transactional
    public AplicarTxRendimentoResponse creditarRendimento(Long id_conta) {
        Conta cp = Validator.verificarContaExistente(contaDAO, id_conta);
        cp.aplicarRendimento();
        contaDAO.salvar(cp);
        return AplicarTxRendimentoResponse.toAplicarTxRendimentoResponse(cp.getNumeroConta(), cp.getTaxaRendimento(), cp.getSaldo());

    }

    //M
    public ContaResponse toResponse(Conta conta) {
        BigDecimal tarifa;
        switch (conta.getTipoConta()) {
            case CORRENTE, INTERNACIONAL -> {
                tarifa = conta.getTarifaManutencao();
            }
            case POUPANCA -> {
                tarifa = conta.getTaxaRendimento();
            }
            default -> throw new IllegalStateException("Unexpected value: " + conta.getTipoConta());
        }
        ContaResponse response = new ContaResponse(conta.getId(), conta.getNumeroConta(), conta.getTipoConta(),
                conta.getMoeda(), conta.getSaldo(), conta.getDataCriacao(),
                tarifa);
        if (conta.getTipoConta().equals(TipoConta.INTERNACIONAL)) {
            response.setSaldoEmReais(conta.getSaldoEmReais());
        }
        return response;
    }

}


