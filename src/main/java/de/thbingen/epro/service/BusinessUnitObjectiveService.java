package de.thbingen.epro.service;

import de.thbingen.epro.model.assembler.BusinessUnitObjectiveAssembler;
import de.thbingen.epro.model.dto.BusinessUnitObjectiveDto;
import de.thbingen.epro.model.dto.PrivilegeDto;
import de.thbingen.epro.model.entity.BusinessUnitObjective;
import de.thbingen.epro.model.entity.CompanyKeyResult;
import de.thbingen.epro.model.mapper.BusinessUnitObjectiveMapper;
import de.thbingen.epro.repository.BusinessUnitObjectiveRepository;
import de.thbingen.epro.repository.BusinessUnitRepository;
import de.thbingen.epro.repository.CompanyKeyResultRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

/**
 * This Service represents the interface between presentation logic and the data layer for everything related to
 * {@link BusinessUnitObjective} and {@link BusinessUnitObjectiveDto}.
 */
@Service
public class BusinessUnitObjectiveService {

    private final BusinessUnitObjectiveRepository businessUnitObjectiveRepository;
    private final BusinessUnitObjectiveMapper businessUnitObjectiveMapper;
    private final BusinessUnitObjectiveAssembler assembler;

    private final CompanyKeyResultRepository companyKeyResultRepository;
    private final BusinessUnitRepository businessUnitRepository;

    /**
     * Default constructor to be used for Constructor Injection
     *
     * @param businessUnitObjectiveRepository The Repository for DB access to {@link BusinessUnitObjective}
     * @param companyKeyResultRepository      The Repository for DB access to {@link CompanyKeyResult}
     * @param businessUnitObjectiveMapper     The Mapstruct mapper to convert from DTO to entity and back
     * @param assembler                       The RepresentationModelAssembler to add the hateoas relations
     * @param businessUnitRepository          The Repository for DB access to {@link de.thbingen.epro.model.entity.BusinessUnit}
     */
    public BusinessUnitObjectiveService(BusinessUnitObjectiveRepository businessUnitObjectiveRepository, CompanyKeyResultRepository companyKeyResultRepository, BusinessUnitObjectiveMapper businessUnitObjectiveMapper, BusinessUnitObjectiveAssembler assembler, BusinessUnitRepository businessUnitRepository) {
        this.businessUnitObjectiveRepository = businessUnitObjectiveRepository;
        this.companyKeyResultRepository = companyKeyResultRepository;
        this.businessUnitObjectiveMapper = businessUnitObjectiveMapper;
        this.assembler = assembler;
        this.businessUnitRepository = businessUnitRepository;
    }

    /**
     * Returns all {@link BusinessUnitObjective}s, which belong to the {@link de.thbingen.epro.model.entity.BusinessUnit}
     * with the given {@code id} and have a {@code startDate} after the given {@code startDate} parameter
     * <b>and</b> have an {@code endDate} before the given {@code endDate} parameter as a Page of {@link BusinessUnitObjectiveDto}s
     *
     * @param businessUnitId The {@code id} of the {@link de.thbingen.epro.model.entity.BusinessUnit} of which the {@link BusinessUnitObjective}s are to be returned
     * @param pageable       Pagination information to request a certain{@link Page}
     * @param startDate      The {@link LocalDate} which is used to find only {@link BusinessUnitObjective}s with a {@code startDate}
     *                       after this Date
     * @param endDate        The {@link LocalDate} which is used to find only {@link BusinessUnitObjective}s with a {@code endDate}
     *                       before this Date
     * @return a Page of {@link BusinessUnitObjectiveDto}s
     */
    public Page<BusinessUnitObjectiveDto> findAllByBusinessUnitId(Long businessUnitId, Pageable pageable, LocalDate startDate, LocalDate endDate) {
        Page<BusinessUnitObjective> pagedResult = businessUnitObjectiveRepository.findAllByBusinessUnitIdAndStartDateGreaterThanEqualAndEndDateLessThanEqual(businessUnitId, startDate, endDate, pageable);

        if (pagedResult.hasContent()) {
            return pagedResult.map(assembler::toModel);
        } else {
            return Page.empty();
        }
    }

    /**
     * Returns all {@link BusinessUnitObjective}s, which have a {@code startDate} after the given {@code startDate} parameter
     * <b>and</b> have an {@code endDate} before the given {@code endDate} parameter as a Page of {@link BusinessUnitObjectiveDto}s
     *
     * @param pageable  Pagination information, may be null
     * @param startDate The {@link LocalDate} which is used to find only {@link BusinessUnitObjective}s with a {@code startDate}
     *                  after this Date
     * @param endDate   The {@link LocalDate} which is used to find only {@link BusinessUnitObjective}s with a {@code endDate}
     *                  before this Date
     * @return a Page of {@link BusinessUnitObjectiveDto}s
     */
    public Page<BusinessUnitObjectiveDto> getAllBusinessUnitObjectives(Pageable pageable, LocalDate startDate, LocalDate endDate) {
        Page<BusinessUnitObjective> pagedResult = businessUnitObjectiveRepository.findAllByStartDateAfterAndEndDateBefore(startDate, endDate, pageable);

        if (pagedResult.hasContent()) {
            return pagedResult.map(assembler::toModel);
        } else {
            return Page.empty();
        }
    }

