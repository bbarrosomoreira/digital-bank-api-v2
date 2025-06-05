package br.com.cdb.bancodigital.config.exceptions.handler;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

import br.com.cdb.bancodigital.config.exceptions.custom.ApiException;
import br.com.cdb.bancodigital.application.core.domain.dto.response.ErrorResponse;
import br.com.cdb.bancodigital.application.core.domain.dto.response.ValidationErrorResponse;
import br.com.cdb.bancodigital.utils.ConstantUtils;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

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
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	// Trata exceções customizadas da hierarquia ApiException
	@ExceptionHandler(ApiException.class)
	public ResponseEntity<ErrorResponse> handleApiException(ApiException ex, WebRequest request) {
		log.error(ConstantUtils.ERRO_TRATADO,
				ex.getStatus(), request.getDescription(false), ex.getMessage());

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
			@NotNull HttpHeaders headers,
			@NotNull HttpStatusCode status,
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
				ConstantUtils.ERRO_VALIDACAO, 
				request.getDescription(false).replace("uri=", ""), 
				fieldErrors);

		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<ErrorResponse> handleSpringAccessDenied(AccessDeniedException ex, WebRequest request) {
		ErrorResponse response = new ErrorResponse(
				LocalDateTime.now(), 
				HttpStatus.FORBIDDEN.value(), 
				ConstantUtils.ACESSO_NEGADO, 
				ConstantUtils.MENSAGEM_ACESSO_NEGADO,
				request.getDescription(false).replace("uri=", "")
		);
		
		return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex, WebRequest request) {
		String mensagem = String.format(ConstantUtils.MENSAGEM_TIPO_ARGUMENTO, ex.getValue(), ex.getName());
		ErrorResponse response = new ErrorResponse(
				LocalDateTime.now(),
				HttpStatus.BAD_REQUEST.value(),
				ConstantUtils.ERRO_TIPO_ARGUMENTO,
				mensagem,
				request.getDescription(false).replace("uri=", "")
		);

		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(TimeoutException.class)
	public ResponseEntity<ErrorResponse> handleTimeoutException(TimeoutException ex, WebRequest request) {
		log.error(ConstantUtils.ERRO_TIMEOUT, request.getDescription(false), ex.getMessage());

		ErrorResponse response = new ErrorResponse(
				LocalDateTime.now(),
				HttpStatus.REQUEST_TIMEOUT.value(),
				ConstantUtils.TIMEOUT,
				ex.getMessage(),
				request.getDescription(false).replace("uri=", "")
		);

		return new ResponseEntity<>(response, HttpStatus.REQUEST_TIMEOUT);
	}

	// Fallback genérico
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleUnhandledExceptions(Exception ex, WebRequest request) {
		logger.error(ConstantUtils.ERRO_INTERNO, ex);

		ErrorResponse response = new ErrorResponse(
				LocalDateTime.now(), 
				HttpStatus.INTERNAL_SERVER_ERROR.value(),
				ConstantUtils.ERRO_INTERNO, 
				ConstantUtils.MENSAGEM_ERRO_INTERNO,
				request.getDescription(false).replace("uri=", ""));

		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);

	}
}
