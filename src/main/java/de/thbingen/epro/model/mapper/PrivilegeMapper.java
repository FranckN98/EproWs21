package de.thbingen.epro.model.mapper;

import de.thbingen.epro.model.dto.PrivilegeDto;
import de.thbingen.epro.model.entity.Privilege;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PrivilegeMapper {

    PrivilegeDto privilegeToDto(Privilege privilege);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "roles", ignore = true)
    Privilege dtoToPrivilege(PrivilegeDto privilegeDto);

}
