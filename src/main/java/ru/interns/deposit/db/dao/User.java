package ru.interns.deposit.db.dao;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.interns.deposit.security.enums.Role;
import ru.interns.deposit.security.enums.Status;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (name = "users")
public class User {

    @Id
    @Column(name = "id")
    @GeneratedValue
    private Long id;

    @Column(name = "login")
    private String login;

    @Column(name = "password")
    private String password;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "role")
    private Role role;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "status")
    private Status status;
}
