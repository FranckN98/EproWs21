package de.thbingen.epro.service;

import de.thbingen.epro.model.assembler.CompanyKeyResultHistoryAssembler;
import de.thbingen.epro.model.dto.CompanyKeyResultHistoryDto;
import de.thbingen.epro.model.entity.CompanyKeyResult;
import de.thbingen.epro.model.entity.CompanyKeyResultHistory;
import de.thbingen.epro.repository.CompanyKeyResultHistoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * This Service represents the interface between presentation logic and the data layer for everything related to
 * {@link CompanyKeyResultHistory} and {@link CompanyKeyResultHistoryDto}.
 */
@Service
public class CompanyKeyResultHistoryService {

    private final CompanyKeyResultHistoryRepository companyKeyResultHistoryRepository;
    private final CompanyKeyResultHistoryAssembler assembler;

    /**
     * Default constructor to be used for Constructor Injection
     *
     * @param companyKeyResultHistoryRepository The Repository for DB access
     * @param assembler                         The RepresentationModelAssembler to add the hateoas relations
     */
    public CompanyKeyResultHistoryService(CompanyKeyResultHistoryRepository companyKeyResultHistoryRepository, CompanyKeyResultHistoryAssembler assembler) {
        this.companyKeyResultHistoryRepository = companyKeyResultHistoryRepository;
        this.assembler = assembler;
    }

    /**
     * Returns all {@link CompanyKeyResultHistory}s present in the DB as a Page of {@link CompanyKeyResultHistoryDto}s
     *
     * @param pageable Pagination information to request a specific {@link Page}
     * @return a Page of {@link CompanyKeyResultHistoryDto}s
     */
    public Page<CompanyKeyResultHistoryDto> findAll(Pageable pageable) {
        Page<CompanyKeyResultHistory> pagedResult = companyKeyResultHistoryRepository.findAll(pageable);

        if (pagedResult.hasContent()) {
            return pagedResult.map(assembler::toModel);
        } else {
            return Page.empty();
        }
    }

    /**
     * Returns the {@link CompanyKeyResultHistory} with the corresponding {@code id}.
     * If there is no {@link CompanyKeyResultHistory} with the given {@code id}, an empty {@link Optional} will be returned
     *
     * @param id the requested {@code id}
     * @return an {@link Optional} of the {@link CompanyKeyResultHistoryDto}, which corresponds to the {@link CompanyKeyResultHistory} with the given {@code id}
     */
    public Optional<CompanyKeyResultHistoryDto> findById(Long id) {
        Optional<CompanyKeyResultHistory> businessUnitKeyResultHistory = companyKeyResultHistoryRepository.findById(id);
        return businessUnitKeyResultHistory.map(assembler::toModel);
    }

    /**
     * Returns all {@link CompanyKeyResultHistory}s belonging to a certain {@link CompanyKeyResult} as a
     * Page of {@link CompanyKeyResultHistoryDto}s.
     *
     * @param id       The {@code id} of the {@link CompanyKeyResult} for which to search the {@link CompanyKeyResultHistory}s
     * @param pageable Pagination information to request a specific {@link Page} of {@link CompanyKeyResultHistory}s
     * @return The requested {@link Page} of {@link CompanyKeyResultHistory}s
     */
    public Page<CompanyKeyResultHistoryDto> findAllByCompanyKeyResultId(Long id, Pageable pageable) {
        Page<CompanyKeyResultHistory> pagedResult = companyKeyResultHistoryRepository.findAllByCompanyKeyResultIdOrderByChangeTimeStampDesc(id, pageable);

        if (pagedResult.hasContent()) {
            return pagedResult.map(assembler::toModel);
        }
        return Page.empty();
    }
}
