package br.com.cdb.bancodigitaljpa.validations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CpfValidator implements ConstraintValidator<ValidCPF, String>{
	
	@Override
	public boolean isValid(String cpf, ConstraintValidatorContext context) {
		if (cpf == null) {
			return false;
		}
		
		//remover caracteres nao numericos
		cpf = cpf.replaceAll("[^0-9]", "");
		
		//verificar se tem 11 dígitos ou se todos são iguais
		if (cpf.length() != 11 || cpf.matches("(\\d)\\1{10}")) {
			return false;
		}
		
		try {
			//calcular primeiro dígito verificador
			int soma = 0;
			for (int i = 0; i < 9; i++) {
				soma += (10 - i) * Character.getNumericValue(cpf.charAt(i));
			}
			int digito1 = 11 - (soma % 11);
			if (digito1 > 9)
				digito1 = 0;
			
			//calcular segundo dígito verificador
			soma = 0;
			for (int i = 0; i < 10; i++) {
				soma += (11 - i) * Character.getNumericValue(cpf.charAt(i));
			}
			int digito2 = 11 - (soma % 11);
			if (digito2 > 9)
				digito2 = 0;
			
			//verificar os dígitos
			return (Character.getNumericValue(cpf.charAt(9)) == digito1)
					&& (Character.getNumericValue(cpf.charAt(10)) == digito2);
		} catch(Exception e) {
			return false;
		}
	}

}
