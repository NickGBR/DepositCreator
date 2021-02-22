package ru.interns.deposit.db.repositoiry;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.interns.deposit.db.dao.User;

import java.util.Optional;

@Repository
public interface UsersRepository extends CrudRepository<User, Long>{
    Boolean existsByLogin(String login);
    Optional<User> findByLogin(String login);
}
