package br.com.cdb.bancodigital.config;

import java.io.IOException;

import br.com.cdb.bancodigital.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
	
	private final JwtService jwtService;
	private final UserDetailsService userDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request,
									@NotNull HttpServletResponse response,
									@NotNull FilterChain filterChain) throws ServletException, IOException {
		final String authHeader = request.getHeader("Authorization");
		
		if (authHeader != null && authHeader.startsWith("Bearer")) {
			final String token = authHeader.substring(7);
			
			try {
				processTokenAuthentication(request, response, token);
			} catch (ExpiredJwtException e) {
				handleException(response, HttpServletResponse.SC_UNAUTHORIZED, "Token expirado");
				return;
			} catch (UnsupportedJwtException | MalformedJwtException e) {
				handleException(response, HttpServletResponse.SC_BAD_REQUEST, "Token malformado ou não suportado");
				return;
			} catch (SignatureException e) {
				handleException(response, HttpServletResponse.SC_UNAUTHORIZED, "Assinatura inválida do token");
				return;
			} catch (Exception e) {
				handleException(response, HttpServletResponse.SC_UNAUTHORIZED, "Erro na autenticação: " + e.getMessage());
				return;
			}
	    }
	    filterChain.doFilter(request, response);
	}

	private void processTokenAuthentication(HttpServletRequest request, HttpServletResponse response, String token) {
		final String username = jwtService.extrairUsername(token);

		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			UserDetails userDetails = userDetailsService.loadUserByUsername(username);

			if (jwtService.tokenValido(token, userDetails)) {
				UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());
				authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

				SecurityContextHolder.getContext().setAuthentication(authToken);
			} else {
				throw new BadCredentialsException("Token inválido");
			}
		}
	}

	private void handleException(HttpServletResponse response, int status, String message) throws IOException {
		response.setStatus(status);
		response.getWriter().write(message);
	}
	

}
