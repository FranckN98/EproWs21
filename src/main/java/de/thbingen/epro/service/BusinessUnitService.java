package de.thbingen.epro.service;

import de.thbingen.epro.model.business.BusinessUnit;
import de.thbingen.epro.model.dto.BusinessUnitDto;
import de.thbingen.epro.model.mapper.BusinessUnitMapper;
import de.thbingen.epro.repository.BusinessUnitRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BusinessUnitService {

    private final BusinessUnitRepository businessUnitRepository;
    private final BusinessUnitMapper businessUnitMapper;

    public BusinessUnitService(BusinessUnitRepository businessUnitRepository, BusinessUnitMapper businessUnitMapper) {
        this.businessUnitRepository = businessUnitRepository;
        this.businessUnitMapper = businessUnitMapper;
    }

    public List<BusinessUnitDto> findAll() {
        List<BusinessUnit> businessUnits = businessUnitRepository.findAll();
        return businessUnitMapper.businessUnitListToBusinessUnitDtoList(businessUnits);
    }

    public Optional<BusinessUnitDto> findById(Long id) {
        Optional<BusinessUnit> businessUnit = businessUnitRepository.findById(id);
        return businessUnit.map(businessUnitMapper::businessUnitToDto);
    }

    public BusinessUnitDto saveBusinessUnit(BusinessUnitDto businessUnitDto) {
        BusinessUnit businessUnit = businessUnitMapper.dtoToBusinessUnit(businessUnitDto);
        return businessUnitMapper.businessUnitToDto(businessUnitRepository.save(businessUnit));
    }

    public boolean existsById(Long id) {
        return businessUnitRepository.existsById(id);
    }

    public void deleteById(Long id) {
        businessUnitRepository.deleteById(id);
    }
}
