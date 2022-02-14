package de.thbingen.epro.model.mapper;

import de.thbingen.epro.model.business.Role;
import de.thbingen.epro.model.dto.RoleDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    RoleDto roleToDto(Role role);

    @Mapping(target = "privileges", ignore = true)
    @Mapping(target = "okrUsers", ignore = true)
    Role dtoToRole(RoleDto roleDto);

    List<RoleDto> roleListToRoleDtoList(List<Role> roleList);
}
