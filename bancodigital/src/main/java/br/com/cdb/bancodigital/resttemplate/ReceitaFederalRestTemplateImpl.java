package br.com.cdb.bancodigital.resttemplate;

import br.com.cdb.bancodigital.dto.response.CpfValidationResponse;
import br.com.cdb.bancodigital.exceptions.custom.ApiCommunicationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
@Profile("prod") // roda apenas com a API externa de simulação da Receita Federal
public class ReceitaFederalRestTemplateImpl implements ReceitaFederalRestTemplate {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${api.receita.url}")
    private String apiUrl;

    @Override
    public CpfValidationResponse consultarCpf(String cpf) {
        try {
            String url = apiUrl + cpf;
            log.info("Consultando CPF na API da Receita: {}", cpf);
            ResponseEntity<CpfValidationResponse> response = restTemplate.getForEntity(url, CpfValidationResponse.class);
            return response.getBody();
        } catch (HttpClientErrorException e) {
            log.warn("Erro ao consultar CPF: {} - Status: {}", cpf, e.getStatusCode());
            return extrairCorpoDaMensagem(e.getResponseBodyAsString());
        } catch (HttpServerErrorException e) {
            log.error("Erro na API da Receita: {}", e.getMessage());
            throw new ApiCommunicationException("Erro na API da Receita", e);
        } catch (Exception e) {
            log.error("Erro inesperado ao consultar CPF: {}", e.getMessage());
            throw new ApiCommunicationException("Erro inesperado ao consultar CPF", e);
        }
    }

    private CpfValidationResponse extrairCorpoDaMensagem(String json) {
        try {
            return objectMapper.readValue(json, CpfValidationResponse.class);
        } catch (JsonProcessingException e) {
            log.error("Erro ao processar corpo da resposta de erro: {}", e.getMessage());
            return new CpfValidationResponse();
        }
    }

    @Override
    public boolean isCpfInvalidoOuInativo(String cpf) {
        CpfValidationResponse response = consultarCpf(cpf);
        return !response.isValid() || "INATIVO".equals(response.getStatus());
    }

}
