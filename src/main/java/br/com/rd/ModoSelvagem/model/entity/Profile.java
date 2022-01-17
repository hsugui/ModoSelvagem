package br.com.rd.ModoSelvagem.model.entity;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

@Data
@Entity
public class Profile implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "profile_name")
    private String name;

    @Override
    public String getAuthority() {
        return name;
    }
}
