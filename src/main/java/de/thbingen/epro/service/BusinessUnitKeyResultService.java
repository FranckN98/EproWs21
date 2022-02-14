package de.thbingen.epro.service;

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

    public BusinessUnitKeyResultService(BusinessUnitKeyResultRepository businessUnitKeyResultRepository, BusinessUnitKeyResultMapper businessUnitKeyResultMapper) {
        this.businessUnitKeyResultRepository = businessUnitKeyResultRepository;
        this.businessUnitKeyResultMapper = businessUnitKeyResultMapper;
    }
    public Set<BusinessUnitKeyResultDto> getAllBusinessUnitKeyResults(Integer pageNo, Integer pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        Page<BusinessUnitKeyResult> pagedResult = businessUnitKeyResultRepository.findAll(paging);

        if (pagedResult.hasContent()) {
            return businessUnitKeyResultMapper.businessUnitKeyResultSetToDto(pagedResult.toSet());
        } else {
            return Collections.emptySet();
        }
    }
    public BusinessUnitKeyResultDto saveBusinessUnitKeyResultWithObjective(BusinessUnitKeyResultDto businessUnitKeyResultDto, BusinessUnitObjectiveDto businessUnitObjectiveDto) {
        BusinessUnitKeyResult businessUnitKeyResult = businessUnitKeyResultMapper.dtoToBusinessUnitKeyResult(businessUnitKeyResultDto, businessUnitObjectiveDto);
        return businessUnitKeyResultMapper.businessUnitKeyResultToDtoWithoutObjective(businessUnitKeyResultRepository.save(businessUnitKeyResult));
    }
    public BusinessUnitKeyResultDto saveBusinessUnitKeyResult(BusinessUnitKeyResultDto businessUnitKeyResultDto) {
      return  null;
    }
    public void deleteById(Long id) {
        businessUnitKeyResultRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return businessUnitKeyResultRepository.existsById(id);
    }

    public Optional<BusinessUnitKeyResultDto> findById(Long id) {
        Optional<BusinessUnitKeyResult> optional = businessUnitKeyResultRepository.findById(id);
        return optional.map(businessUnitKeyResultMapper::businessUnitKeyResultToDtoWithoutObjective);
    }

}
