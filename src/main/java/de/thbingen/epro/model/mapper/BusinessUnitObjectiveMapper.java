package de.thbingen.epro.model.mapper;

import de.thbingen.epro.model.dto.BusinessUnitDto;
import de.thbingen.epro.model.dto.BusinessUnitObjectiveDto;
import de.thbingen.epro.model.entity.BusinessUnitObjective;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface BusinessUnitObjectiveMapper {

    BusinessUnitObjectiveDto businessUnitObjectiveToDto(BusinessUnitObjective businessUnitObjective);

    @Mapping(target = "companyKeyResult", ignore = true)
    @Mapping(target = "businessUnitKeyResults", ignore = true)
    @Mapping(target = "businessUnit", ignore = true)
    BusinessUnitObjective dtoToBusinessUnitObjective(BusinessUnitObjectiveDto businessUnitObjectiveDto);

    @Named("withBusinessUnit")
    @Mapping(target = "achievement", source = "businessUnitObjectiveDto.achievement")
    @Mapping(target = "name", source = "businessUnitObjectiveDto.name")
    @Mapping(target = "companyKeyResult", ignore = true)
    @Mapping(target = "businessUnitKeyResults", ignore = true)
    @Mapping(target = "businessUnit", ignore = true)
    BusinessUnitObjective dtoToBusinessUnitObjective(BusinessUnitObjectiveDto businessUnitObjectiveDto, BusinessUnitDto businessUnitDto);
}
