package de.thbingen.epro.service;

import de.thbingen.epro.model.assembler.BusinessUnitAssembler;
import de.thbingen.epro.model.dto.BusinessUnitDto;
import de.thbingen.epro.model.entity.BusinessUnit;
import de.thbingen.epro.model.mapper.BusinessUnitMapper;
import de.thbingen.epro.repository.BusinessUnitRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

/**
 * This Service represents the interface between presentation logic and the data layer for everything related to
 * {@link BusinessUnit} and {@link BusinessUnitDto}.
 */
@Service
public class BusinessUnitService {

    private final BusinessUnitRepository businessUnitRepository;
    private final BusinessUnitMapper businessUnitMapper;
    private final BusinessUnitAssembler businessUnitAssembler;

    /**
     * Default constructor to be used for Constructor Injection
     *
     * @param businessUnitRepository The Repository for DB access
     * @param businessUnitMapper     The Mapstruct mapper to convert from DTO to entity and back
     * @param businessUnitAssembler  The RepresentationModelAssembler to add the hateoas relations
     */
    public BusinessUnitService(BusinessUnitRepository businessUnitRepository, BusinessUnitMapper businessUnitMapper, BusinessUnitAssembler businessUnitAssembler) {
        this.businessUnitRepository = businessUnitRepository;
        this.businessUnitMapper = businessUnitMapper;
        this.businessUnitAssembler = businessUnitAssembler;
    }

    /**
     * Returns all {@link BusinessUnit}s present in the DB as a Page of {@link BusinessUnitDto}s
     *
     * @param pageable Pagination information to request a specific {@link Page}
     * @return a Page of {@link BusinessUnitDto}s
     */
    public Page<BusinessUnitDto> findAll(Pageable pageable) {
        Page<BusinessUnit> pagedResult = businessUnitRepository.findAll(pageable);

        if (pagedResult.hasContent()) {
            return pagedResult.map(businessUnitAssembler::toModel);
        } else {
            return Page.empty();
        }
    }

    /**
     * Returns the {@link BusinessUnit} with the corresponding {@code id}.
     * If there is no {@link BusinessUnit} with the given {@code id}, an empty {@link Optional} will be returned
     *
     * @param id the requested {@code id}
     * @return an {@link Optional} of the {@link BusinessUnitDto}, which corresponds to the {@link BusinessUnit} with the given {@code id}
     */
    public Optional<BusinessUnitDto> findById(Long id) {
        Optional<BusinessUnit> businessUnit = businessUnitRepository.findById(id);
        return businessUnit.map(businessUnitAssembler::toModel);
    }

    /**
     * Updates the {@link BusinessUnit} in the database with the given {@code id}, using the values from the given {@link BusinessUnitDto}
     *
     * @param id              The {@code id} of the {@link BusinessUnit}, which is to be updated
     * @param businessUnitDto The new data, with which a {@link BusinessUnit} is to be updated
     * @return A {@link BusinessUnitDto} of the new {@link BusinessUnit}
     */
    public BusinessUnitDto updateBusinessUnit(Long id, BusinessUnitDto businessUnitDto) {
        Optional<BusinessUnit> businessUnitOptional = businessUnitRepository.findById(id);
        BusinessUnit businessUnit = businessUnitOptional.orElseThrow(() -> new EntityNotFoundException("No BusinessUnit with this id exists"));
        businessUnitMapper.updateBusinessUnitFromDto(businessUnitDto, businessUnit);
        return businessUnitAssembler.toModel(businessUnitRepository.save(businessUnit));
    }

    /**
     * Creates a new {@link BusinessUnit} in the DB using the data from the {@link BusinessUnitDto}
     *
     * @param businessUnitDto The new data, with which an {@link BusinessUnit} is to be inserted
     * @return An {@link BusinessUnitDto} of the new {@link BusinessUnit}
     */
    public BusinessUnitDto insertBusinessUnit(BusinessUnitDto businessUnitDto) {
        BusinessUnit businessUnit = businessUnitMapper.dtoToBusinessUnit(businessUnitDto);
        return businessUnitAssembler.toModel(businessUnitRepository.save(businessUnit));
    }

    /**
     * Checks whether a {@link BusinessUnit} with the given {@code id} exists in the DB
     *
     * @param id The {@code id} for which the existence check in the DB will be executed
     * @return true if a {@link BusinessUnit} for the given {@code id} exists, false if there is no {@link BusinessUnit} for the given {@code id}
     */
    public boolean existsById(Long id) {
        return businessUnitRepository.existsById(id);
    }

    /**
     * Delete the {@link BusinessUnit} with the given {@code id} from the DB
     *
     * @param id the {@code id} for which a role shall be deleted
     */
    public void deleteById(Long id) {
        businessUnitRepository.deleteById(id);
    }
}
