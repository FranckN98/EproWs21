package de.thbingen.epro.model.mapper;

import de.thbingen.epro.model.business.CompanyObjective;
import de.thbingen.epro.model.dto.CompanyObjectiveDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CompanyObjectiveMapper {

    CompanyObjectiveDto companyObjectiveToDto(CompanyObjective companyObjective);

    List<CompanyObjectiveDto> companyObjectiveListToDto(List<CompanyObjective> companyObjectives);

    CompanyObjective dtoToCompanyObjective(CompanyObjectiveDto companyObjectiveDto);


    List<CompanyObjectiveDto> companyObjectiveListToDtoIncludeKeyResults(List<CompanyObjective> content);
}
