package br.com.med.voll.api.usecase.usuario.impl;

import br.com.med.voll.api.domain.usuario.DadosAutenticacao;
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

    @Override
    public ResponseEntity signIn(DadosAutenticacao dados) {
        var token = new UsernamePasswordAuthenticationToken(dados.login(), dados.senha());
        Authentication authentication = manager.authenticate(token);
        return ResponseEntity.ok().build();
    }
}
