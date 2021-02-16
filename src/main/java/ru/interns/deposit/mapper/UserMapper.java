package ru.interns.deposit.mapper;

import jdk.nashorn.internal.ir.annotations.Ignore;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.interns.deposit.db.dao.Deposit;
import ru.interns.deposit.dto.UserDTO;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface UserMapper {
    @Mapping(target = "accountNumber", ignore = true)
    Deposit toEntity(UserDTO source);
}
