package de.thbingen.epro.model.mapper;

import de.thbingen.epro.model.dto.BusinessUnitDto;
import de.thbingen.epro.model.entity.BusinessUnit;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface BusinessUnitMapper {

    BusinessUnitDto businessUnitToDto(BusinessUnit businessUnit);

    @Mapping(target = "okrUsers", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "businessUnitObjectives", ignore = true)
    BusinessUnit dtoToBusinessUnit(BusinessUnitDto businessUnitDto);

    @Mapping(target = "okrUsers", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "businessUnitObjectives", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateBusinessUnitFromDto(BusinessUnitDto businessUnitDto, @MappingTarget BusinessUnit businessUnit);
}
