package br.com.med.voll.api.usecase.usuario.impl;

import br.com.med.voll.api.domain.usuario.DadosAutenticacao;
import br.com.med.voll.api.domain.usuario.Usuario;
import br.com.med.voll.api.infrastructure.integration.authentication.DadosTokenJWT;
import br.com.med.voll.api.infrastructure.integration.authentication.TokenService;
import br.com.med.voll.api.usecase.usuario.AutenticacaoUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class AutenticacaoUsuarioServiceImpl implements AutenticacaoUsuarioService {

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private TokenService tokenService;

    @Override
    public ResponseEntity signIn(DadosAutenticacao dados) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(dados.login(), dados.senha());
        Authentication authentication = manager.authenticate(authenticationToken);
        var tokenJWT = tokenService.createToken((Usuario) authentication.getPrincipal());
        return ResponseEntity.ok(new DadosTokenJWT(tokenJWT));
    }
}
