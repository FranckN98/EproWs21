package de.thbingen.epro.model.mapper;

import de.thbingen.epro.model.business.CompanyObjective;
import de.thbingen.epro.model.dto.CompanyObjectiveDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface CompanyObjectiveMapper {

    //@Mapping(target = "companyKeyResults", ignore = true)
    public CompanyObjectiveDto companyObjectiveToDto(CompanyObjective companyObjective);

    //@Mapping(target = "companyKeyResults", ignore = true)
    public List<CompanyObjectiveDto> companyObjectiveListToDto(List<CompanyObjective> companyObjectives);
   // @Mapping(target = "companyKeyResults", ignore = true)
    public CompanyObjective dtoToCompanyObjective(CompanyObjectiveDto companyObjectiveDto);


    public List<CompanyObjectiveDto> companyObjectiveListToDtoIncludeKeyResults(List<CompanyObjective> content);
}
