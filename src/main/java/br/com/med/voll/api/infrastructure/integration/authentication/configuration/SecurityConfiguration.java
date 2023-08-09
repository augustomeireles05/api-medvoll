package br.com.med.voll.api.infrastructure.integration.authentication.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Classe que concentra as configurações de segurança para configuração de forma STATELESS.
 * Com essa configuração, passa-se a não mais possuir como default, o padrão STATEFUL, fornecendo a tela padrão de login do spring security.
 * Com isso, passe-se a não mais bloquar as URLs, e sim permitir com que nós configuremos isso (Desabilitação do bloqueio de requisição default).
 */

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Autowired
    private SecurityFilter securityFilter;

    /**
     * Expõe o objeto SecurityFilterChain como um bean para o spring
     * desabilita proteção contra ataques do tipo Cross-Site Request Forgery porque o JWT já possui essa proteção
     * transformação para tipo STATELESS
     * cria um objeto do tipo SecurityFilterChain
     *
     * @param http
     * @return
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(req -> {
                    req.requestMatchers(HttpMethod.POST, "/authentication").permitAll();
                    req.anyRequest().authenticated();
                })
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
    /**
     * @param configuration
     * @return getAuthenticationManager() que sabe criar o objeto do tipo AuthenticationManager
     * @throws Exception
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    /**
     * Ensinando ao spring qual o algorítmo de hashing de senhas.
     * Utilização do algorítmo BCrypt
     * @return encriptogração da senha através do BCrypt
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}
