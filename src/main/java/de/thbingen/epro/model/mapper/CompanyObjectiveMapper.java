package de.thbingen.epro.model.mapper;

import de.thbingen.epro.model.dto.CompanyObjectiveDto;
import de.thbingen.epro.model.entity.CompanyObjective;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface CompanyObjectiveMapper {

    CompanyObjectiveDto companyObjectiveToDto(CompanyObjective companyObjective);

    @Mapping(target = "companyKeyResults", ignore = true)
    @Mapping(target = "id", ignore = true)
    CompanyObjective dtoToCompanyObjective(CompanyObjectiveDto companyObjectiveDto);


    @Mapping(target = "companyKeyResults", ignore = true)
    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateCompanyObjectiveFromDto(CompanyObjectiveDto companyObjectiveDto, @MappingTarget CompanyObjective companyObjective);

}
