package de.thbingen.epro.model.mapper;

import de.thbingen.epro.model.dto.RoleDto;
import de.thbingen.epro.model.entity.Role;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    RoleDto roleToDto(Role role);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "privileges", ignore = true)
    @Mapping(target = "okrUsers", ignore = true)
    Role dtoToRole(RoleDto roleDto);

    @Mapping(target = "privileges", ignore = true)
    @Mapping(target = "okrUsers", ignore = true)
    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateRoleFromDto(RoleDto roleDto, @MappingTarget Role role);
}
