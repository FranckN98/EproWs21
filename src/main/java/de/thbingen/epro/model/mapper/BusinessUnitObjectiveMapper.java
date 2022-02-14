package de.thbingen.epro.model.mapper;

import de.thbingen.epro.model.business.BusinessUnitObjective;
import de.thbingen.epro.model.dto.BusinessUnitDto;
import de.thbingen.epro.model.dto.BusinessUnitObjectiveDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BusinessUnitObjectiveMapper {

    @Mapping(target = "id", source = "unitObjectiveDto.id")
    @Mapping(target = "achievement", source = "unitObjectiveDto.achievement")
    @Mapping(target = "name", source = "unitObjectiveDto.name")
    @Mapping(target = "businessUnit", source = "businessUnit")
    public BusinessUnitObjective businessUnitToDto(BusinessUnitObjectiveDto unitObjectiveDto, BusinessUnitDto businessUnit);

    public BusinessUnitObjectiveDto toDto(BusinessUnitObjective unitObjective);
}
