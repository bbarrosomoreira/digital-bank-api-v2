package br.com.cdb.bancodigital.config;

import java.io.IOException;

import br.com.cdb.bancodigital.application.core.service.JwtService;
import br.com.cdb.bancodigital.utils.ConstantUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {
	
	private final JwtService jwtService;
	private final UserDetailsService userDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request,
									@NotNull HttpServletResponse response,
									@NotNull FilterChain filterChain) throws ServletException, IOException {
		log.info(ConstantUtils.INICIO_FILTRO_JWT);
		final String authHeader = request.getHeader(ConstantUtils.HEATHER_AUTHORIZATION);
		
		if (authHeader != null && authHeader.startsWith(ConstantUtils.HEADER_BEARER)) {
			log.info(ConstantUtils.HEADER_AUTENTICACAO_ENCONTRADO);
			final String token = authHeader.substring(7);
			
			try {
				log.info(ConstantUtils.PROCESSANDO_TOKEN);
				processTokenAuthentication(request, token);
				log.info(ConstantUtils.SUCESSO_TOKEN_PROCESSADO);
			} catch (ExpiredJwtException e) {
				log.error(ConstantUtils.TOKEN_EXPIRADO, e.getMessage());
				handleException(response, HttpServletResponse.SC_UNAUTHORIZED, ConstantUtils.TOKEN_EXPIRADO);
				return;
			} catch (UnsupportedJwtException | MalformedJwtException e) {
				log.error(ConstantUtils.TOKEN_INVALIDO, e.getMessage());
				handleException(response, HttpServletResponse.SC_BAD_REQUEST, ConstantUtils.TOKEN_MALFORMADO);
				return;
			} catch (SignatureException e) {
				log.error(ConstantUtils.ASSINATURA_INVALIDA, e.getMessage());
				handleException(response, HttpServletResponse.SC_UNAUTHORIZED, ConstantUtils.ASSINATURA_INVALIDA);
				return;
			} catch (Exception e) {
				log.error(ConstantUtils.ERRO_AUTENTICACAO, e.getMessage());
				handleException(response, HttpServletResponse.SC_UNAUTHORIZED, ConstantUtils.ERRO_AUTENTICACAO + e.getMessage());
				return;
			}
	    }
	    filterChain.doFilter(request, response);
		log.info(ConstantUtils.FIM_FILTRO_JWT);
	}

	private void processTokenAuthentication(HttpServletRequest request, String token) {
		final String username = jwtService.extrairUsername(token);

		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			UserDetails userDetails = userDetailsService.loadUserByUsername(username);

			if (jwtService.tokenValido(token, userDetails)) {
				UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());
				authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

				SecurityContextHolder.getContext().setAuthentication(authToken);
			} else {
				throw new BadCredentialsException(ConstantUtils.TOKEN_INVALIDO);
			}
		}
	}

	private void handleException(HttpServletResponse response, int status, String message) throws IOException {
		response.setStatus(status);
		response.getWriter().write(message);
	}
	

}
