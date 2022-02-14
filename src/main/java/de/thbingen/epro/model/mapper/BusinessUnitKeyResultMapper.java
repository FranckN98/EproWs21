package de.thbingen.epro.model.mapper;

import de.thbingen.epro.model.business.BusinessUnitKeyResult;
import de.thbingen.epro.model.dto.BusinessUnitKeyResultDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BusinessUnitKeyResultMapper {
    public BusinessUnitKeyResultDto BusinessUnitKeyResultToDto (BusinessUnitKeyResult businessUnitKeyResult);
}
