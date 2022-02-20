package de.thbingen.epro.service;

import de.thbingen.epro.controller.assembler.BusinessUnitObjectiveAssembler;
import de.thbingen.epro.model.business.BusinessUnitObjective;
import de.thbingen.epro.model.dto.BusinessUnitDto;
import de.thbingen.epro.model.dto.BusinessUnitObjectiveDto;
import de.thbingen.epro.model.mapper.BusinessUnitObjectiveMapper;
import de.thbingen.epro.repository.BusinessUnitObjectiveRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BusinessUnitObjectiveService {

    private final BusinessUnitObjectiveRepository businessUnitObjectiveRepository;
    private final BusinessUnitObjectiveMapper businessUnitObjectiveMapper;
    private final BusinessUnitObjectiveAssembler assembler;

    public BusinessUnitObjectiveService(BusinessUnitObjectiveRepository businessUnitObjectiveRepository, BusinessUnitObjectiveMapper businessUnitObjectiveMapper, BusinessUnitObjectiveAssembler assembler) {
        this.businessUnitObjectiveRepository = businessUnitObjectiveRepository;
        this.businessUnitObjectiveMapper = businessUnitObjectiveMapper;
        this.assembler = assembler;
    }

    public Page<BusinessUnitObjectiveDto> getAllByBusinessUnitId(Long businessUnitId, Pageable pageable) {
        Page<BusinessUnitObjective> pagedResult = businessUnitObjectiveRepository.findAllByBusinessUnitId(businessUnitId, pageable);

        if (pagedResult.hasContent()) {
            return pagedResult.map(assembler::toModel);
        } else {
            return Page.empty();
        }
    }

    public Page<BusinessUnitObjectiveDto> getAllBusinessUnitObjectives(Pageable pageable) {
        Page<BusinessUnitObjective> pagedResult = businessUnitObjectiveRepository.findAll(pageable);

        if (pagedResult.hasContent()) {
            return pagedResult.map(assembler::toModel);
        } else {
            return Page.empty();
        }
    }

    public BusinessUnitObjectiveDto saveBusinessUnitObjective(BusinessUnitObjectiveDto businessUnitObjectiveDto) {
        BusinessUnitObjective businessUnitObjective = businessUnitObjectiveMapper.dtoToBusinessUnitObjective(businessUnitObjectiveDto);
        return assembler.toModel(businessUnitObjectiveRepository.save(businessUnitObjective));
    }

    public BusinessUnitObjectiveDto saveBusinessUnitObjectiveWithBusinessUnit(BusinessUnitObjectiveDto businessUnitObjectiveDto, BusinessUnitDto businessUnitDto) {
        BusinessUnitObjective businessUnitObjective = businessUnitObjectiveMapper.dtoToBusinessUnitObjective(businessUnitObjectiveDto, businessUnitDto);
        return assembler.toModel(businessUnitObjectiveRepository.save(businessUnitObjective));
    }

    public Optional<BusinessUnitObjectiveDto> findById(Long id) {
        Optional<BusinessUnitObjective> optional = businessUnitObjectiveRepository.findById(id);
        return optional.map(assembler::toModel);
    }

    public void deleteById(Long id) {
        businessUnitObjectiveRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return businessUnitObjectiveRepository.existsById(id);
    }

}
