package br.com.cdb.bancodigital.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
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
public class JwtAuthFilter extends OncePerRequestFilter {
	
	@Autowired
	private JwtService jwtService;
	
	@Autowired
	private UserDetailsService userDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, 
									HttpServletResponse response, 
									FilterChain filterChain) throws ServletException, IOException {
		final String authHeader = request.getHeader("Authorization");
		
		if (authHeader != null && authHeader.startsWith("Bearer")) {
			final String token = authHeader.substring(7);
			
			try {
				final String username = jwtService.extrairUsername(token);
				
				if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
					UserDetails userDetails = userDetailsService.loadUserByUsername(username);
					
					if(jwtService.tokenValido(token, userDetails)) {
						UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
								userDetails, null, userDetails.getAuthorities());
						authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
						
						SecurityContextHolder.getContext().setAuthentication(authToken);
					} else {
						throw new BadCredentialsException("Token inválido");
					}
				}
			} catch (ExpiredJwtException e) {
	            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
	            response.getWriter().write("Token expirado");
	            return;

	        } catch (UnsupportedJwtException | MalformedJwtException e) {
	            response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400
	            response.getWriter().write("Token malformado ou não suportado");
	            return;

	        } catch (SignatureException e) {
	            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
	            response.getWriter().write("Assinatura inválida do token");
	            return;

	        } catch (Exception e) {
	            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
	            response.getWriter().write("Erro na autenticação: " + e.getMessage());
	            return;
	        }
	    }

	    filterChain.doFilter(request, response);
		
	}
	
	

}
