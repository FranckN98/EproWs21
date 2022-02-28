package de.thbingen.epro.service;

import de.thbingen.epro.model.assembler.BusinessUnitKeyResultAssembler;
import de.thbingen.epro.model.dto.BusinessUnitKeyResultDto;
import de.thbingen.epro.model.dto.BusinessUnitKeyResultPostDto;
import de.thbingen.epro.model.dto.BusinessUnitKeyResultUpdateDto;
import de.thbingen.epro.model.dto.CompanyKeyResultDto;
import de.thbingen.epro.model.entity.BusinessUnitKeyResult;
import de.thbingen.epro.model.entity.CompanyKeyResult;
import de.thbingen.epro.model.mapper.BusinessUnitKeyResultMapper;
import de.thbingen.epro.repository.BusinessUnitKeyResultRepository;
import de.thbingen.epro.repository.BusinessUnitObjectiveRepository;
import de.thbingen.epro.repository.CompanyKeyResultRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

/**
 * This Service represents the interface between presentation logic and the data layer for everything related to
 * {@link BusinessUnitKeyResult} and {@link BusinessUnitKeyResultDto}.
 */
@Service
public class BusinessUnitKeyResultService {

    private final BusinessUnitKeyResultRepository businessUnitKeyResultRepository;
    private final BusinessUnitKeyResultMapper businessUnitKeyResultMapper;
    private final BusinessUnitKeyResultAssembler businessUnitKeyResultAssembler;
    private final CompanyKeyResultRepository companyKeyResultRepository;
    private final BusinessUnitObjectiveRepository businessUnitObjectiveRepository;

    /**
     * Default constructor to be used for Constructor Injection
     *
     * @param businessUnitKeyResultRepository The Repository for DB access to {@link BusinessUnitKeyResult}s
     * @param businessUnitKeyResultMapper     The Mapstruct mapper to convert from DTO to entity and back
     * @param businessUnitKeyResultAssembler  The RepresentationModelAssembler to add the hateoas relations
     * @param companyKeyResultRepository      The Repository for DB access to {@link CompanyKeyResult}s
     * @param businessUnitObjectiveRepository The Repository for DB access to {@link de.thbingen.epro.model.entity.BusinessUnitObjective}s
     */
    public BusinessUnitKeyResultService(BusinessUnitKeyResultRepository businessUnitKeyResultRepository, BusinessUnitKeyResultMapper businessUnitKeyResultMapper, BusinessUnitKeyResultAssembler businessUnitKeyResultAssembler, CompanyKeyResultRepository companyKeyResultRepository, BusinessUnitObjectiveRepository businessUnitObjectiveRepository) {
        this.businessUnitKeyResultRepository = businessUnitKeyResultRepository;
        this.businessUnitKeyResultMapper = businessUnitKeyResultMapper;
        this.businessUnitKeyResultAssembler = businessUnitKeyResultAssembler;
        this.companyKeyResultRepository = companyKeyResultRepository;
        this.businessUnitObjectiveRepository = businessUnitObjectiveRepository;
    }

    /**
     * Returns all {@link BusinessUnitKeyResult}s present in the DB as a Page of {@link BusinessUnitKeyResultDto}s
     *
     * @param pageable Pagination information to request a specific {@link Page}
     * @return a Page of {@link BusinessUnitKeyResultDto}s
     */
    public Page<BusinessUnitKeyResultDto> findAllBusinessUnitKeyResults(Pageable pageable) {
        Page<BusinessUnitKeyResult> pagedResult = businessUnitKeyResultRepository.findAll(pageable);

        if (pagedResult.hasContent()) {
            return pagedResult.map(businessUnitKeyResultAssembler::toModel);
        } else {
            return Page.empty();
        }
    }

    /**
     * Returns all {@link BusinessUnitKeyResult}s belonging to a certain {@link de.thbingen.epro.model.entity.BusinessUnitObjective} as a
     * Page of {@link CompanyKeyResultDto}s.
     *
     * @param businessUnitObjectiveId The {@code id} of the {@link de.thbingen.epro.model.entity.BusinessUnitObjective} for which to search the {@link BusinessUnitKeyResult}s
     * @param pageable                Pagination information to request a specific {@link Page} of {@link BusinessUnitKeyResult}s
     * @return The requested {@link Page} of {@link BusinessUnitKeyResult}s
     */
    public Page<BusinessUnitKeyResultDto> findAllByBusinessUnitObjectiveId(Long businessUnitObjectiveId, Pageable pageable) {
        Page<BusinessUnitKeyResult> pagedResult =
                businessUnitKeyResultRepository.findAllByBusinessUnitObjectiveId(businessUnitObjectiveId, pageable);

        if (pagedResult.hasContent()) {
            return pagedResult.map(businessUnitKeyResultAssembler::toModel);
        } else {
            return Page.empty();
        }
    }

