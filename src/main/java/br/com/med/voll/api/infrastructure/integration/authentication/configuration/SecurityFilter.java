package br.com.med.voll.api.infrastructure.integration.authentication.configuration;

import br.com.med.voll.api.infrastructure.integration.authentication.TokenService;
import br.com.med.voll.api.infrastructure.integration.authentication.impl.TokenServiceImpl;
import br.com.med.voll.api.infrastructure.integration.repository.usuario.UsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jdk.swing.interop.SwingInterOpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static br.com.med.voll.api.utils.Constants.ERROR_NON_SENT_TOKEN_AT_HEADER;
import static br.com.med.voll.api.utils.Constants.AUTHORIZATION_HEADER;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioRepository repository;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        System.out.println("ANTES DO FILTRO");
        var tokenJWT = getToken(request);

        if(tokenJWT != null) {
            var subject = tokenService.getSubject(tokenJWT);
            System.out.println(subject);
            var usuario = repository.findByLogin(subject);

            var authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            System.out.println("LOGADO NA REQUISIÇÃO");
        }

        filterChain.doFilter(request, response);
    }

    private String getToken(HttpServletRequest request) {
        var authorizationHeader = request.getHeader(AUTHORIZATION_HEADER);

        if(authorizationHeader != null)
            return authorizationHeader.replace("Bearer ", "").trim();

        return null;
    }
}
