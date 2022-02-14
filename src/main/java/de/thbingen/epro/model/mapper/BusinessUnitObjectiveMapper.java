package de.thbingen.epro.model.mapper;

import de.thbingen.epro.model.business.BusinessUnitObjective;
import de.thbingen.epro.model.dto.BusinessUnitDto;
import de.thbingen.epro.model.dto.BusinessUnitObjectiveDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.HashSet;
import java.util.Set;

@Mapper(componentModel ="spring")
public abstract class BusinessUnitObjectiveMapper {

    BusinessUnitKeyResultMapper businessUnitKeyResultMapper = Mappers.getMapper(BusinessUnitKeyResultMapper.class);

    @Mapping(target = "businessUnitKeyResults", ignore = true)
    public abstract BusinessUnitObjectiveDto businessUnitObjectiveToDto(BusinessUnitObjective businessUnitObjective);

    @Mapping(target = "businessUnitKeyResults", ignore = true)
    @Mapping(target = "businessUnit", ignore = true)
    public abstract BusinessUnitObjective dtoToBusinessUnitObjective(BusinessUnitObjectiveDto businessUnitObjectiveDto);
    public abstract Set<BusinessUnitObjective> dtoSetToBusinessUnitObjectiveSet(Set<BusinessUnitObjectiveDto> businessUnitObjectiveDtos);
    public abstract Set<BusinessUnitObjectiveDto> businessUnitObjectiveSetToDtoSet(Set<BusinessUnitObjective> businessUnitObjectives);

    @Named("withBusinessUnit")
    @Mapping(target = "id", source = "businessUnitObjectiveDto.id")
    @Mapping(target = "achievement", source = "businessUnitObjectiveDto.achievement")
    @Mapping(target = "name", source = "businessUnitObjectiveDto.name")
    @Mapping(target = "businessUnit", source = "businessUnitDto")
    @Mapping(target = "businessUnitKeyResults", ignore = true)
    public abstract BusinessUnitObjective dtoToBusinessUnitObjective(BusinessUnitObjectiveDto businessUnitObjectiveDto, BusinessUnitDto businessUnitDto);



    @Named("withKeyResults")
    public BusinessUnitObjectiveDto businessUnitObjectiveToDtoIncludeKeyResults(BusinessUnitObjective businessUnitObjective){

        BusinessUnitObjectiveDto businessUnitObjectiveDto = businessUnitObjectiveToDto(businessUnitObjective);
        businessUnitObjectiveDto.setBusinessUnitKeyResults(businessUnitKeyResultMapper.businessUnitKeyResultSetToDto(businessUnitObjective.getBusinessUnitKeyResults()));
        return businessUnitObjectiveDto;
    }
    @Named("withKeyResults")
    public Set<BusinessUnitObjectiveDto> businessUnitObjectiveSetToDtoIncludeKeyResults(Set<BusinessUnitObjective> businessUnitObjectives)
    {
        if(businessUnitObjectives == null)
            return null;
        Set<BusinessUnitObjectiveDto> set = new HashSet<>(businessUnitObjectives.size());
        for (BusinessUnitObjective businessUnitObjective: businessUnitObjectives) {
            set.add(businessUnitObjectiveToDtoIncludeKeyResults(businessUnitObjective));
        }
        return set;
    }

}
