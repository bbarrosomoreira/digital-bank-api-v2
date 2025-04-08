package br.com.cdb.bancodigitaljpa.exceptions;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import br.com.cdb.bancodigitaljpa.dto.ErrorResponse;
import br.com.cdb.bancodigitaljpa.dto.ValidationErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

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
	protected ResponseEntity<Object> handleMethodArgumentNotValid(
			MethodArgumentNotValidException ex,
			HttpHeaders headers, 
			HttpStatus status, 
			WebRequest request) {

		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getFieldErrors()
				.forEach(error -> errors.put(
						error.getObjectName() + " - " + error.getField(), 
						error.getDefaultMessage()));

		ValidationErrorResponse response = new ValidationErrorResponse(LocalDateTime.now(),
				HttpStatus.BAD_REQUEST.value(), "Erro de validação", "Campos inválidos na requisição",
				request.getDescription(false).replace("uri=", ""), errors);

		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	// Fallback genérico
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleUnhandledExceptions(Exception ex, WebRequest request) {
		logger.error("Erro não tratado", ex);

		ErrorResponse response = new ErrorResponse(LocalDateTime.now(), HttpStatus.INTERNAL_SERVER_ERROR.value(),
				"Erro interno", "Ocorreu um erro inesperado. Tente novamente mais tarde.",
				request.getDescription(false).replace("uri=", ""));

		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);

	}
}
