package de.thbingen.epro.service;

import de.thbingen.epro.model.assembler.BusinessUnitKeyResultHistoryAssembler;
import de.thbingen.epro.model.dto.BusinessUnitKeyResultHistoryDto;
import de.thbingen.epro.model.entity.BusinessUnitKeyResultHistory;
import de.thbingen.epro.model.entity.CompanyKeyResult;
import de.thbingen.epro.model.mapper.BusinessUnitKeyResultHistoryMapper;
import de.thbingen.epro.repository.BusinessUnitKeyResultHistoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * This Service represents the interface between presentation logic and the data layer for everything related to
 * {@link BusinessUnitKeyResultHistory} and {@link BusinessUnitKeyResultHistoryDto}.
 */
@Service
public class BusinessUnitKeyResultHistoryService {

    private final BusinessUnitKeyResultHistoryRepository businessUnitKeyResultHistoryRepository;
    private final BusinessUnitKeyResultHistoryAssembler assembler;

    /**
     * Default constructor to be used for Constructor Injection
     *
     * @param businessUnitKeyResultHistoryRepository The Repository for DB access
     * @param assembler                              The RepresentationModelAssembler to add the hateoas relations
     */
    public BusinessUnitKeyResultHistoryService(BusinessUnitKeyResultHistoryRepository businessUnitKeyResultHistoryRepository, BusinessUnitKeyResultHistoryMapper businessUnitKeyResultHistoryMapper, BusinessUnitKeyResultHistoryAssembler assembler) {
        this.businessUnitKeyResultHistoryRepository = businessUnitKeyResultHistoryRepository;
        this.assembler = assembler;
    }

    /**
     * Returns all {@link BusinessUnitKeyResultHistory}s present in the DB as a Page of {@link BusinessUnitKeyResultHistoryDto}s
     *
     * @param pageable Pagination information to request a specific {@link Page}
     * @return a Page of {@link BusinessUnitKeyResultHistoryDto}s
     */
    public Page<BusinessUnitKeyResultHistoryDto> findAll(Pageable pageable) {
        Page<BusinessUnitKeyResultHistory> pagedResult = businessUnitKeyResultHistoryRepository.findAll(pageable);

        if (pagedResult.hasContent()) {
            return pagedResult.map(assembler::toModel);
        } else {
            return Page.empty();
        }
    }

    /**
     * Returns the {@link BusinessUnitKeyResultHistory} with the corresponding {@code id}.
     * If there is no {@link BusinessUnitKeyResultHistory} with the given {@code id}, an empty {@link Optional} will be returned
     *
     * @param id the requested {@code id}
     * @return an {@link Optional} of the {@link BusinessUnitKeyResultHistoryDto}, which corresponds to the {@link BusinessUnitKeyResultHistory} with the given {@code id}
     */
    public Optional<BusinessUnitKeyResultHistoryDto> findById(Long id) {
        Optional<BusinessUnitKeyResultHistory> businessUnitKeyResultHistory = businessUnitKeyResultHistoryRepository.findById(id);
        return businessUnitKeyResultHistory.map(assembler::toModel);
    }

    /**
     * Returns all {@link BusinessUnitKeyResultHistory}s belonging to a certain {@link CompanyKeyResult} as a
     * Page of {@link BusinessUnitKeyResultHistoryDto}s.
     *
     * @param id       The {@code id} of the {@link CompanyKeyResult} for which to search the {@link BusinessUnitKeyResultHistory}s
     * @param pageable Pagination information to request a specific {@link Page} of {@link BusinessUnitKeyResultHistory}s
     * @return The requested {@link Page} of {@link BusinessUnitKeyResultHistory}s
     */
    public Page<BusinessUnitKeyResultHistoryDto> getAllByBusinessUnitKeyResultId(Long id, Pageable pageable) {
        Page<BusinessUnitKeyResultHistory> pagedResult = businessUnitKeyResultHistoryRepository.findAllByCurrentBusinessUnitKeyResultIdOrderByChangeTimeStampDesc(id, pageable);

        if (pagedResult.hasContent()) {
            return pagedResult.map(assembler::toModel);
        } else {
            return Page.empty();
        }
    }
}
