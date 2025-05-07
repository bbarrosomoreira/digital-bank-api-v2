package br.com.cdb.bancodigital.service;

import java.math.BigDecimal;
import java.util.Objects;

import br.com.cdb.bancodigital.dao.*;
import br.com.cdb.bancodigital.model.*;
import br.com.cdb.bancodigital.resttemplate.BrasilApiRestTemplate;
import br.com.cdb.bancodigital.resttemplate.ReceitaFederalRestTemplate;
import br.com.cdb.bancodigital.utils.Validator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.brasilapi.api.CEP2;
import br.com.cdb.bancodigital.dto.ClienteUsuarioDTO;
import br.com.cdb.bancodigital.model.enums.CategoriaCliente;
import br.com.cdb.bancodigital.model.enums.Moeda;
import br.com.cdb.bancodigital.model.enums.TipoCartao;
import br.com.cdb.bancodigital.model.enums.TipoConta;
import br.com.cdb.bancodigital.model.enums.TipoSeguro;
import br.com.cdb.bancodigital.exceptions.ErrorMessages;
import br.com.cdb.bancodigital.exceptions.custom.InvalidInputParameterException;
import br.com.cdb.bancodigital.exceptions.custom.ResourceNotFoundException;
import br.com.cdb.bancodigital.dto.response.CartaoResponse;
import br.com.cdb.bancodigital.dto.response.ClienteResponse;
import br.com.cdb.bancodigital.dto.response.ContaResponse;
import br.com.cdb.bancodigital.dto.response.SeguroResponse;

@Service
@AllArgsConstructor
@Slf4j
public class AdminService {

    private final UsuarioDAO usuarioDAO;
    private final ClienteDAO clienteDAO;
    private final EnderecoClienteDAO enderecoClienteDAO;
    private final ContaDAO contaDAO;
    private final CartaoDAO cartaoDAO;
    private final SeguroDAO seguroDAO;
    private final PoliticaDeTaxasDAO politicaDeTaxaDAO;
    private final SecurityService securityService;
    private final ReceitaFederalRestTemplate receitaFederalRestTemplate;
    private final BrasilApiRestTemplate brasilApiRestTemplate;
    private final ConversorMoedasService conversorMoedasService;


    // Cadastrar cliente
    public ClienteResponse cadastrarCliente(ClienteUsuarioDTO dto) {
        try {
            log.info("Iniciando cadastro de cliente");
            // Buscar dados do CEP na BrasilAPI
            CEP2 cepInfo = buscarDadosCep(dto.getCep());
            Usuario usuario = criarUsuario(dto);
            Cliente cliente = criarCliente(dto, usuario);
            validarCliente(cliente);
            Cliente clienteSalvo = salvarCliente(cliente);
            salvarEndereco(dto, clienteSalvo, cepInfo);

            log.info("Cadastro de cliente realizado com sucesso");
            return toClienteResponse(clienteSalvo);
        } catch (Exception e) {
            log.error("Erro ao cadastrar cliente: {}", e.getMessage());
            throw new InvalidInputParameterException("Erro ao cadastrar cliente: " + e.getMessage());
        }
    }

    // addConta de forma genérica
    @Transactional
    public ContaResponse abrirConta(Long id_cliente, Usuario usuarioLogado, TipoConta tipo, Moeda moeda, BigDecimal valorDeposito) {
        Objects.requireNonNull(tipo, "Tipo de conta não pode ser nulo");

        Cliente cliente = verificarClienteExistente(id_cliente);
        securityService.validateAccess(usuarioLogado, cliente);
        Conta contaNova = criarContaPorTipo(tipo, cliente, moeda, valorDeposito);
        contaDAO.salvar(contaNova);

        return toContaResponse(contaNova);
    }

    // add cartao
    @Transactional
    public CartaoResponse emitirCartao(Long id_conta, Usuario usuarioLogado, TipoCartao tipo, String senha) {
        Objects.requireNonNull(tipo, "O tipo não pode ser nulo");
        Objects.requireNonNull(senha, "A senha do cartão não pode ser nula");

        Conta conta = verificarContaExistente(id_conta);
        securityService.validateAccess(usuarioLogado, conta.getCliente());
        Cartao cartaoNovo = criarCartaoPorTipo(tipo, conta, senha);
        cartaoDAO.salvar(cartaoNovo);

        return toCartaoResponse(cartaoNovo);
    }

    // contrataSeguro
    @Transactional
    public SeguroResponse contratarSeguro(Long id_cartao, Usuario usuarioLogado, TipoSeguro tipo) {
        Objects.requireNonNull(tipo, "O tipo não pode ser nulo");
        Cartao ccr = cartaoDAO.findCartaoById(id_cartao)
                .orElseThrow(() -> new ResourceNotFoundException("Cartão com ID " + id_cartao + " não encontrado."));
        securityService.validateAccess(usuarioLogado, ccr.getConta().getCliente());

        Seguro seguroNovo = contratarSeguroPorTipo(tipo, ccr);
        seguroDAO.salvar(seguroNovo);
        return toSeguroResponse(seguroNovo);
    }

    public Cliente getClienteById(Long id_cliente, Usuario usuarioLogado) {
        Cliente cliente = verificarClienteExistente(id_cliente);
        securityService.validateAccess(usuarioLogado, cliente);
        return cliente;
    }