    /**
     * Updates the {@link BusinessUnitObjective} in the database with the given {@code id}, using the values from the given {@link BusinessUnitObjectiveDto}
     *
     * @param id                       The {@code id} of the {@link BusinessUnitObjective}, which is to be updated
     * @param businessUnitObjectiveDto The new data, with which a {@link BusinessUnitObjective} is to be updated
     * @return A {@link PrivilegeDto} of the new {@link BusinessUnitObjective}
     */
    public BusinessUnitObjectiveDto updateBusinessUnitObjective(Long id, BusinessUnitObjectiveDto businessUnitObjectiveDto) {
        BusinessUnitObjective businessUnitObjective = businessUnitObjectiveRepository.getById(id);
        businessUnitObjectiveMapper.updateBusinessUnitObjectiveFromDto(businessUnitObjectiveDto, businessUnitObjective);
        return assembler.toModel(businessUnitObjectiveRepository.save(businessUnitObjective));
    }

    /**
     * Inserts the given {@link BusinessUnitObjective} and adds it to the {@link de.thbingen.epro.model.entity.BusinessUnit}
     * with the given {@code id}.
     *
     * @param businessUnitObjectiveDto The {@link BusinessUnitObjectiveDto} to use for inserting
     * @param id                       The {@code id} of the {@link de.thbingen.epro.model.entity.BusinessUnit} to which the new
     *                                 {@link BusinessUnitObjective} is to be added
     * @return The just inserted {@link BusinessUnitObjective} in it's {@link BusinessUnitObjectiveDto} representation
     */
    public BusinessUnitObjectiveDto insertBusinessUnitObjectiveWithBusinessUnit(BusinessUnitObjectiveDto businessUnitObjectiveDto, Long id) {
        BusinessUnitObjective businessUnitObjective = businessUnitObjectiveMapper.dtoToBusinessUnitObjective(businessUnitObjectiveDto);
        businessUnitObjective.setBusinessUnit(businessUnitRepository.getById(id));
        return assembler.toModel(businessUnitObjectiveRepository.save(businessUnitObjective));
    }

    /**
     * Returns the {@link BusinessUnitObjective} with the corresponding {@code id}.
     * If there is no {@link BusinessUnitObjective} with the given {@code id}, an empty {@link Optional} will be returned
     *
     * @param id the requested {@code id}
     * @return an {@link Optional} of the {@link BusinessUnitObjectiveDto}, which corresponds to the {@link BusinessUnitObjective} with the given {@code id}
     */
    public Optional<BusinessUnitObjectiveDto> findById(Long id) {
        Optional<BusinessUnitObjective> optional = businessUnitObjectiveRepository.findById(id);
        return optional.map(assembler::toModel);
    }

    /**
     * Delete the {@link BusinessUnitObjective} with the given {@code id} from the DB
     *
     * @param id the {@code id} for which a role shall be deleted
     */
    public void deleteById(Long id) {
        businessUnitObjectiveRepository.deleteById(id);
    }

    /**
     * Checks whether a {@link BusinessUnitObjective} with the given {@code id} exists in the DB
     *
     * @param id The {@code id} for which the existence check in the DB will be executed
     * @return true if a {@link BusinessUnitObjective} for the given {@code id} exists, false if there is no {@link BusinessUnitObjective} for the given {@code id}
     */
    public boolean existsById(Long id) {
        return businessUnitObjectiveRepository.existsById(id);
    }

    /**
     * Adds a reference to the {@link CompanyKeyResult} with the given {@code companyKeyResultId} to the
     * {@link BusinessUnitObjective} with the given {@code businessUnitObjectiveId}
     *
     * @param businessUnitObjectiveId The id of the {@link BusinessUnitObjective} which will get the reference added
     * @param companyKeyResultId      The id of the {@link CompanyKeyResult} which will be referenced
     * @return {@code true} if the reference could be created, {@code false} if the reference could not be created
     */
    public boolean referenceCompanyKeyResult(Long businessUnitObjectiveId, Long companyKeyResultId) {
        BusinessUnitObjective businessUnitObjective = businessUnitObjectiveRepository.findById(businessUnitObjectiveId).orElse(null);
        CompanyKeyResult companyKeyResult = companyKeyResultRepository.findById(companyKeyResultId).orElse(null);

        if (businessUnitObjective == null || companyKeyResult == null)
            return false;

        businessUnitObjective.setCompanyKeyResult(companyKeyResult);
        businessUnitObjectiveRepository.save(businessUnitObjective);
        return true;
    }

    /**
     * Deletes the reference to a {@link CompanyKeyResult} from the {@link BusinessUnitObjective} with the given {@code businessUnitObjectiveId}
     *
     * @param businessUnitObjectiveId The id of the {@link BusinessUnitObjective} for which the reference will be deleted
     * @return {@code true} if the reference could be deleted, {@code false} if the reference could not be deleted
     */
    public boolean deleteCompanyKeyResultReference(Long businessUnitObjectiveId) {
        BusinessUnitObjective businessUnitObjective = businessUnitObjectiveRepository.findById(businessUnitObjectiveId).orElse(null);

        if (businessUnitObjective == null)
            return false;

        businessUnitObjective.setCompanyKeyResult(null);
        businessUnitObjectiveRepository.save(businessUnitObjective);
        return true;
    }
}
