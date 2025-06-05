package br.com.cdb.bancodigital.application.core.service;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

import br.com.cdb.bancodigital.application.port.in.JwtUseCase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import br.com.cdb.bancodigital.utils.ConstantUtils;

@Service
@Slf4j
public class JwtService implements JwtUseCase {
	
	@Value("${jwt.secret}")
	private String secret;
	private Key getChaveAssinatura() {
		return Keys.hmacShaKeyFor(secret.getBytes());
	}
	
	public String gerarToken(UserDetails usuario) {
		log.info(ConstantUtils.JWT_GERANDO_TOKEN);
		String token = Jwts.builder()
				.setSubject(usuario.getUsername())
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 2)) // expira em 2h
				.signWith(getChaveAssinatura(), SignatureAlgorithm.HS256)
				.compact();
		log.info(ConstantUtils.JWT_TOKEN_GERADO_SUCESSO);
		return token;
	}
	
	public String extrairUsername(String token) {
		log.debug(ConstantUtils.JWT_EXTRAINDO_USERNAME);
		return extrairClaim(token, Claims::getSubject);
	}
	public <T>T extrairClaim(String token, Function<Claims, T> claimsResolver) {
		log.debug(ConstantUtils.JWT_EXTRAINDO_CLAIM);
		final Claims claims = extrairTodosClaims(token);
		return claimsResolver.apply(claims);
	}
	public Claims extrairTodosClaims(String token) {
		log.debug(ConstantUtils.JWT_EXTRAINDO_TODOS_CLAIMS);
		return Jwts
				.parserBuilder()
				.setSigningKey(getChaveAssinatura())
				.build()
				.parseClaimsJws(token)
				.getBody();
	}
	public boolean tokenValido(String token, UserDetails userDetails) {
		log.info(ConstantUtils.JWT_VALIDANDO_TOKEN);
		final String username = extrairUsername(token);
		boolean valido = (username != null && username.equals(userDetails.getUsername()) && !tokenExpirado(token));
		if (valido) {
			log.info(ConstantUtils.JWT_TOKEN_VALIDO);
		} else {
			log.warn(ConstantUtils.JWT_TOKEN_INVALIDO);
		}
		return valido;
	}
	public boolean tokenExpirado(String token) {
		boolean expirado = extrairExpiracao(token).before(new Date());
		if (expirado) {
			log.warn(ConstantUtils.JWT_TOKEN_EXPIRADO);
		}
		return expirado;
	}
	public Date extrairExpiracao(String token) {
		log.debug(ConstantUtils.JWT_EXTRAINDO_EXPIRACAO);
		return extrairClaim(token, Claims::getExpiration);
	}

}
