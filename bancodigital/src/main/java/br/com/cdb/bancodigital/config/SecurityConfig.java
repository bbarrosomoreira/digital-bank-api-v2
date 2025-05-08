package br.com.cdb.bancodigital.config;

import br.com.cdb.bancodigital.utils.ConstantUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import br.com.cdb.bancodigital.service.UsuarioDetailsService;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity()
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {
	
	private final UsuarioDetailsService usuarioDetailsService;
	private final JwtAuthFilter jwtAuthFilter;
	
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception { //configura as rotas que precisam de login
        log.info(ConstantUtils.SECURITY_FILTER_CHAIN);
        http
        	.csrf(AbstractHttpConfigurer::disable)
        	.authorizeHttpRequests(auth -> {
                        log.info(ConstantUtils.ACESSO_PUBLICO);
                auth.requestMatchers(HttpMethod.POST, ConstantUtils.ROTAS_LIBERADAS).permitAll() //libera login/cadastro
                                .anyRequest().authenticated(); // Exige autenticação para todas as outras rotas
                    })
        			.sessionManagement(sess -> {
                        log.info(ConstantUtils.POLITICA_SESSION_STATELESS);
                        sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                    })
        			.authenticationProvider(authenticationProvider())
        			.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        log.info(ConstantUtils.SUCESSO_SECURITY_FILTER_CHAIN);
        return http.build();
    }
    
    @Bean
    public AuthenticationProvider authenticationProvider() {
        log.info(ConstantUtils.AUTENTICACAO_PROVIDER);
    	DaoAuthenticationProvider provider =  new DaoAuthenticationProvider();
    	provider.setUserDetailsService(usuarioDetailsService);
    	provider.setPasswordEncoder(passwordEncoder());
        log.info(ConstantUtils.SUCESSO_AUTENTICACAO_PROVIDER);
    	return provider;
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        log.info(ConstantUtils.CRIANDO_PASSWORD_ENCODER);
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        log.info(ConstantUtils.AUTHENTICACAO_MANAGER);
        return config.getAuthenticationManager();
    }

}
