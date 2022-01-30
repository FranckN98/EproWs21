package de.thbingen.epro.model.mapper;

import de.thbingen.epro.model.business.OKRUser;
import de.thbingen.epro.model.dto.OKRUserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OKRUserMapper {

    public OKRUserDto OKRUserToDto(OKRUser okrUser);

    @Mapping(target = "role", ignore = true)
    @Mapping(target = "businessUnit", ignore = true)
    public OKRUser DtoToOKRUser(OKRUserDto okrUserDto);
}
