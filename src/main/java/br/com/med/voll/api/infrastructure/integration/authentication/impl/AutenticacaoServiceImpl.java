package br.com.med.voll.api.infrastructure.integration.authentication.impl;

import br.com.med.voll.api.infrastructure.integration.authentication.AutenticacaoService;
import br.com.med.voll.api.infrastructure.integration.repository.usuario.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Essa classe representa o serviço de autenticação;
 * Essa classe será chamada de forma automática pelo spring security quando houver a tentativa de realização de autenticação na aplicação;
 * Ela acessará a base de dados procurando o login do usuário a qual está tentando logar.
 */
@Service
public class AutenticacaoServiceImpl implements AutenticacaoService {

    @Autowired
    private UsuarioRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByLogin(username);
    }
}
