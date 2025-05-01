package br.com.cdb.bancodigital.exceptions.handler;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

import br.com.cdb.bancodigital.exceptions.custom.ApiException;
import br.com.cdb.bancodigital.dto.response.ErrorResponse;
import br.com.cdb.bancodigital.dto.response.ValidationErrorResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	// Trata exceções customizadas da hierarquia ApiException
	@ExceptionHandler(ApiException.class)
	public ResponseEntity<ErrorResponse> handleApiException(ApiException ex, WebRequest request) {
		ErrorResponse response = new ErrorResponse(
				LocalDateTime.now(), 
				ex.getStatus().value(),
				ex.getStatus().getReasonPhrase(), 
				ex.getMessage(), 
				request.getDescription(false).replace("uri=", ""));

		return new ResponseEntity<>(response, ex.getStatus());
	}

	// Trata erros de validação
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(
			MethodArgumentNotValidException ex,
			HttpHeaders headers, 
			HttpStatusCode status, 
			WebRequest request) {

		Map<String, String> fieldErrors = ex.getBindingResult().getFieldErrors()
				.stream()
				.collect(Collectors.toMap(
						FieldError::getField,
		                FieldError::getDefaultMessage
						));

		ValidationErrorResponse response = new ValidationErrorResponse(
				LocalDateTime.now(),
				HttpStatus.BAD_REQUEST.value(), 
				"Erro de validação", 
				request.getDescription(false).replace("uri=", ""), 
				fieldErrors);

		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<ErrorResponse> handleSpringAccessDenied(AccessDeniedException ex, WebRequest request) {
		ErrorResponse response = new ErrorResponse(
				LocalDateTime.now(), 
				HttpStatus.FORBIDDEN.value(), 
				"Acesso negado", 
				"Você não tem permissão para acessar este recurso.",
				request.getDescription(false).replace("uri=", "")
		);
		
		return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
	}

	// Fallback genérico
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleUnhandledExceptions(Exception ex, WebRequest request) {
		logger.error("Erro não tratado", ex);

		ErrorResponse response = new ErrorResponse(
				LocalDateTime.now(), 
				HttpStatus.INTERNAL_SERVER_ERROR.value(),
				"Erro interno", 
				"Ocorreu um erro inesperado. Tente novamente mais tarde.",
				request.getDescription(false).replace("uri=", ""));

		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);

	}
}
