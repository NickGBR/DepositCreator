package ru.interns.deposit.db.repositoiry;


import org.springframework.data.repository.CrudRepository;
import ru.interns.deposit.db.dao.PersonalData;
import java.util.*;

public interface PersonalDataRepository extends CrudRepository<PersonalData, Long> {
PersonalData getByForeignKey(Long key);
Boolean existsByForeignKey(Long foreignKey);
Boolean existsByPassportNumber(Long passportNumber);
}
