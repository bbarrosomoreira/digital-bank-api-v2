package br.com.cdb.bancodigitaljpa.exceptions;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler{
	
	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(GlobalExceptionHandler.class);
	
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleAllExceptions(Exception ex) {
        ex.printStackTrace(); // pra sair no console/log
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erro: " + ex.getClass().getSimpleName() + " - " + ex.getMessage());
    }

	// Tratamento para clientes não encontrados
	@ExceptionHandler(ClienteNaoEncontradoException.class)
	public ResponseEntity<ErrorResponse> handleClienteNaoEncontrado(
			ClienteNaoEncontradoException ex, 
			WebRequest request) {
		
		ErrorResponse response = new ErrorResponse(
				LocalDateTime.now(), 
				HttpStatus.NOT_FOUND.value(), 
				"Objeto não encontrado", 
				ex.getMessage(), 
				request.getDescription(false).replace("uri=", "")
				);
		
		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}
	
	// Tratamento para CPF já existente
	@ExceptionHandler(CpfExistenteException.class)
	public ResponseEntity<ErrorResponse> handleCpfExistente(
			CpfExistenteException ex,
			WebRequest request) {
		
		ErrorResponse response = new ErrorResponse(
				LocalDateTime.now(),
				HttpStatus.CONFLICT.value(),
				"Conflito de dados",
				ex.getMessage(),
				request.getDescription(false).replace("uri=", "")
				);
		
		return new ResponseEntity<>(response, HttpStatus.CONFLICT);
	}
	
	// Tratamento para objetos não encontrados
	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<String> handleContaNaoEncontrada(RuntimeException ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
	}
	
	// Tratamento para saldo insuficiente
	@ExceptionHandler(SaldoInsuficienteException.class)
	public ResponseEntity<String> handleSaldoInsuficiente(SaldoInsuficienteException ex) {
	    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
	}

	// Tratamento para contas não encontradas
	@ExceptionHandler(ContaNaoEncontradaException.class)
	public ResponseEntity<String> handleContaNaoEncontrada(ContaNaoEncontradaException ex) {
	    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
	}
	
	
//	// Tratamento para erro inesperado
//	@ExceptionHandler(Exception.class)
//	public ResponseEntity<ErrorResponse> handleAllExceptions(
//			Exception ex,
//			WebRequest request) {
//		
//		logger.error("Erro não tratado - Mensagem: {}, StackTrace: {}", ex.getMessage(), ex.toString(), ex);
//		
//		ErrorResponse response = new ErrorResponse(
//				LocalDateTime.now(),
//				HttpStatus.INTERNAL_SERVER_ERROR.value(),
//				"Erro interno",
//				"Ocorreu um erro inesperado. Tente novamente mais tarde",
//				request.getDescription(false).replace("uri=", "")
//				);
//		
//		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
//	}
	
	
	// Classe auxiliar para o formato do erro
	public record ErrorResponse (
			LocalDateTime timestamp,
			int status,
			String error,
			String message,
			String path) {}

}
