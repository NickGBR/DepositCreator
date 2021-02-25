package ru.interns.deposit.db.dao;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "personal_data")
public class PersonalData {
    @Id
    @Column(name = "id")
    @GeneratedValue
    private Long id;

    @Column(name = "foreign_key")
    private Long foreignKey;

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

    @Column(name = "kladr_address")
    private Long kladrAddress;

    @Column(name = "address")
    private String address;
}
