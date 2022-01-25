package de.thbingen.epro.model.mapper;

import de.thbingen.epro.model.business.BusinessUnit;
import de.thbingen.epro.model.dto.BusinessUnitDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BusinessUnitMapper {

    public BusinessUnitDto businessUnitToDto(BusinessUnit businessUnit);

    public BusinessUnit dtoToBusinessUnit(BusinessUnitDto businessUnitDto);

    public List<BusinessUnitDto> businessUnitListToBusinessUnitDtoList(List<BusinessUnit> businessUnits);
}
