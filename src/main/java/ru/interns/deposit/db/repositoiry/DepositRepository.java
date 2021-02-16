package ru.interns.deposit.db.repositoiry;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.interns.deposit.db.dao.Deposit;

@Repository
public interface DepositRepository extends CrudRepository<Deposit, Long> {
}
