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



    @Mapping(target = "businessUnitKeyResultHistories", ignore = true)
    @Mapping(target = "businessUnitObjective", ignore = true)
    BusinessUnitKeyResultDto businessUnitKeyResultToDtoWithoutObjective(BusinessUnitKeyResult businessUnitKeyResult);

    @Named("WithObjective")
    @Mapping(target = "businessUnitKeyResultHistories", ignore = true)
    BusinessUnitKeyResultDto businessUnitKeyResultToDto (BusinessUnitKeyResult businessUnitKeyResult);

    @Mapping(target = "id", source = "businessUnitKeyResultDto.id")
    @Mapping(target = "name", source = "businessUnitKeyResultDto.name")
    @Mapping(target = "currentValue", source = "businessUnitKeyResultDto.currentValue")
    @Mapping(target = "goalValue", source = "businessUnitKeyResultDto.goalValue")
    @Mapping(target = "achievement", source = "businessUnitKeyResultDto.achievement")
    @Mapping(target = "businessUnitObjective", source = "businessUnitObjectiveDto")
    @Mapping(target = "businessUnitKeyResultHistories", ignore = true)
    BusinessUnitKeyResult dtoToBusinessUnitKeyResult(BusinessUnitKeyResultDto businessUnitKeyResultDto, BusinessUnitObjectiveDto businessUnitObjectiveDto);


    @Mapping(target = "businessUnitKeyResultHistories", ignore = true)
    Set<BusinessUnitKeyResultDto> businessUnitKeyResultSetToDto(Set<BusinessUnitKeyResult> businessUnitKeyResultSet);

}