    public Cliente verificarClienteExistente(Long id_cliente) {
        return clienteDAO.buscarClienteporId(id_cliente)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(ErrorMessages.CLIENTE_NAO_ENCONTRADO, id_cliente)));
    }

    public Conta verificarContaExistente(Long id_conta) {
        return contaDAO.buscarContaPorId(id_conta)
                .orElseThrow(() -> new ResourceNotFoundException("Conta com ID " + id_conta + " não encontrada."));
    }

    public ClienteResponse toClienteResponse(Cliente cliente) {
        EnderecoCliente endereco = enderecoClienteDAO.buscarEnderecoporClienteOuErro(cliente);
        return new ClienteResponse(cliente, endereco);
    }

    private Conta criarContaPorTipo(TipoConta tipo, Cliente cliente, Moeda moeda, BigDecimal valorDeposito) {
        PoliticaDeTaxas parametros = verificarPoliticaExitente(cliente.getCategoria());

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

    public PoliticaDeTaxas verificarPoliticaExitente(CategoriaCliente categoria) {
        return politicaDeTaxaDAO.findByCategoria(categoria)
                .orElseThrow(() -> new ResourceNotFoundException("Parâmetros não encontrados para a categoria: " + categoria));
    }

    public ContaResponse toContaResponse(Conta conta) {
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

    public CartaoResponse toCartaoResponse(Cartao cartao) {
        return new CartaoResponse(cartao.getId(), cartao.getNumeroCartao(), cartao.getTipoCartao(),
                cartao.getStatus(), cartao.getConta().getNumeroConta(), cartao.getDataVencimento(), cartao.getLimite());
    }

    public Cartao criarCartaoPorTipo(TipoCartao tipo, Conta conta, String senha) {

        CategoriaCliente categoria = conta.getCliente().getCategoria();
        PoliticaDeTaxas parametros = verificarPoliticaExitente(categoria);

        return switch (tipo) {
            case CREDITO -> {
                Cartao ccr = new Cartao(conta, senha, tipo);
                ccr.setLimite(parametros.getLimiteCartaoCredito());
                yield ccr;
            }
            case DEBITO -> {
                Cartao cdb = new Cartao(conta, senha, tipo);
                cdb.setLimite(parametros.getLimiteDiarioDebito());
                yield cdb;
            }
        };

    }

    public Seguro contratarSeguroPorTipo(TipoSeguro tipo, Cartao ccr) {

        CategoriaCliente categoria = ccr.getConta().getCliente().getCategoria();

        PoliticaDeTaxas parametros = politicaDeTaxaDAO.findByCategoria(categoria)
                .orElseThrow(() -> new ResourceNotFoundException("Parâmetros não encontrados para a categoria: " + categoria));

        return switch (tipo) {
            case FRAUDE -> {
                Seguro sf = new Seguro(ccr);
                sf.setValorApolice(parametros.getValorApoliceFraude());
                sf.setPremioApolice(parametros.getTarifaSeguroFraude());
                sf.setDescricaoCondicoes(TipoSeguro.FRAUDE.getDescricao());
                yield sf;
            }
            case VIAGEM -> {
                Seguro sv = new Seguro(ccr);
                sv.setValorApolice(parametros.getValorApoliceViagem());
                sv.setPremioApolice(parametros.getTarifaSeguroViagem());
                sv.setDescricaoCondicoes(TipoSeguro.VIAGEM.getDescricao());
                yield sv;
            }
        };
    }

    public SeguroResponse toSeguroResponse(Seguro seguro) {
        return SeguroResponse.toSeguroResponse(seguro);
    }

    private Usuario criarUsuario(ClienteUsuarioDTO dto) {
        log.info("Criando usuário para o email: {}", dto.getEmail());
        return usuarioDAO.criarUsuario(dto.getEmail(), dto.getSenha(), dto.getRole());
    }

    private Cliente criarCliente(ClienteUsuarioDTO dto, Usuario usuario) {
        Cliente cliente = dto.transformaClienteParaObjeto();
        cliente.setCategoria(CategoriaCliente.COMUM);
        cliente.setUsuario(usuario);
        return cliente;
    }

    private void validarCliente(Cliente cliente) {
        if (receitaFederalRestTemplate.isCpfInvalidoOuInativo(cliente.getCpf())) {
            log.error("CPF inválido ou inativo: {}", cliente.getCpf());
            throw new InvalidInputParameterException("CPF inválido ou inativo na Receita Federal");
        }
        Validator.validarCpfUnico(clienteDAO, cliente.getCpf());
        Validator.validarMaiorIdade(cliente);
    }

    private Cliente salvarCliente(Cliente cliente) {
        log.info("Salvando cliente no banco de dados");
        return clienteDAO.salvar(cliente);
    }

    private void salvarEndereco(ClienteUsuarioDTO dto, Cliente cliente, CEP2 cepInfo) {
        log.info("Salvando endereço para o cliente: {}", cliente.getId());
        EnderecoCliente enderecoCliente = dto.transformaEnderecoParaObjeto();
        enderecoCliente.setCliente(cliente);
        enderecoCliente.setBairro(cepInfo.getNeighborhood());
        enderecoCliente.setCidade(cepInfo.getCity());
        enderecoCliente.setEstado(cepInfo.getState());
        enderecoCliente.setRua(cepInfo.getStreet());
        enderecoClienteDAO.salvar(enderecoCliente);
    }

    private CEP2 buscarDadosCep(String cep) {
        log.info("Buscando dados do CEP");
        return brasilApiRestTemplate.buscarEnderecoPorCep(cep);
    }
}
