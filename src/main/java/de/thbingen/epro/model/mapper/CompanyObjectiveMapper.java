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

    CompanyKeyResultMapper companyKeyResultMapper = Mappers.getMapper(CompanyKeyResultMapper.class);

    @Mapping(target = "companyKeyResults", ignore = true)
    public abstract CompanyObjectiveDto companyObjectiveToDto(CompanyObjective companyObjective);

    @Mapping(target = "companyKeyResults", ignore = true)
    public abstract List<CompanyObjectiveDto> companyObjectiveListToDto(List<CompanyObjective> companyObjectives);

    public abstract CompanyObjective dtoToCompanyObjective(CompanyObjectiveDto companyObjectiveDto);

    @Named("withKeyResults")
    public CompanyObjectiveDto companyObjectiveToDtoIncludeKeyResults(CompanyObjective companyObjective) {
        CompanyObjectiveDto companyObjectiveDto = companyObjectiveToDto(companyObjective);
        companyObjectiveDto.setCompanyKeyResults(companyKeyResultMapper.companyKeyResultSetToDto(companyObjective.getCompanyKeyResults()));
        return companyObjectiveDto;
    }

    @Named("withKeyResults")
    public List<CompanyObjectiveDto> companyObjectiveListToDtoIncludeKeyResults(List<CompanyObjective> companyObjectives) {
        if (companyObjectives == null) {
            return null;
        }

        List<CompanyObjectiveDto> list = new ArrayList<>(companyObjectives.size());
        for (CompanyObjective companyObjective : companyObjectives) {
            list.add(companyObjectiveToDtoIncludeKeyResults(companyObjective));
        }

        return list;
    }
}
