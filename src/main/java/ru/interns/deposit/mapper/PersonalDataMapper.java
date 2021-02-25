package ru.interns.deposit.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.interns.deposit.db.dao.PersonalData;
import ru.interns.deposit.dto.PersonalDataDTO;


@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface PersonalDataMapper {
    @Mapping(target = "id", ignore = true)
    PersonalData toEntity(PersonalDataDTO source);
}
