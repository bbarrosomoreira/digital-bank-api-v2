package br.com.cdb.bancodigital.resttemplate;

import br.com.cdb.bancodigital.dto.response.CpfValidationResponse;
import br.com.cdb.bancodigital.exceptions.custom.CommunicationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
@Profile("prod") // roda apenas com a API externa de simulação da Receita Federal
public class ReceitaFederalRestTemplateImpl implements ReceitaFederalRestTemplate {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${api.receita.url}")
    private String apiUrl;

    @Override
    public CpfValidationResponse consultarCpf(String cpf) {
        String url = String.format("%s%s", apiUrl, cpf);
        log.info("Consultando CPF na API que simula a da Receita Federal");
        try {
            ResponseEntity<CpfValidationResponse> response = restTemplate.getForEntity(url, CpfValidationResponse.class);

            return response.getBody();
        } catch (Exception e) {
            log.error("Erro ao consultar CPF na API que simula a da Receita Federal", e);
            throw new CommunicationException("Erro ao consultar CPF");
        }
    }

    @Override
    public boolean isCpfInvalidoOuInativo(String cpf) {
        CpfValidationResponse response = consultarCpf(cpf);
        return !response.isValid() || "INATIVO".equals(response.getStatus());
    }

}
