package de.thbingen.epro.model.mapper;

import de.thbingen.epro.model.business.CompanyKeyResult;
import de.thbingen.epro.model.dto.CompanyKeyResultDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface CompanyKeyResultMapper {

    @Mapping(target = "companyObjective", ignore = true)
    public CompanyKeyResultDto companyKeyResultToDto(CompanyKeyResult companyKeyResult);
    @Mapping(target = "companyObjective", ignore = true)
    public List<CompanyKeyResultDto> companyKeyResultListToDto(List<CompanyKeyResult> companyKeyResults);
    @Mapping(target = "companyObjective", ignore = true)
    public Set<CompanyKeyResultDto> companyKeyResultSetToDto(Set<CompanyKeyResult> companyKeyResultSet);
}
