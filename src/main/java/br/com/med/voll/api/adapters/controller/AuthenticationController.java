package br.com.med.voll.api.adapters.controller;

import br.com.med.voll.api.domain.usuario.DadosAutenticacao;
import br.com.med.voll.api.usecase.usuario.AutenticacaoUsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/authentication")
public class AuthenticationController {

    @Autowired
    private AutenticacaoUsuarioService service;

    @PostMapping
    public ResponseEntity signIn(@RequestBody @Valid DadosAutenticacao dados) {
        return service.signIn(dados);
    }
}
