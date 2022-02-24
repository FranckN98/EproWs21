package de.thbingen.epro.model.mapper;

import de.thbingen.epro.model.dto.BusinessUnitDto;
import de.thbingen.epro.model.dto.BusinessUnitObjectiveDto;
import de.thbingen.epro.model.entity.BusinessUnitObjective;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface BusinessUnitObjectiveMapper {

    BusinessUnitObjectiveDto businessUnitObjectiveToDto(BusinessUnitObjective businessUnitObjective);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "companyKeyResult", ignore = true)
    @Mapping(target = "businessUnitKeyResults", ignore = true)
    @Mapping(target = "businessUnit", ignore = true)
    BusinessUnitObjective dtoToBusinessUnitObjective(BusinessUnitObjectiveDto businessUnitObjectiveDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "companyKeyResult", ignore = true)
    @Mapping(target = "businessUnitKeyResults", ignore = true)
    @Mapping(target = "businessUnit", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateBusinessUnitObjectiveFromDto(BusinessUnitObjectiveDto businessUnitObjectiveDto, @MappingTarget BusinessUnitObjective businessUnitObjective);

    @Named("withBusinessUnit")
    @Mapping(target = "achievement", source = "businessUnitObjectiveDto.achievement")
    @Mapping(target = "name", source = "businessUnitObjectiveDto.name")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "companyKeyResult", ignore = true)
    @Mapping(target = "businessUnitKeyResults", ignore = true)
    @Mapping(target = "businessUnit", ignore = true)
    BusinessUnitObjective dtoToBusinessUnitObjectiveWithBusinessUnit(BusinessUnitObjectiveDto businessUnitObjectiveDto, BusinessUnitDto businessUnitDto);
}
