package de.thbingen.epro.model.mapper;

import de.thbingen.epro.model.business.Role;
import de.thbingen.epro.model.dto.RoleDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    public RoleDto roleToDto(Role role);

    @Mapping(target = "privilege", ignore = true)
    @Mapping(target = "okrUsers", ignore = true)
    public Role dtoToRole(RoleDto roleDto);

    public List<RoleDto> roleListToRoleDtoList(List<Role> roleList);
}
