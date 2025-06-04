package br.com.cdb.bancodigital.application.core.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CpfValidationResponse {
	private Boolean success;
	private String cpf;
	private Boolean valid;
	private String status;
	private String message;
}
