package org.example.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class Account {

    public Account(String email, String password, Authority authority) {
        this.email = email;
        this.password = password;
        this.authority = authority;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String email;

    private String password;

    @ManyToOne()
    @JoinColumn(name = "authority_id")
    private Authority authority;

}