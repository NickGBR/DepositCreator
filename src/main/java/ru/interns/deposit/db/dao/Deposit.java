package ru.interns.deposit.db.dao;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.context.annotation.Primary;
import org.springframework.ws.config.annotation.EnableWs;

import javax.persistence.*;
import java.util.Date;
import org.hibernate.annotations.Parameter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "deposits")
public class Deposit {

    @Id
    @GenericGenerator(
            name = "account_number_generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @Parameter(name = "sequence_name", value = "user_sequence"),
                    @Parameter(name = "initial_value", value = "1000000000"),
                    @Parameter(name = "increment_size", value = "1")
            }
    )
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "account_number_generator")
    @Column(name = "account_number", unique = true, nullable = false)
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
