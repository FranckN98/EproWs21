package de.thbingen.epro.service;

import de.thbingen.epro.controller.assembler.BusinessUnitAssembler;
import de.thbingen.epro.model.business.BusinessUnit;
import de.thbingen.epro.model.dto.BusinessUnitDto;
import de.thbingen.epro.model.dto.BusinessUnitObjectiveDto;
import de.thbingen.epro.model.mapper.BusinessUnitMapper;
import de.thbingen.epro.model.mapper.BusinessUnitObjectiveMapper;
import de.thbingen.epro.repository.BusinessUnitObjectiveRepository;
import de.thbingen.epro.repository.BusinessUnitRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BusinessUnitService {

    private final BusinessUnitRepository businessUnitRepository;
    private final BusinessUnitMapper businessUnitMapper;
    private final BusinessUnitAssembler businessUnitAssembler;
    private final BusinessUnitObjectiveRepository businessUnitObjectiveRepository;
    private final BusinessUnitObjectiveMapper businessUnitObjectiveMapper;

    public BusinessUnitService(BusinessUnitRepository businessUnitRepository, BusinessUnitMapper businessUnitMapper, BusinessUnitAssembler businessUnitAssembler, BusinessUnitObjectiveRepository businessUnitObjectiveRepository, BusinessUnitObjectiveMapper businessUnitObjectiveMapper) {
        this.businessUnitRepository = businessUnitRepository;
        this.businessUnitMapper = businessUnitMapper;
        this.businessUnitAssembler = businessUnitAssembler;
        this.businessUnitObjectiveRepository = businessUnitObjectiveRepository;
        this.businessUnitObjectiveMapper = businessUnitObjectiveMapper;
    }

    public Page<BusinessUnitDto> findAll(
            int pageNo,
            int pageSize,
            String sortBy
    ) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        Page<BusinessUnit> pagedResult = businessUnitRepository.findAll(paging);

        if (pagedResult.hasContent()) {
            return pagedResult.map(businessUnitAssembler::toModel);
        } else {
            return Page.empty();
        }
    }

    public Optional<BusinessUnitDto> findById(Long id) {
        Optional<BusinessUnit> businessUnit = businessUnitRepository.findById(id);
        return businessUnit.map(businessUnitAssembler::toModel);
    }

    public BusinessUnitDto saveBusinessUnit(BusinessUnitDto businessUnitDto) {
        BusinessUnit businessUnit = businessUnitMapper.dtoToBusinessUnit(businessUnitDto);
        return businessUnitAssembler.toModel(businessUnitRepository.save(businessUnit));
    }

    public boolean existsById(Long id) {
        return businessUnitRepository.existsById(id);
    }

    public void deleteById(Long id) {
        businessUnitRepository.deleteById(id);
    }

    public BusinessUnitObjectiveDto saveBusinessUnitObjective(BusinessUnitObjectiveDto newBusinessUnitObj, BusinessUnitDto businessUnit) {
        return businessUnitObjectiveMapper.toDto(businessUnitObjectiveRepository.save(businessUnitObjectiveMapper.businessUnitToDto(newBusinessUnitObj, businessUnit)));
    }
}
