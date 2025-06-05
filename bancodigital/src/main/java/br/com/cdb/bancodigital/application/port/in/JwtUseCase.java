package br.com.cdb.bancodigital.application.port.in;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;
import java.util.function.Function;

public interface JwtUseCase {
    String gerarToken(UserDetails usuario);
    String extrairUsername(String token);
    <T> T extrairClaim(String token, Function<Claims, T> claimsResolver);
    Claims extrairTodosClaims(String token);
    boolean tokenValido(String token, UserDetails userDetails);
    boolean tokenExpirado(String token);
    Date extrairExpiracao(String token);
}
