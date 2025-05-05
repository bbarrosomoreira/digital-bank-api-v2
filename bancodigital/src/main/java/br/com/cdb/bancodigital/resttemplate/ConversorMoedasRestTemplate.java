package br.com.cdb.bancodigital.resttemplate;

import br.com.cdb.bancodigital.dto.response.ApiConversorMoedasResponse;
import br.com.cdb.bancodigital.exceptions.custom.ApiCommunicationException;
import br.com.cdb.bancodigital.model.enums.Moeda;
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
public class ConversorMoedasRestTemplate {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${api.currency.url}")
    private String apiUrl;

    @Value("${api.currency.key}")
    private String apiKey;

    public ApiConversorMoedasResponse chamarApiConversao(@NonNull Moeda from, @NonNull Moeda to, @NonNull BigDecimal amount) {
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
            log.info("Chamada à API bem-sucedida. Status: {}, URL: {}", response.getStatusCode(), url);
            return response.getBody();
        } catch (Exception e) {
            log.error("Erro ao chamar a API de conversão de moedas. URL: {}, Erro: {}", url, e.getMessage());
            throw new ApiCommunicationException("Erro ao chamar a API de conversão de moedas");
        }
    }


}
