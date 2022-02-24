package de.thbingen.epro.service;

import de.thbingen.epro.model.assembler.BusinessUnitKeyResultAssembler;
import de.thbingen.epro.model.dto.BusinessUnitKeyResultDto;
import de.thbingen.epro.model.entity.BusinessUnitKeyResult;
import de.thbingen.epro.model.entity.CompanyKeyResult;
import de.thbingen.epro.model.mapper.BusinessUnitKeyResultMapper;
import de.thbingen.epro.repository.BusinessUnitKeyResultRepository;
import de.thbingen.epro.repository.BusinessUnitObjectiveRepository;
import de.thbingen.epro.repository.CompanyKeyResultRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BusinessUnitKeyResultService {

    private final BusinessUnitKeyResultRepository businessUnitKeyResultRepository;
    private final BusinessUnitKeyResultMapper businessUnitKeyResultMapper;
    private final BusinessUnitKeyResultAssembler businessUnitKeyResultAssembler;
    private final CompanyKeyResultRepository companyKeyResultRepository;
    private final BusinessUnitObjectiveRepository businessUnitObjectiveRepository;

    public BusinessUnitKeyResultService(BusinessUnitKeyResultRepository businessUnitKeyResultRepository, BusinessUnitKeyResultMapper businessUnitKeyResultMapper, BusinessUnitKeyResultAssembler businessUnitKeyResultAssembler, CompanyKeyResultRepository companyKeyResultRepository, BusinessUnitObjectiveRepository businessUnitObjectiveRepository) {
        this.businessUnitKeyResultRepository = businessUnitKeyResultRepository;
        this.businessUnitKeyResultMapper = businessUnitKeyResultMapper;
        this.businessUnitKeyResultAssembler = businessUnitKeyResultAssembler;
        this.companyKeyResultRepository = companyKeyResultRepository;
        this.businessUnitObjectiveRepository = businessUnitObjectiveRepository;
    }

    public Page<BusinessUnitKeyResultDto> getAllBusinessUnitKeyResults(Pageable pageable) {
        Page<BusinessUnitKeyResult> pagedResult = businessUnitKeyResultRepository.findAll(pageable);

        if (pagedResult.hasContent()) {
            return pagedResult.map(businessUnitKeyResultAssembler::toModel);
        } else {
            return Page.empty();
        }
    }

    public Page<BusinessUnitKeyResultDto> getAllByBusinessUnitObjectiveId(Long businesUnitObjectiveId, Pageable pageable) {
        Page<BusinessUnitKeyResult> pagedResult =
                businessUnitKeyResultRepository.findAllByBusinessUnitObjectiveId(businesUnitObjectiveId, pageable);

        if (pagedResult.hasContent()) {
            return pagedResult.map(businessUnitKeyResultAssembler::toModel);
        } else {
            return Page.empty();
        }
    }

    public BusinessUnitKeyResultDto insertBusinessUnitKeyResultWithObjective(BusinessUnitKeyResultDto businessUnitKeyResultDto, Long id) {
        BusinessUnitKeyResult businessUnitKeyResult = businessUnitKeyResultMapper.dtoToBusinessUnitKeyResult(businessUnitKeyResultDto);
        businessUnitKeyResult.setBusinessUnitObjective(businessUnitObjectiveRepository.getById(id));
        return businessUnitKeyResultAssembler.toModel(businessUnitKeyResultRepository.save(businessUnitKeyResult));
    }

    public BusinessUnitKeyResultDto saveBusinessUnitKeyResult(BusinessUnitKeyResultDto businessUnitKeyResultDto) {
        return updateBusinessUnitKeyResult(null, businessUnitKeyResultDto);
    }

    public BusinessUnitKeyResultDto updateBusinessUnitKeyResult(Long id, BusinessUnitKeyResultDto businessUnitKeyResultDto) {
        BusinessUnitKeyResult businessUnitKeyResult = businessUnitKeyResultRepository.getById(id);
        businessUnitKeyResultMapper.updateBusinessUnitKeyResultFromDto(businessUnitKeyResultDto, businessUnitKeyResult);
        return businessUnitKeyResultAssembler.toModel(businessUnitKeyResultRepository.save(businessUnitKeyResult));
    }

    public void deleteById(Long id) {
        businessUnitKeyResultRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return businessUnitKeyResultRepository.existsById(id);
    }

    public Optional<BusinessUnitKeyResultDto> findById(Long id) {
        Optional<BusinessUnitKeyResult> optional = businessUnitKeyResultRepository.findById(id);
        return optional.map(businessUnitKeyResultAssembler::toModel);
    }

    public boolean referenceCompanyKeyResult(Long businessUnitKeyResultId, Long companyKeyResultId) {
        BusinessUnitKeyResult businessUnitKeyResult = businessUnitKeyResultRepository.findById(businessUnitKeyResultId).orElse(null);
        CompanyKeyResult companyKeyResult = companyKeyResultRepository.findById(companyKeyResultId).orElse(null);

        if (businessUnitKeyResult == null || companyKeyResult == null)
            return false;

        businessUnitKeyResult.setCompanyKeyResult(companyKeyResult);
        businessUnitKeyResultRepository.save(businessUnitKeyResult);
        return true;
    }

    public boolean deleteCompanyKeyResultReference(Long businessUnitKeyResultId) {
        BusinessUnitKeyResult businessUnitKeyResult = businessUnitKeyResultRepository.findById(businessUnitKeyResultId).orElse(null);

        if (businessUnitKeyResult == null)
            return false;

        businessUnitKeyResult.setCompanyKeyResult(null);
        businessUnitKeyResultRepository.save(businessUnitKeyResult);
        return true;
    }
}
