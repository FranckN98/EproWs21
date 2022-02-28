package de.thbingen.epro.model.mapper;

import de.thbingen.epro.model.dto.PrivilegeDto;
import de.thbingen.epro.model.entity.Privilege;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface PrivilegeMapper {

    PrivilegeDto privilegeToDto(Privilege privilege);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "roles", ignore = true)
    Privilege dtoToPrivilege(PrivilegeDto privilegeDto);

    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updatePrivilegeFromDto(PrivilegeDto privilegeDto, @MappingTarget Privilege privilege);
}
