package br.com.cdb.bancodigitaljpa.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import br.com.cdb.bancodigitaljpa.security.UsuarioDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Autowired
	private UsuarioDetailsService usuarioDetailsService;
	
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception { //configura as rotas que precisam de login
        http
        	.csrf(csrf -> csrf.disable())
        	.authorizeHttpRequests(auth -> auth
        			.requestMatchers(HttpMethod.POST, "/auth/**").permitAll() //libera login/cadastro
        			.anyRequest().authenticated() // exige autenticação nos outros
        			)
        			.sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        			.authenticationProvider(authenticationProvider()); 
        return http.build();
    }
    
    @Bean
    public AuthenticationProvider authenticationProvider() {
    	DaoAuthenticationProvider provider =  new DaoAuthenticationProvider();
    	provider.setUserDetailsService(usuarioDetailsService);
    	provider.setPasswordEncoder(passwordEncoder());
    	return provider;
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
    	return new BCryptPasswordEncoder();
    }
    
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

}
