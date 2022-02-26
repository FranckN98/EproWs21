package de.thbingen.epro.model.mapper;

import de.thbingen.epro.model.business.OkrUser;
import de.thbingen.epro.model.dto.OkrUserDto;
import de.thbingen.epro.model.dto.OkrUserPostDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OkrUserMapper {

    OkrUserDto okrUserToDto(OkrUser okrUser);

    @Mapping(target = "role", ignore = true)
    @Mapping(target = "businessUnit", ignore = true)
    @Mapping(target = "password", ignore = true)
    OkrUser dtoToOkrUser(OkrUserDto okrUserDto);

    OkrUserPostDto okrUserToPostDto(OkrUser okrUser);

    @Mapping(target = "role", ignore = true)
    @Mapping(target = "businessUnit", ignore = true)
    OkrUser postDtoToOkrUser(OkrUserPostDto okrUserPostDto);

    List<OkrUserDto> okrUserListToOkrUserDtoList(List<OkrUser> OkrUserList);

}
