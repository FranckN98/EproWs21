package de.thbingen.epro.model.mapper;

import de.thbingen.epro.model.dto.OkrUserDto;
import de.thbingen.epro.model.entity.OkrUser;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface OkrUserMapper {

    OkrUserDto okrUserToDto(OkrUser OkrUser);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "businessUnit", ignore = true)
    OkrUser dtoToOkrUser(OkrUserDto OkrUserDto);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "businessUnit", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateOkrUserFromDto(OkrUserDto okrUserDto, @MappingTarget OkrUser okrUser);

}
