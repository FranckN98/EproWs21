package de.thbingen.epro.model.mapper;

import de.thbingen.epro.model.dto.CompanyKeyResultDto;
import de.thbingen.epro.model.dto.CompanyKeyResultPostDto;
import de.thbingen.epro.model.dto.CompanyKeyResultUpdateDto;
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
    @Mapping(target = "achievement", constant = "0f")
    CompanyKeyResult postDtoToCompanyKeyResult(CompanyKeyResultPostDto companyKeyResultPostDto);

    @Mapping(target = "name", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "goalValue", ignore = true)
    @Mapping(target = "companyObjective", ignore = true)
    @Mapping(target = "companyKeyResultHistories", ignore = true)
    @Mapping(target = "businessUnitObjectives", ignore = true)
    @Mapping(target = "businessUnitKeyResults", ignore = true)
    @Mapping(target = "achievement", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateCompanyKeyResultFromUpdateDto(CompanyKeyResultUpdateDto companyKeyResultUpdateDto, @MappingTarget CompanyKeyResult companyKeyResult);
}
