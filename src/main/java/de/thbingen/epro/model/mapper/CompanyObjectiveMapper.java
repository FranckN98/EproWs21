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
public abstract class CompanyObjectiveMapper {

    public abstract CompanyObjectiveDto companyObjectiveToDto(CompanyObjective companyObjective);

    public abstract List<CompanyObjectiveDto> companyObjectiveListToDto(List<CompanyObjective> companyObjectives);

    public abstract CompanyObjective dtoToCompanyObjective(CompanyObjectiveDto companyObjectiveDto);
}
