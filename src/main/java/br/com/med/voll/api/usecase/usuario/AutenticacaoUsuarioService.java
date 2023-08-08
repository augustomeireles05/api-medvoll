package br.com.med.voll.api.usecase.usuario;

import br.com.med.voll.api.domain.usuario.DadosAutenticacao;
import org.springframework.http.ResponseEntity;

public interface AutenticacaoUsuarioService {
    ResponseEntity signIn(DadosAutenticacao dados);
}
