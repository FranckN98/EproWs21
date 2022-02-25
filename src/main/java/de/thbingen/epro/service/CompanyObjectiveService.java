package de.thbingen.epro.service;

import de.thbingen.epro.model.assembler.CompanyObjectiveAssembler;
import de.thbingen.epro.model.dto.CompanyObjectiveDto;
import de.thbingen.epro.model.entity.CompanyObjective;
import de.thbingen.epro.model.mapper.CompanyObjectiveMapper;
import de.thbingen.epro.repository.CompanyObjectiveRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

/**
 * This Service represents the interface between presentation logic and the data layer for everything related to
 * {@link CompanyObjective} and {@link CompanyObjectiveDto}.
 */
@Service
public class CompanyObjectiveService {

    private final CompanyObjectiveRepository companyObjectiveRepository;
    private final CompanyObjectiveMapper companyObjectiveMapper;
    private final CompanyObjectiveAssembler companyObjectiveAssembler;

    /**
     * Default constructor to be used for Constructor Injection
     *
     * @param companyObjectiveRepository The Repository for DB access
     * @param companyObjectiveMapper     The Mapstruct mapper to convert from DTO to entity and back
     * @param companyObjectiveAssembler  The RepresentationModelAssembler to add the hateoas relations
     */
    public CompanyObjectiveService(CompanyObjectiveRepository companyObjectiveRepository, CompanyObjectiveMapper companyObjectiveMapper, CompanyObjectiveAssembler companyObjectiveAssembler) {
        this.companyObjectiveRepository = companyObjectiveRepository;
        this.companyObjectiveMapper = companyObjectiveMapper;
        this.companyObjectiveAssembler = companyObjectiveAssembler;
    }

    /**
     * Returns all {@link CompanyObjective}s, which have a {@code startDate} after the given {@code startDate} parameter
     * <b>and</b> have an {@code endDate} before the given {@code endDate} parameter as a Page of {@link CompanyObjectiveDto}s
     *
     * @param pageable  Pagination information, may be null
     * @param startDate The {@link LocalDate} which is used to find only {@link CompanyObjective}s with a {@code startDate}
     *                  after this Date
     * @param endDate   The {@link LocalDate} which is used to find only {@link CompanyObjective}s with a {@code endDate}
     *                  before this Date
     * @return a Page of {@link CompanyObjectiveDto}s
     */
    public Page<CompanyObjectiveDto> getAllCompanyObjectives(Pageable pageable, LocalDate startDate, LocalDate endDate) {
        Page<CompanyObjective> pagedResult = companyObjectiveRepository.findAllByStartDateAfterAndEndDateBefore(startDate, endDate, pageable);

        if (pagedResult.hasContent()) {
            return pagedResult.map(companyObjectiveAssembler::toModel);
        } else {
            return Page.empty();
        }
    }

    /**
     * Returns the {@link CompanyObjective} with the corresponding {@code id}.
     * If there is no {@link CompanyObjective} with the given {@code id}, an empty {@link Optional} will be returned
     *
     * @param id the requested {@code id}
     * @return an {@link Optional} of the {@link CompanyObjectiveDto}, which corresponds to the {@link CompanyObjective} with the given {@code id}
     */
    public Optional<CompanyObjectiveDto> findById(Long id) {
        Optional<CompanyObjective> optional = companyObjectiveRepository.findById(id);
        return optional.map(companyObjectiveMapper::companyObjectiveToDto);
    }

    /**
     * Creates a new {@link CompanyObjective} in the DB using the data from the {@link CompanyObjectiveDto}
     *
     * @param companyObjectiveDto The new data, with which an {@link CompanyObjective} is to be inserted
     * @return An {@link CompanyObjectiveDto} of the new {@link CompanyObjective}
     */
    public CompanyObjectiveDto insertCompanyObjective(CompanyObjectiveDto companyObjectiveDto) {
        CompanyObjective companyObjective = companyObjectiveMapper.dtoToCompanyObjective(companyObjectiveDto);
        return companyObjectiveAssembler.toModel(companyObjectiveRepository.save(companyObjective));
    }

    /**
     * Updates the {@link CompanyObjective} in the database with the given {@code id}, using the values from the given
     * {@link CompanyObjectiveDto}.
     *
     * @param id                  The {@code id} of the role, which is to be updated.
     * @param companyObjectiveDto The new data, with which a {@link CompanyObjective} is to be updated
     * @return A {@link CompanyObjectiveDto} of the new {@link CompanyObjective}
     */
    public CompanyObjectiveDto updateCompanyObjective(Long id, CompanyObjectiveDto companyObjectiveDto) {
        CompanyObjective companyObjective = companyObjectiveRepository.getById(id);
        companyObjectiveMapper.updateCompanyObjectiveFromDto(companyObjectiveDto, companyObjective);
        return companyObjectiveAssembler.toModel(companyObjectiveRepository.save(companyObjective));
    }

    /**
     * Checks whether a {@link CompanyObjective} with the given {@code id} exists in the DB
     *
     * @param id The {@code id} for which the existence check in the DB will be executed
     * @return true if a {@link CompanyObjective} for the given {@code id} exists, false if there is no
     * {@link CompanyObjective} for the given {@code id}
     */
    public boolean existsById(Long id) {
        return companyObjectiveRepository.existsById(id);
    }

    /**
     * Delete the {@link CompanyObjective} with the given {@code id} from the DB
     *
     * @param id the {@code id} for which a role shall be deleted
     */
    public void deleteById(Long id) {
        companyObjectiveRepository.deleteById(id);
    }
}
