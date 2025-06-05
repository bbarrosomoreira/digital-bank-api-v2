package br.com.cdb.bancodigital.adapter.output.resttemplate;

import br.com.cdb.bancodigital.application.port.out.api.ReceitaFederalPort;
import br.com.cdb.bancodigital.application.core.domain.dto.response.CpfValidationResponse;
import br.com.cdb.bancodigital.config.exceptions.custom.CommunicationException;
import br.com.cdb.bancodigital.utils.ConstantUtils;
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
public class ReceitaFederalRestTemplateImpl implements ReceitaFederalPort {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${api.receita.url}")
    private String apiUrl;

    @Override
    public CpfValidationResponse consultarCpf(String cpf) {
        String url = String.format("%s%s", apiUrl, cpf);
        log.info(ConstantUtils.INICIO_CONSULTA_CPF);
        try {
            ResponseEntity<CpfValidationResponse> response = restTemplate.getForEntity(url, CpfValidationResponse.class);

            return response.getBody();
        } catch (Exception e) {
            log.error(ConstantUtils.ERRO_CONSULTA_CPF, e);
            throw new CommunicationException(ConstantUtils.ERRO_CONSULTA_CPF);
        }
    }

    @Override
    public boolean isCpfInvalidoOuInativo(String cpf) {
        CpfValidationResponse response = consultarCpf(cpf);
        return !response.getValid() || "INATIVO".equals(response.getStatus());
    }

}
