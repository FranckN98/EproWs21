package de.thbingen.epro.model.mapper;

import de.thbingen.epro.model.business.OkrUser;
import de.thbingen.epro.model.dto.OkrUserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OkrUserMapper {

    OkrUserDto okrUserToDto(OkrUser OkrUser);

    @Mapping(target = "role", ignore = true)
    @Mapping(target = "businessUnit", ignore = true)
    OkrUser dtoToOkrUser(OkrUserDto OkrUserDto);

    List<OkrUserDto> okrUserListToOkrUserDtoList(List<OkrUser> OkrUserList);

}
