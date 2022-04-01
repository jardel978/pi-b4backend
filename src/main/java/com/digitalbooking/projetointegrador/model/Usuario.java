package com.digitalbooking.projetointegrador.model;

import com.digitalbooking.projetointegrador.model.enums.NomeFuncao;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Classe para mapeamento da entidade Usuario
 *
 * @version 1.0
 * @since 1.0
 */

@Entity
@Getter
@Setter
@Table(name = "tb_usuario")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@NoArgsConstructor
@AllArgsConstructor
public class Usuario implements UserDetails {

    @Id
    @Column(name = "usuario_id")
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    private String nome;
    private String sobrenome;

    @Column(unique = true)
    private String email;

    private String senha;

    @ManyToOne
    @JoinColumn(name = "funcao_id", foreignKey = @ForeignKey(name = "fk_funcao"))
    private Funcao funcao;

    @Column(name = "usuario_validado")
    private Boolean usuarioEstaValidado;

    @Column(name = "data_validacao_registro")
    private Date dataValidacaoRegistro;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> autoridadesDoUsuario = new ArrayList<>();
        SimpleGrantedAuthority admin = new SimpleGrantedAuthority("ROLE_ADMIN");
        SimpleGrantedAuthority user = new SimpleGrantedAuthority("ROLE_USER");

        if (funcao.getNomeFuncaoEnum() == NomeFuncao.ADMIN) {
            autoridadesDoUsuario.add(admin);
            autoridadesDoUsuario.add(user);
        }

        if (funcao.getNomeFuncaoEnum() == NomeFuncao.USER) {
            autoridadesDoUsuario.add(user);
        }
        return autoridadesDoUsuario;
    }

    @Override
    public String getPassword() {
        return this.senha;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.usuarioEstaValidado;
    }
}
