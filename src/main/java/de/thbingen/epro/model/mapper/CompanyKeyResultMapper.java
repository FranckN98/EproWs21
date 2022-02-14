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


    @Mapping(target = "businessUnitObjectives", ignore = true)
    public CompanyKeyResultDto companyKeyResultToDto(CompanyKeyResult companyKeyResult);

    @Mapping(target = "businessUnitObjectives", ignore = true)
    public CompanyKeyResult dtoToCompanyKeyResult(CompanyKeyResultDto companyKeyResultDto);
    @Mapping(target = "companyObjective", ignore = true)
    public List<CompanyKeyResultDto> companyKeyResultListToDto(List<CompanyKeyResult> companyKeyResults);
    @Mapping(target = "companyObjective", ignore = true)
    @Mapping(target = "businessUnitObjectives", ignore = true)
    public Set<CompanyKeyResultDto> companyKeyResultSetToDto(Set<CompanyKeyResult> companyKeyResultSet);
    @Mapping(target = "companyObjective", ignore = true)
    public Set<CompanyKeyResult> dtoSetToCompanyKeyResultSet(Set<CompanyKeyResultDto> companyKeyResultSet);
    @Mapping(target = "companyObjective", source = "companyObjectiveDto")
    @Mapping(target = "id", source = "companyKeyResultDto.id")
    @Mapping(target = "name", source = "companyKeyResultDto.name")
    @Mapping(target = "achievement", source = "companyKeyResultDto.achievement")
    public CompanyKeyResult dtoToCompanyKeyResultWithObjective(CompanyKeyResultDto companyKeyResultDto, CompanyObjectiveDto companyObjectiveDto);
}
