package de.thbingen.epro.model.mapper;

import de.thbingen.epro.model.dto.BusinessUnitKeyResultDto;
import de.thbingen.epro.model.entity.BusinessUnitKeyResult;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface BusinessUnitKeyResultMapper {

    @Named("WithObjective")
    BusinessUnitKeyResultDto businessUnitKeyResultToDto(BusinessUnitKeyResult businessUnitKeyResult);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "businessUnitObjective", ignore = true)
    @Mapping(target = "companyKeyResult", ignore = true)
    @Mapping(target = "businessUnitKeyResultHistories", ignore = true)
    @Mapping(target = "name", source = "businessUnitKeyResultDto.name")
    @Mapping(target = "currentValue", source = "businessUnitKeyResultDto.currentValue")
    @Mapping(target = "goalValue", source = "businessUnitKeyResultDto.goalValue")
    @Mapping(target = "achievement", source = "businessUnitKeyResultDto.achievement")
    BusinessUnitKeyResult dtoToBusinessUnitKeyResult(BusinessUnitKeyResultDto businessUnitKeyResultDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "companyKeyResult", ignore = true)
    @Mapping(target = "businessUnitObjective", ignore = true)
    @Mapping(target = "businessUnitKeyResultHistories", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateBusinessUnitKeyResultFromDto(BusinessUnitKeyResultDto businessUnitKeyResultDto, @MappingTarget BusinessUnitKeyResult businessUnitKeyResult);

}
