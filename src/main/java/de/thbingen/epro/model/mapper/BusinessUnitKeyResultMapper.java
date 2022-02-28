package de.thbingen.epro.model.mapper;

import de.thbingen.epro.model.dto.BusinessUnitKeyResultDto;
import de.thbingen.epro.model.dto.BusinessUnitKeyResultPostDto;
import de.thbingen.epro.model.dto.BusinessUnitKeyResultUpdateDto;
import de.thbingen.epro.model.entity.BusinessUnitKeyResult;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface BusinessUnitKeyResultMapper {

    @Named("WithObjective")
    BusinessUnitKeyResultDto businessUnitKeyResultToDto(BusinessUnitKeyResult businessUnitKeyResult);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "companyKeyResult", ignore = true)
    @Mapping(target = "businessUnitObjective", ignore = true)
    @Mapping(target = "businessUnitKeyResultHistories", ignore = true)
    @Mapping(target = "achievement", constant = "0f")
    BusinessUnitKeyResult postDtoToBusinessUnitKeyResult(BusinessUnitKeyResultPostDto businessUnitKeyResultPostDto);

    @Mapping(target = "name", ignore = true)
    @Mapping(target = "goalValue", ignore = true)
    @Mapping(target = "achievement", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "companyKeyResult", ignore = true)
    @Mapping(target = "businessUnitObjective", ignore = true)
    @Mapping(target = "businessUnitKeyResultHistories", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateBusinessUnitKeyResultFromUpdateDto(BusinessUnitKeyResultUpdateDto businessUnitKeyResultDto, @MappingTarget BusinessUnitKeyResult businessUnitKeyResult);

}
