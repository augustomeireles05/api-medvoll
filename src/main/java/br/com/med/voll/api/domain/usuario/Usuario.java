package br.com.med.voll.api.domain.usuario;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Table(name = "usuarios")
@Entity(name = "Usuario")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String login;
    private String senha;

    /**
     * Se no projeto houver controle de permissão, tais como Perfis (admin, moderador, etc) precisaremos criar uma classe que representa esses perfis.
     * Como no projeto, qualquer usuário poderá visualizar qualquer tela, vamos apenas criar uma Coleção representando os perfis porque o spring necessita dessa implementação.
     * Simularemos uma coleção só para a compilação da aplicação.
     * @return perfil default para todos os perfis que se chama ROLE_USER (padrão de perfil único e fixo).
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    /**
     * @return o atributo que representa a senha
     */
    @Override
    public String getPassword() {
        return senha;
    }

    /**
     * @return o atributo que representa o username
     */
    @Override
    public String getUsername() {
        return login;
    }

    /**
     * Controle de expiração de conta do usuário
     * usuário não está expirado
     * @return
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Controle de bloqueio de usuário
     * usuário não está bloquado
     * @return
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Controle de expiração de credencial de usuário
     * usuário não está com a credencial expirada
     * @return
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Controle de habilitação de usuário
     * usuário está habilitado
     * @return
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
