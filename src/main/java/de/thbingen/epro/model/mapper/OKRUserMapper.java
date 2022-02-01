package de.thbingen.epro.model.mapper;

import de.thbingen.epro.model.business.OKRUser;
import de.thbingen.epro.model.dto.OKRUserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OKRUserMapper {

    public OKRUserDto okrUserToDto(OKRUser okrUser);

    @Mapping(target = "role", ignore = true)
    @Mapping(target = "businessUnit", ignore = true)
    public OKRUser dtoToOKRUser(OKRUserDto okrUserDto);

    public List<OKRUserDto> okrUserListToOKRUserList(List<OKRUser> okrUserList);

}
