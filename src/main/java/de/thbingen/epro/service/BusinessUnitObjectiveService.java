package de.thbingen.epro.service;

import de.thbingen.epro.model.assembler.BusinessUnitObjectiveAssembler;
import de.thbingen.epro.model.dto.BusinessUnitObjectiveDto;
import de.thbingen.epro.model.entity.BusinessUnitObjective;
import de.thbingen.epro.model.entity.CompanyKeyResult;
import de.thbingen.epro.model.mapper.BusinessUnitObjectiveMapper;
import de.thbingen.epro.repository.BusinessUnitObjectiveRepository;
import de.thbingen.epro.repository.BusinessUnitRepository;
import de.thbingen.epro.repository.CompanyKeyResultRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BusinessUnitObjectiveService {

    private final BusinessUnitObjectiveRepository businessUnitObjectiveRepository;
    private final CompanyKeyResultRepository companyKeyResultRepository;
    private final BusinessUnitObjectiveMapper businessUnitObjectiveMapper;
    private final BusinessUnitObjectiveAssembler assembler;

    private final BusinessUnitRepository businessUnitRepository;

    public BusinessUnitObjectiveService(BusinessUnitObjectiveRepository businessUnitObjectiveRepository, CompanyKeyResultRepository companyKeyResultRepository, BusinessUnitObjectiveMapper businessUnitObjectiveMapper, BusinessUnitObjectiveAssembler assembler, BusinessUnitRepository businessUnitRepository) {
        this.businessUnitObjectiveRepository = businessUnitObjectiveRepository;
        this.companyKeyResultRepository = companyKeyResultRepository;
        this.businessUnitObjectiveMapper = businessUnitObjectiveMapper;
        this.assembler = assembler;
        this.businessUnitRepository = businessUnitRepository;
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

    public BusinessUnitObjectiveDto updateBusinessUnitObjective(Long id, BusinessUnitObjectiveDto businessUnitObjectiveDto) {
        BusinessUnitObjective businessUnitObjective = businessUnitObjectiveRepository.getById(id);
        businessUnitObjectiveMapper.updateBusinessUnitObjectiveFromDto(businessUnitObjectiveDto, businessUnitObjective);
        return assembler.toModel(businessUnitObjectiveRepository.save(businessUnitObjective));
    }

    public BusinessUnitObjectiveDto insertBusinessUnitObjectiveWithBusinessUnit(BusinessUnitObjectiveDto businessUnitObjectiveDto, Long businessUnitDto) {
        BusinessUnitObjective businessUnitObjective = businessUnitObjectiveMapper.dtoToBusinessUnitObjective(businessUnitObjectiveDto);
        businessUnitObjective.setBusinessUnit(businessUnitRepository.getById(businessUnitDto));
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

    public boolean referenceCompanyKeyResult(Long businessUnitObjectiveId, Long companyKeyResultId) {
        BusinessUnitObjective businessUnitObjective = businessUnitObjectiveRepository.findById(businessUnitObjectiveId).orElse(null);
        CompanyKeyResult companyKeyResult = companyKeyResultRepository.findById(companyKeyResultId).orElse(null);

        if (businessUnitObjective == null || companyKeyResult == null)
            return false;

        businessUnitObjective.setCompanyKeyResult(companyKeyResult);
        businessUnitObjectiveRepository.save(businessUnitObjective);
        return true;
    }

    public boolean deleteCompanyKeyResultReference(Long businessUnitObjectiveId) {
        BusinessUnitObjective businessUnitObjective = businessUnitObjectiveRepository.findById(businessUnitObjectiveId).orElse(null);

        if (businessUnitObjective == null)
            return false;

        businessUnitObjective.setCompanyKeyResult(null);
        businessUnitObjectiveRepository.save(businessUnitObjective);
        return true;
    }
}
