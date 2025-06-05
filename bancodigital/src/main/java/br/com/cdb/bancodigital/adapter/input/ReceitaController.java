package br.com.cdb.bancodigital.adapter.input;

import br.com.cdb.bancodigital.application.core.domain.dto.ConsultaCpfDTO;
import br.com.cdb.bancodigital.utils.ConstantUtils;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import br.com.cdb.bancodigital.application.core.domain.dto.response.CpfValidationResponse;
import br.com.cdb.bancodigital.application.port.out.api.ReceitaFederalPort;

@RestController
@RequestMapping(ConstantUtils.RECEITA_FEDERAL)
@AllArgsConstructor
@Slf4j
public class ReceitaController {
	
	private final ReceitaFederalPort receitaFederalPort;

	@PreAuthorize(ConstantUtils.ROLE_ADMIN)
	@GetMapping(ConstantUtils.GET_CPF)
	public ResponseEntity<CpfValidationResponse> consultarCpf(@Valid @RequestBody ConsultaCpfDTO dto) {
		long startTime = System.currentTimeMillis();
		log.info(ConstantUtils.INICIO_CONSULTA_CPF);

		CpfValidationResponse response = receitaFederalPort.consultarCpf(dto.getCpf());
		log.info(ConstantUtils.SUCESSO_CONSULTA_CPF);

		long endTime = System.currentTimeMillis();
		log.info(ConstantUtils.FIM_CHAMADA, endTime - startTime);
		return ResponseEntity.ok(response);
	}

}
