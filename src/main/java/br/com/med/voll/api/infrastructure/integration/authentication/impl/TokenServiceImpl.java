package br.com.med.voll.api.infrastructure.integration.authentication.impl;

import br.com.med.voll.api.domain.usuario.Usuario;
import br.com.med.voll.api.infrastructure.integration.authentication.TokenService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static br.com.med.voll.api.utils.Constants.ERROR_GENERATE_TOKEN;
import static br.com.med.voll.api.utils.Constants.WITH_ISSUER;
import static br.com.med.voll.api.utils.Constants.ERROR_GET_TOKEN_INVALID_OR_EXPIRED;

@Service
public class TokenServiceImpl implements TokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    @Override
    public String createToken(Usuario usuario) {
        try {
            var algoritmo = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer("API Voll.med")
                    .withSubject(usuario.getLogin())
                    .withClaim("id", usuario.getId())
                    .withExpiresAt(dataExpiracao())
                    .sign(algoritmo);
        } catch (JWTCreationException exception){
            throw new RuntimeException(ERROR_GENERATE_TOKEN, exception);
        }
    }

    /**
     * Método que verifica se o token está valido e devolver o id, usuário e a senha do usuário que fez determinada requisição.
     * @param tokenJWT
     * @return o objeto decodificado de dentro do token (id, login, senha)
     */
    @Override
    public String getSubject(String tokenJWT) {
        try {
            var algoritmo = Algorithm.HMAC256(secret);
            return JWT.require(algoritmo)
                    .withIssuer("API Voll.med")
                    .build()
                    .verify(tokenJWT)
                    .getSubject();

        } catch(JWTVerificationException exception) {
            throw new RuntimeException(ERROR_GET_TOKEN_INVALID_OR_EXPIRED);
        }
    }

    /**
     * @return horario de geração acrescida de 8 horas transformando o return em um objeto instant e acrescentando o timezone de -3 horas que o brasil possui em relação ao meridiano de Greenwitch
     */
    private Instant dataExpiracao() {
        return LocalDateTime.now().plusHours(8).toInstant(ZoneOffset.of("-03:00"));
    }
}
