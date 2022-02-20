package de.thbingen.epro.model.mapper;

import de.thbingen.epro.model.business.BusinessUnitKeyResult;
import de.thbingen.epro.model.dto.BusinessUnitKeyResultDto;
import de.thbingen.epro.model.dto.BusinessUnitObjectiveDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface BusinessUnitKeyResultMapper {

    @Named("WithObjective")
    BusinessUnitKeyResultDto businessUnitKeyResultToDto (BusinessUnitKeyResult businessUnitKeyResult);

    @Mapping(target = "companyKeyResult", ignore = true)
    @Mapping(target = "businessUnitKeyResultHistories", ignore = true)
    @Mapping(target = "businessUnitObjective", source = "businessUnitObjectiveDto")
    @Mapping(target = "name", source = "businessUnitKeyResultDto.name")
    @Mapping(target = "currentValue", source = "businessUnitKeyResultDto.currentValue")
    @Mapping(target = "goalValue", source = "businessUnitKeyResultDto.goalValue")
    @Mapping(target = "achievement", source = "businessUnitKeyResultDto.achievement")
    BusinessUnitKeyResult dtoToBusinessUnitKeyResult(BusinessUnitKeyResultDto businessUnitKeyResultDto, BusinessUnitObjectiveDto businessUnitObjectiveDto);


    Set<BusinessUnitKeyResultDto> businessUnitKeyResultSetToDto(Set<BusinessUnitKeyResult> businessUnitKeyResultSet);

}
