package br.com.cdb.bancodigital.application.core.domain.entity;

import java.math.BigDecimal;

import br.com.cdb.bancodigital.application.core.domain.entity.enums.Status;
import org.springframework.security.crypto.password.PasswordEncoder;

public interface CartaoBase {
	void realizarPagamento(BigDecimal valor);
	void alterarLimite(BigDecimal limiteNovo);
	void alterarStatus(Status statusNovo);
	void alterarSenha(String senhaAntiga, String senhaNova, PasswordEncoder passwordEncoder);
	
}
