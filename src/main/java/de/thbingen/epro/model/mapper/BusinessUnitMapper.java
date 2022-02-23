package de.thbingen.epro.model.mapper;

import de.thbingen.epro.model.dto.BusinessUnitDto;
import de.thbingen.epro.model.entity.BusinessUnit;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BusinessUnitMapper {

    BusinessUnitDto businessUnitToDto(BusinessUnit businessUnit);

    BusinessUnit dtoToBusinessUnit(BusinessUnitDto businessUnitDto);

    List<BusinessUnitDto> businessUnitListToBusinessUnitDtoList(List<BusinessUnit> businessUnits);
}
