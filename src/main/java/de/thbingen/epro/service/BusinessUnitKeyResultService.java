package de.thbingen.epro.service;

import de.thbingen.epro.controller.assembler.BusinessUnitKeyResultAssembler;
import de.thbingen.epro.model.business.BusinessUnitKeyResult;
import de.thbingen.epro.model.dto.BusinessUnitKeyResultDto;
import de.thbingen.epro.model.dto.BusinessUnitObjectiveDto;
import de.thbingen.epro.model.mapper.BusinessUnitKeyResultMapper;
import de.thbingen.epro.repository.BusinessUnitKeyResultRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

@Service
public class BusinessUnitKeyResultService {

    private final BusinessUnitKeyResultRepository businessUnitKeyResultRepository;
    private final BusinessUnitKeyResultMapper businessUnitKeyResultMapper;
    private final BusinessUnitKeyResultAssembler businessUnitKeyResultAssembler;

    public BusinessUnitKeyResultService(BusinessUnitKeyResultRepository businessUnitKeyResultRepository, BusinessUnitKeyResultMapper businessUnitKeyResultMapper, BusinessUnitKeyResultAssembler businessUnitKeyResultAssembler) {
        this.businessUnitKeyResultRepository = businessUnitKeyResultRepository;
        this.businessUnitKeyResultMapper = businessUnitKeyResultMapper;
        this.businessUnitKeyResultAssembler = businessUnitKeyResultAssembler;
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

        if(pagedResult.hasContent()) {
            return pagedResult.map(businessUnitKeyResultAssembler::toModel);
        } else {
            return Page.empty();
        }
    }

    public BusinessUnitKeyResultDto saveBusinessUnitKeyResultWithObjective(BusinessUnitKeyResultDto businessUnitKeyResultDto, BusinessUnitObjectiveDto businessUnitObjectiveDto) {
        BusinessUnitKeyResult businessUnitKeyResult = businessUnitKeyResultMapper.dtoToBusinessUnitKeyResult(businessUnitKeyResultDto, businessUnitObjectiveDto);
        BusinessUnitKeyResultDto test =  businessUnitKeyResultAssembler.toModel(businessUnitKeyResultRepository.save(businessUnitKeyResult));
        return test;
    }

    public BusinessUnitKeyResultDto saveBusinessUnitKeyResult(BusinessUnitKeyResultDto businessUnitKeyResultDto) {
        return null;
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

}
