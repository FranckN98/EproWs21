package de.thbingen.epro.service;

import de.thbingen.epro.model.business.BusinessUnitObjective;
import de.thbingen.epro.model.dto.BusinessUnitDto;
import de.thbingen.epro.model.dto.BusinessUnitObjectiveDto;
import de.thbingen.epro.model.mapper.BusinessUnitObjectiveMapper;
import de.thbingen.epro.repository.BusinessUnitObjectiveRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

@Service
public class BusinessUnitObjectiveService {

    private final BusinessUnitObjectiveRepository businessUnitObjectiveRepository;
    private final BusinessUnitObjectiveMapper businessUnitObjectiveMapper;

    public BusinessUnitObjectiveService(BusinessUnitObjectiveRepository businessUnitObjectiveRepository, BusinessUnitObjectiveMapper businessUnitObjectiveMapper) {
        this.businessUnitObjectiveRepository = businessUnitObjectiveRepository;
        this.businessUnitObjectiveMapper = businessUnitObjectiveMapper;
    }

    public Set<BusinessUnitObjectiveDto> getAllByBusinessUnitId(
            Long businessUnitId
    ) {
        return businessUnitObjectiveMapper.businessUnitObjectiveSetToDtoSet(businessUnitObjectiveRepository.findAllByBusinessUnitId(businessUnitId));
    }

    public Set<BusinessUnitObjectiveDto> getAllBusinessUnitObjectives(Integer pageNo, Integer pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        Page<BusinessUnitObjective> pagedResult = businessUnitObjectiveRepository.findAll(paging);

        if (pagedResult.hasContent()) {
            return businessUnitObjectiveMapper.businessUnitObjectiveSetToDtoSet(pagedResult.toSet());
        } else {
            return Collections.emptySet();
        }
    }

    public BusinessUnitObjectiveDto saveBusinessUnitObjective(BusinessUnitObjectiveDto businessUnitObjectiveDto) {
        BusinessUnitObjective businessUnitObjective = businessUnitObjectiveMapper.dtoToBusinessUnitObjective(businessUnitObjectiveDto);
        return businessUnitObjectiveMapper.businessUnitObjectiveToDto(businessUnitObjectiveRepository.save(businessUnitObjective));
    }

    public BusinessUnitObjectiveDto saveBusinessUnitObjectiveWithBusinessUnit(BusinessUnitObjectiveDto businessUnitObjectiveDto, BusinessUnitDto businessUnitDto) {
        BusinessUnitObjective businessUnitObjective = businessUnitObjectiveMapper.dtoToBusinessUnitObjective(businessUnitObjectiveDto, businessUnitDto);
        return businessUnitObjectiveMapper.businessUnitObjectiveToDto(businessUnitObjectiveRepository.save(businessUnitObjective));
    }

    public Optional<BusinessUnitObjectiveDto> findById(Long id) {
        Optional<BusinessUnitObjective> optional = businessUnitObjectiveRepository.findById(id);
        return optional.map(businessUnitObjectiveMapper::businessUnitObjectiveToDtoIncludeKeyResults);
    }

    public void deleteById(Long id) {
        businessUnitObjectiveRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return businessUnitObjectiveRepository.existsById(id);
    }

}
