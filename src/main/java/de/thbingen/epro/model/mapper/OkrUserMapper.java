package de.thbingen.epro.model.mapper;

import de.thbingen.epro.model.dto.OkrUserDto;
import de.thbingen.epro.model.dto.OkrUserPostDto;
import de.thbingen.epro.model.dto.OkrUserUpdateDto;
import de.thbingen.epro.model.entity.OkrUser;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface OkrUserMapper {

    OkrUserDto okrUserToDto(OkrUser okrUser);

    @Mapping(target = "role", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "businessUnit", ignore = true)
    OkrUser dtoToOkrUser(OkrUserDto okrUserDto);

    OkrUserPostDto okrUserToPostDto(OkrUser okrUser);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "businessUnit", ignore = true)
    OkrUser postDtoToOkrUser(OkrUserPostDto okrUserPostDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "businessUnit", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateOkrUserFromUpdateDto(OkrUserUpdateDto okrUserDto, @MappingTarget OkrUser okrUser);

}
