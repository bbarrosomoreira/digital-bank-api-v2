package br.com.cdb.bancodigital.security;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
	
	@Value("${jwt.secret}")
	private String secret;
	private Key getChaveAssinatura() {
		return Keys.hmacShaKeyFor(secret.getBytes());
	}
	
	public String gerarToken(UserDetails usuario) {
		return Jwts.builder()
				.setSubject(usuario.getUsername())
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 2)) // expira em 2h
				.signWith(getChaveAssinatura(), SignatureAlgorithm.HS256)
				.compact();
				
	}
	
	public String extrairUsername(String token) {
		return extrairClaim(token, Claims::getSubject);
	}
	public <T>T extrairClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extrairTodosClaims(token);
		return claimsResolver.apply(claims);
	}
	public Claims extrairTodosClaims(String token) {
		return Jwts
				.parserBuilder()
				.setSigningKey(getChaveAssinatura())
				.build()
				.parseClaimsJws(token)
				.getBody();
	}
	public boolean tokenValido(String token, UserDetails userDetails) {
		final String username = extrairUsername(token);
		return (username != null && username.equals(userDetails.getUsername()) && !tokenExpirado(token));
	}
	public boolean tokenExpirado(String token) {
		return extrairExpiracao(token).before(new Date());
	}
	public Date extrairExpiracao(String token) {
		return extrairClaim(token, Claims::getExpiration);
	}

}
