package org.example.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.roles.Roles;
import org.springframework.security.core.GrantedAuthority;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class Authority implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Enumerated(EnumType.STRING)
    private Roles role;

    public Authority(Roles role) {
        this.role = role;
    }

    @Override
    public String getAuthority() {
        return role.name();
    }
}