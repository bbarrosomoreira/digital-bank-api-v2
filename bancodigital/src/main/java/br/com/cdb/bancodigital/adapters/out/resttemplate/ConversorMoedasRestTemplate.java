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
import java.math.RoundingMode;
import java.util.Optional;

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
    public Optional<ApiConversorMoedasResponse> fazerConversao(Moeda from, Moeda to, BigDecimal amount) {
        if (from == null || to == null || amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            log.warn(ConstantUtils.ERRO_CONVERSAO);
            return Optional.empty();
        }

        try {
            ApiConversorMoedasResponse resposta = converterMoeda(from, to, amount);
            return Optional.ofNullable(resposta);
        } catch (Exception e) {
            log.error(ConstantUtils.ERRO_CONVERSAO + " {}", e.getMessage());
            return Optional.empty();
        }
    }

    public BigDecimal converterDeBrl(Moeda to, BigDecimal amount) {
        if (to == null || amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            log.warn(ConstantUtils.ERRO_CONVERSAO);
            return BigDecimal.ZERO;
        }

        if (to == Moeda.BRL) {
            return amount.setScale(2, RoundingMode.HALF_UP);
        }

        try {
            ApiConversorMoedasResponse resposta = converterMoeda(Moeda.BRL, to, amount);
            if (resposta != null && resposta.getResult() != null) {
                return resposta.getResult().setScale(2, RoundingMode.HALF_UP);
            }
        } catch (Exception e) {
            log.error(ConstantUtils.ERRO_CONVERSAO + " {}", e.getMessage());
        }

        return BigDecimal.ZERO;
    }

}
