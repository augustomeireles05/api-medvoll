package br.com.med.voll.api.infrastructure.integration.authentication;

import br.com.med.voll.api.domain.usuario.Usuario;

public interface TokenService {

    public String createToken(Usuario usuario);

    public String getSubject(String tokenJWT);
}