    /**
     * Inserts the given {@link BusinessUnitKeyResult} and adds it to the {@link de.thbingen.epro.model.entity.BusinessUnitObjective}
     * with the given {@code id}.
     *
     * @param businessUnitKeyResultDto The {@link BusinessUnitKeyResultDto} to use for inserting
     * @param id                       The {@code id} of the {@link de.thbingen.epro.model.entity.BusinessUnitObjective} to which the new
     *                                 {@link BusinessUnitKeyResult} is to be added
     * @return The just inserted {@link BusinessUnitKeyResult} in it's {@link BusinessUnitKeyResultDto} representation
     */
    public BusinessUnitKeyResultDto insertBusinessUnitKeyResultWithObjective(BusinessUnitKeyResultPostDto businessUnitKeyResultDto, Long id) {
        BusinessUnitKeyResult businessUnitKeyResult = businessUnitKeyResultMapper.postDtoToBusinessUnitKeyResult(businessUnitKeyResultDto);
        businessUnitKeyResult.setBusinessUnitObjective(businessUnitObjectiveRepository.getById(id));
        return businessUnitKeyResultAssembler.toModel(businessUnitKeyResultRepository.save(businessUnitKeyResult));
    }

    /**
     * Updates the {@link BusinessUnitKeyResult} in the database with the given {@code id}, using the values from the given {@link BusinessUnitKeyResultDto}
     *
     * @param id                       The {@code id} of the {@link BusinessUnitKeyResult}, which is to be updated
     * @param businessUnitKeyResultDto The new data, with which a {@link BusinessUnitKeyResult} is to be updated
     * @return A {@link BusinessUnitKeyResultDto} of the new {@link BusinessUnitKeyResult}
     */
    public BusinessUnitKeyResultDto updateBusinessUnitKeyResult(Long id, BusinessUnitKeyResultUpdateDto businessUnitKeyResultDto) {
        Optional<BusinessUnitKeyResult> businessUnitKeyResultOptional = businessUnitKeyResultRepository.findById(id);
        BusinessUnitKeyResult businessUnitKeyResult = businessUnitKeyResultOptional.orElseThrow(() -> new EntityNotFoundException("No BusinessUnitKeyResult with this id exists"));
        businessUnitKeyResultMapper.updateBusinessUnitKeyResultFromUpdateDto(businessUnitKeyResultDto, businessUnitKeyResult);
        return businessUnitKeyResultAssembler.toModel(businessUnitKeyResultRepository.save(businessUnitKeyResult));
    }

    /**
     * Delete the {@link BusinessUnitKeyResult} with the given {@code id} from the DB
     *
     * @param id the {@code id} for which a role shall be deleted
     */
    public void deleteById(Long id) {
        businessUnitKeyResultRepository.deleteById(id);
    }

    /**
     * Checks whether a {@link BusinessUnitKeyResult} with the given {@code id} exists in the DB
     *
     * @param id The {@code id} for which the existence check in the DB will be executed
     * @return true if a {@link BusinessUnitKeyResult} for the given {@code id} exists, false if there is no {@link BusinessUnitKeyResult} for the given {@code id}
     */
    public boolean existsById(Long id) {
        return businessUnitKeyResultRepository.existsById(id);
    }

    /**
     * Returns the {@link BusinessUnitKeyResult} with the corresponding {@code id}.
     * If there is no {@link BusinessUnitKeyResult} with the given {@code id}, an empty {@link Optional} will be returned
     *
     * @param id the requested {@code id}
     * @return an {@link Optional} of the {@link BusinessUnitKeyResultDto}, which corresponds to the {@link BusinessUnitKeyResult} with the given {@code id}
     */
    public Optional<BusinessUnitKeyResultDto> findById(Long id) {
        Optional<BusinessUnitKeyResult> optional = businessUnitKeyResultRepository.findById(id);
        return optional.map(businessUnitKeyResultAssembler::toModel);
    }

    /**
     * Adds a reference to the {@link CompanyKeyResult} with the given {@code companyKeyResultId} to the
     * {@link BusinessUnitKeyResult} with the given {@code businessUnitKeyResultId}
     *
     * @param businessUnitKeyResultId The id of the {@link BusinessUnitKeyResult} which will get the reference added
     * @param companyKeyResultId      The id of the {@link CompanyKeyResult} which will be referenced
     * @return {@code true} if the reference could be created, {@code false} if the reference could not be created
     */
    public boolean referenceCompanyKeyResult(Long businessUnitKeyResultId, Long companyKeyResultId) {
        BusinessUnitKeyResult businessUnitKeyResult = businessUnitKeyResultRepository.findById(businessUnitKeyResultId).orElse(null);
        CompanyKeyResult companyKeyResult = companyKeyResultRepository.findById(companyKeyResultId).orElse(null);

        if (businessUnitKeyResult == null || companyKeyResult == null)
            return false;

        businessUnitKeyResult.setCompanyKeyResult(companyKeyResult);
        businessUnitKeyResultRepository.save(businessUnitKeyResult);
        return true;
    }

    /**
     * Deletes the reference to a {@link CompanyKeyResult} from the {@link BusinessUnitKeyResult} with the given {@code businessUnitKeyResultId}
     *
     * @param businessUnitKeyResultId The id of the {@link BusinessUnitKeyResult} for which the reference will be deleted
     * @return {@code true} if the reference could be deleted, {@code false} if the reference could not be deleted
     */
    public boolean deleteCompanyKeyResultReference(Long businessUnitKeyResultId) {
        BusinessUnitKeyResult businessUnitKeyResult = businessUnitKeyResultRepository.findById(businessUnitKeyResultId).orElse(null);

        if (businessUnitKeyResult == null)
            return false;

        businessUnitKeyResult.setCompanyKeyResult(null);
        businessUnitKeyResultRepository.save(businessUnitKeyResult);
        return true;
    }
}
