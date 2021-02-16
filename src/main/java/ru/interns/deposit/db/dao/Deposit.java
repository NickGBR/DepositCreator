package ru.interns.deposit.db.dao;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.context.annotation.Primary;
import org.springframework.ws.config.annotation.EnableWs;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "deposits")
public class Deposit {

    @Id
    @Column(name = "account_number", unique = true)
    private Long accountNumber;

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "passport_number")
    private Long passportNumber;

    @Column(name = "date_of_birthday")
    private Date dateOfBirthday;
}
