package de.thbingen.epro.service;

import de.thbingen.epro.model.assembler.CompanyKeyResultAssembler;
import de.thbingen.epro.model.dto.CompanyKeyResultDto;
import de.thbingen.epro.model.dto.CompanyKeyResultPostDto;
import de.thbingen.epro.model.dto.CompanyKeyResultUpdateDto;
import de.thbingen.epro.model.entity.CompanyKeyResult;
import de.thbingen.epro.model.mapper.CompanyKeyResultMapper;
import de.thbingen.epro.repository.CompanyKeyResultRepository;
import de.thbingen.epro.repository.CompanyObjectiveRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * This Service represents the interface between presentation logic and the data layer for everything related to
 * {@link CompanyKeyResult} and {@link CompanyKeyResultDto}.
 */
@Service
public class CompanyKeyResultService {

    private final CompanyKeyResultMapper companyKeyResultMapper;
    private final CompanyKeyResultRepository companyKeyResultRepository;
    private final CompanyKeyResultAssembler assembler;
    private final CompanyObjectiveRepository companyObjectiveRepository;

    /**
     * Default constructor to be used for Constructor Injection
     *
     * @param companyKeyResultMapper     The Mapstruct mapper to convert from DTO to entity and back
     * @param companyKeyResultRepository The Repository for DB access to {@link CompanyKeyResult}s
     * @param assembler                  The RepresentationModelAssembler to add the hateoas relations
     * @param companyObjectiveRepository The Repository for DB access to {@link de.thbingen.epro.model.entity.CompanyObjective}s
     */
    public CompanyKeyResultService(CompanyKeyResultMapper companyKeyResultMapper, CompanyKeyResultRepository companyKeyResultRepository, CompanyKeyResultAssembler assembler, CompanyObjectiveRepository companyObjectiveRepository) {
        this.companyKeyResultMapper = companyKeyResultMapper;
        this.companyKeyResultRepository = companyKeyResultRepository;
        this.assembler = assembler;
        this.companyObjectiveRepository = companyObjectiveRepository;
    }

    /**
     * Returns all {@link CompanyKeyResult}s present in the DB as a Page of {@link CompanyKeyResultDto}s
     *
     * @param pageable Pagination information to request a specific {@link Page}
     * @return a Page of {@link CompanyKeyResultDto}s
     */
    public Page<CompanyKeyResultDto> findAllCompanyKeyResults(Pageable pageable) {
        Page<CompanyKeyResult> pagedResult = companyKeyResultRepository.findAll(pageable);

        if (pagedResult.hasContent()) {
            return pagedResult.map(assembler::toModel);
        } else {
            return Page.empty();
        }
    }

    /**
     * Returns the {@link CompanyKeyResult} with the corresponding {@code id}.
     * If there is no {@link CompanyKeyResult} with the given {@code id}, an empty {@link Optional} will be returned
     *
     * @param id the requested {@code id}
     * @return an {@link Optional} of the {@link CompanyKeyResultDto}, which corresponds to the {@link CompanyKeyResult} with the given {@code id}
     */
    public Optional<CompanyKeyResultDto> findById(Long id) {
        Optional<CompanyKeyResult> optional = companyKeyResultRepository.findById(id);
        return optional.map(assembler::toModel);
    }

    /**
     * Returns all {@link CompanyKeyResult}s belonging to a certain {@link de.thbingen.epro.model.entity.CompanyObjective} as a
     * Page of {@link CompanyKeyResultDto}s.
     *
     * @param id       The {@code id} of the {@link de.thbingen.epro.model.entity.CompanyObjective} for which to search the {@link CompanyKeyResult}s
     * @param pageable Pagination information to request a specific {@link Page} of {@link CompanyKeyResult}s
     * @return The requested {@link Page} of {@link CompanyKeyResult}s
     */
    public Page<CompanyKeyResultDto> findAllByCompanyObjectiveId(Long id, Pageable pageable) {
        Page<CompanyKeyResult> pagedResult = companyKeyResultRepository.findAllByCompanyObjectiveId(id, pageable);

        if (pagedResult.hasContent()) {
            return pagedResult.map(assembler::toModel);
        }
        return Page.empty();
    }

    /**
     * Inserts the given {@link CompanyKeyResult} and adds it to the {@link de.thbingen.epro.model.entity.CompanyObjective}
     * with the given {@code id}.
     *
     * @param companyKeyResultDto The {@link CompanyKeyResultDto} to use for inserting
     * @param id                  The {@code id} of the {@link de.thbingen.epro.model.entity.CompanyObjective} to which the new
     *                            {@link CompanyKeyResult} is to be added
     * @return The just inserted {@link CompanyKeyResult} in it's {@link CompanyKeyResultDto} representation
     */
    public CompanyKeyResultDto insertCompanyKeyResultWithObjective(CompanyKeyResultPostDto companyKeyResultDto, Long id) {
        CompanyKeyResult companyKeyResult = companyKeyResultMapper.postDtoToCompanyKeyResult(companyKeyResultDto);
        companyKeyResult.setCompanyObjective(companyObjectiveRepository.getById(id));
        return assembler.toModel(companyKeyResultRepository.save(companyKeyResult));
    }

    /**
     * Updates the {@link CompanyKeyResult} in the database with the given {@code id}, using the values from the given {@link CompanyKeyResultDto}
     *
     * @param id                  The {@code id} of the {@link CompanyKeyResult}, which is to be updated
     * @param companyKeyResultDto The new data, with which a {@link CompanyKeyResult} is to be updated
     * @return A {@link CompanyKeyResultDto} of the new {@link CompanyKeyResult}
     */
    public CompanyKeyResultDto updateCompanyKeyResult(Long id, CompanyKeyResultUpdateDto companyKeyResultDto) {
        CompanyKeyResult companyKeyResult = companyKeyResultRepository.getById(id);
        companyKeyResultMapper.updateCompanyKeyResultFromUpdateDto(companyKeyResultDto, companyKeyResult);
        return assembler.toModel(companyKeyResultRepository.save(companyKeyResult));
    }

    /**
     * Checks whether a {@link CompanyKeyResult} with the given {@code id} exists in the DB
     *
     * @param id The {@code id} for which the existence check in the DB will be executed
     * @return true if a {@link CompanyKeyResult} for the given {@code id} exists, false if there is no {@link CompanyKeyResult} for the given {@code id}
     */
    public boolean existsById(Long id) {
        return companyKeyResultRepository.existsById(id);
    }

    /**
     * Delete the {@link CompanyKeyResult} with the given {@code id} from the DB
     *
     * @param id the {@code id} for which a role shall be deleted
     */
    public void deleteById(Long id) {
        companyKeyResultRepository.deleteById(id);
    }
}
