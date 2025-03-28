package br.com.cdb.bancodigitaljpa.validations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Constraint(validatedBy = CpfValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidCPF {
	String message() default "CPF inválido";
	
	Class<?>[] groups() default {}; //agrupamento de validações
	
	Class<? extends Payload>[] payload() default {}; //metadados adicionais


}
