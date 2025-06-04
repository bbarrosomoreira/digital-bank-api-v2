package br.com.cdb.bancodigital.adapters.out.resttemplate;

import br.com.cdb.bancodigital.application.port.out.api.ConversorMoedasPort;
import br.com.cdb.bancodigital.application.core.domain.dto.response.ApiConversorMoedasResponse;
import br.com.cdb.bancodigital.exceptions.custom.CommunicationException;
import br.com.cdb.bancodigital.application.core.domain.model.enums.Moeda;
import br.com.cdb.bancodigital.utils.ConstantUtils;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

@Component
@Slf4j
public class ConversorMoedasRestTemplate implements ConversorMoedasPort {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${api.currency.url}")
    private String apiUrl;

    @Value("${api.currency.key}")
    private String apiKey;

    public ApiConversorMoedasResponse converterMoeda(@NonNull Moeda from, @NonNull Moeda to, @NonNull BigDecimal amount) {
        String url = String.format("%s?from=%s&to=%s&amount=%s", apiUrl, from, to, amount);

        HttpHeaders headers = new HttpHeaders();
        headers.set("apikey", apiKey);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<ApiConversorMoedasResponse> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    ApiConversorMoedasResponse.class
            );
            log.info(ConstantUtils.SUCESSO_CONVERSAO, response.getStatusCode(), url);
            return response.getBody();
        } catch (Exception e) {
            log.error(ConstantUtils.ERRO_CONVERSAO, url, e.getMessage());
            throw new CommunicationException(ConstantUtils.ERRO_CONVERSAO);
        }
    }


}
