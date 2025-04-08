package br.com.cdb.bancodigitaljpa.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import br.com.cdb.bancodigitaljpa.entity.EnderecoCliente;
import br.com.cdb.bancodigitaljpa.entity.EnderecoCorreios;
import br.com.cdb.bancodigitaljpa.exceptions.CepNaoEncontradoException;

@Service
public class CorreiosService {
	
	private static final String URL_VIACEP = "https://viacep.com.br/ws/{cep}/json/";
	private final RestTemplate restTemplate = new RestTemplate();
	
	@Cacheable("ceps")
	public EnderecoCliente consultarEConverterCep(String cep) {
		EnderecoCorreios enderecoCorreios = consultarCep(cep);
		return converterParaEnderecoCliente(enderecoCorreios);
	}
	
	private EnderecoCorreios consultarCep(String cep) {
		return restTemplate.getForObject(URL_VIACEP, EnderecoCorreios.class, cep);
	}
	
	private EnderecoCliente converterParaEnderecoCliente(EnderecoCorreios enderecoCorreios) {
		if (enderecoCorreios.getCep() == null) {
			throw new CepNaoEncontradoException("CEP não encontrado");
		}
		return new EnderecoCliente(
				enderecoCorreios.getLogradouro(),
				0,
				enderecoCorreios.getComplemento(),
				enderecoCorreios.getLocalidade(),
				enderecoCorreios.getUf(),
				enderecoCorreios.getCep(),
				true
				);
	}

}
