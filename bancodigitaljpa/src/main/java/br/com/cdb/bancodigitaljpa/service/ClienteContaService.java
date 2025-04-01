package br.com.cdb.bancodigitaljpa.service;

public class ClienteContaService {

//	private static final Logger log = LoggerFactory.getLogger(ClienteService.class);
//	
//	private static final BigDecimal TAXA_COMUM_MANUTENCAO = new BigDecimal("12.00");
//	private static final BigDecimal TAXA_SUPER_MANUTENCAO = new BigDecimal("8.00");
//	private static final BigDecimal TAXA_PREMIUM_MANUTENCAO = BigDecimal.ZERO;
//	
//	private static final BigDecimal TAXA_COMUM_RENDIMENTO = new BigDecimal("0.005");
//	private static final BigDecimal TAXA_SUPER_RENDIMENTO = new BigDecimal("0.007");
//	private static final BigDecimal TAXA_PREMIUM_RENDIMENTO = new BigDecimal("0.009");
//
//	private final ClienteRepository clienteRepository;
//	private final ContaRepository contaRepository;
//
//	@Autowired
//	public ClienteContaService(ClienteRepository clienteRepository, ContaRepository contaRepository) {
//		this.clienteRepository = clienteRepository;
//		this.contaRepository = contaRepository;
//	}
//
//	@Transactional
//	public Cliente updateCategoriaCliente(Long id_cliente, CategoriaCliente novaCategoria) {
//		Cliente cliente = clienteRepository.findById(id_cliente)
//				.orElseThrow(() -> new ClienteNaoEncontradoException(id_cliente));
//
//		cliente.setCategoria(novaCategoria);
//		clienteRepository.save(cliente);
//
//		atualizarTaxasDasContas(id_cliente, cliente.getCategoria());
//
//		return cliente;
//	}
//
//	@Transactional(propagation = Propagation.REQUIRES_NEW)
//	public void atualizarTaxasDasContas(Long id_cliente, CategoriaCliente novaCategoria) {
//		try {
//			List<ContaBase> contas = contaRepository.findByClienteId(id_cliente);
//
//			if (contas.isEmpty()) {
//				log.warn("Cliente ID {} não possui contas.", id_cliente);
//				return;
//			}
//
//			contas.forEach(conta -> {
//				if (conta instanceof ContaCorrente cc) {
//					cc.setTaxaManutencao(calcularTaxaManutencao(novaCategoria));
//				} else if (conta instanceof ContaPoupanca cp) {
//					cp.setTaxaRendimento(calcularTaxaRendimento(novaCategoria));
//				}
//			});
//
//			contaRepository.saveAll(contas);
//		} catch (Exception e) {
//			log.error("Falha ao atualizar taxas das contas do cliente ID {}", id_cliente, e);
//			throw e;
//		}
//	}
//	
//	public BigDecimal calcularTaxaManutencao(CategoriaCliente categoria) {
//		return switch (categoria) {
//		case COMUM -> TAXA_COMUM_MANUTENCAO;
//		case SUPER -> TAXA_SUPER_MANUTENCAO;
//		case PREMIUM -> TAXA_PREMIUM_MANUTENCAO;
//		};
//	}
//	public BigDecimal calcularTaxaRendimento(CategoriaCliente categoria) {
//		return switch (categoria) {
//		case COMUM -> TAXA_COMUM_RENDIMENTO;
//		case SUPER -> TAXA_SUPER_RENDIMENTO;
//		case PREMIUM -> TAXA_PREMIUM_RENDIMENTO;
//		};
//	}

}
