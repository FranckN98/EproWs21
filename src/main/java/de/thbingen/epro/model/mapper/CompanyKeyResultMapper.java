package de.thbingen.epro.model.mapper;

import de.thbingen.epro.model.dto.CompanyKeyResultDto;
import de.thbingen.epro.model.entity.CompanyKeyResult;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface CompanyKeyResultMapper {


    CompanyKeyResultDto companyKeyResultToDto(CompanyKeyResult companyKeyResult);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "companyObjective", ignore = true)
    @Mapping(target = "companyKeyResultHistories", ignore = true)
    @Mapping(target = "businessUnitObjectives", ignore = true)
    @Mapping(target = "businessUnitKeyResults", ignore = true)
    CompanyKeyResult dtoToCompanyKeyResult(CompanyKeyResultDto companyKeyResultDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "companyObjective", ignore = true)
    @Mapping(target = "companyKeyResultHistories", ignore = true)
    @Mapping(target = "businessUnitObjectives", ignore = true)
    @Mapping(target = "businessUnitKeyResults", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateCompanyKeyResultFromDto(CompanyKeyResultDto companyKeyResultDto, @MappingTarget CompanyKeyResult companyKeyResult);
}
