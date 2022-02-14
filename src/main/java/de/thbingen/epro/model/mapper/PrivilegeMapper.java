package de.thbingen.epro.model.mapper;

import de.thbingen.epro.model.business.Privilege;
import de.thbingen.epro.model.dto.PrivilegeDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PrivilegeMapper {

    PrivilegeDto privilegeToDto(Privilege privilege);

    @Mapping(target = "roles", ignore = true)
    Privilege dtoToPrivilege(PrivilegeDto privilegeDto);

    List<PrivilegeDto> privilegeListToPrivilegeDtoList(List<Privilege> privileges);

}
