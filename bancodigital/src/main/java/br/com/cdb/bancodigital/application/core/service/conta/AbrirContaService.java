package br.com.cdb.bancodigital.application.core.service.conta;

import br.com.cdb.bancodigital.application.core.domain.dto.response.ContaResponse;
import br.com.cdb.bancodigital.application.core.domain.model.Cliente;
import br.com.cdb.bancodigital.application.core.domain.model.Conta;
import br.com.cdb.bancodigital.application.core.domain.model.PoliticaDeTaxas;
import br.com.cdb.bancodigital.application.core.domain.model.Usuario;
import br.com.cdb.bancodigital.application.core.domain.model.enums.Moeda;
import br.com.cdb.bancodigital.application.core.domain.model.enums.TipoConta;
import br.com.cdb.bancodigital.application.port.in.conta.AbrirContaUseCase;
import br.com.cdb.bancodigital.application.port.out.api.ConversorMoedasPort;
import br.com.cdb.bancodigital.application.port.out.repository.ClienteRepository;
import br.com.cdb.bancodigital.application.port.out.repository.ContaRepository;
import br.com.cdb.bancodigital.application.port.out.repository.PoliticaDeTaxasRepository;
import br.com.cdb.bancodigital.config.exceptions.custom.ValidationException;
import br.com.cdb.bancodigital.utils.ConstantUtils;
import br.com.cdb.bancodigital.utils.Validator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@AllArgsConstructor
@Slf4j
public class AbrirContaService implements AbrirContaUseCase {

    private final ClienteRepository clienteRepository;
    private final ContaRepository contaRepository;
    private final PoliticaDeTaxasRepository politicaDeTaxasRepository;
    private final ConversorMoedasPort conversorMoedasPort;

    @Transactional
    public ContaResponse abrirConta(Long id_cliente, Usuario usuarioLogado, TipoConta tipo, Moeda moeda, BigDecimal valorDeposito) {
        log.info(ConstantUtils.INICIO_ABERTURA_CONTA);
        Cliente cliente = Validator.verificarClienteExistente(clienteRepository, id_cliente);
        log.info(ConstantUtils.CLIENTE_ENCONTRADO);
        securityService.validateAccess(usuarioLogado, cliente);
        log.info(ConstantUtils.ACESSO_VALIDADO);
        Conta contaNova = criarContaPorTipo(tipo, cliente, moeda, valorDeposito);
        log.info(ConstantUtils.CONTA_CRIADA, contaNova.getId());
        try {
            contaRepository.save(contaNova);
            log.info(ConstantUtils.CONTA_SALVA_BANCO, contaNova.getId());
        } catch (DataAccessException e) {
            log.error(ConstantUtils.ERRO_SALVAR_CONTA, e.getMessage());
            throw new ValidationException(ConstantUtils.ERRO_ABRIR_CONTA_MENSAGEM_EXCEPTION);
        }
        log.info(ConstantUtils.SUCESSO_ABERTURA_CONTA, id_cliente);
        return toResponse(contaNova);
    }
    private Conta criarContaPorTipo(TipoConta tipo, Cliente cliente, Moeda moeda, BigDecimal valorDeposito) {
        log.info(ConstantUtils.VERIFICANDO_POLITICA_TAXAS);
        PoliticaDeTaxas parametros = Validator.verificarPoliticaExitente(politicaDeTaxasRepository, cliente.getCategoria());
        log.info(ConstantUtils.POLITICA_TAXAS_ENCONTRADA);

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
        BigDecimal saldoMoedaExtrangeira = conversorMoedasPort.converterDeBrl(ci.getMoeda(), ci.getSaldoEmReais());
        ci.setSaldo(saldoMoedaExtrangeira);
        return ci;
    }
    private ContaResponse toResponse(Conta conta) {
        BigDecimal tarifa;
        switch (conta.getTipoConta()) {
            case CORRENTE, INTERNACIONAL -> {
                tarifa = conta.getTarifaManutencao();
            }
            case POUPANCA -> {
                tarifa = conta.getTaxaRendimento();
            }
            default -> throw new IllegalStateException(ConstantUtils.VALOR_INESPERADO + conta.getTipoConta());
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
