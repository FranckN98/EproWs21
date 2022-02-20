package de.thbingen.epro.model.mapper;

import de.thbingen.epro.model.business.CompanyKeyResult;
import de.thbingen.epro.model.dto.CompanyKeyResultDto;
import de.thbingen.epro.model.dto.CompanyObjectiveDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface CompanyKeyResultMapper {


    CompanyKeyResultDto companyKeyResultToDto(CompanyKeyResult companyKeyResult);

    CompanyKeyResult dtoToCompanyKeyResult(CompanyKeyResultDto companyKeyResultDto);

    List<CompanyKeyResultDto> companyKeyResultListToDto(List<CompanyKeyResult> companyKeyResults);

    Set<CompanyKeyResultDto> companyKeyResultSetToDto(Set<CompanyKeyResult> companyKeyResultSet);

    Set<CompanyKeyResult> dtoSetToCompanyKeyResultSet(Set<CompanyKeyResultDto> companyKeyResultSet);

    @Mapping(target = "companyObjective", source = "companyObjectiveDto")
    @Mapping(target = "name", source = "companyKeyResultDto.name")
    @Mapping(target = "achievement", source = "companyKeyResultDto.achievement")
    CompanyKeyResult dtoToCompanyKeyResultWithObjective(CompanyKeyResultDto companyKeyResultDto, CompanyObjectiveDto companyObjectiveDto);
}
