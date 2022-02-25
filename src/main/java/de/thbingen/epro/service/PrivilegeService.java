package de.thbingen.epro.service;

import de.thbingen.epro.model.assembler.PrivilegeAssembler;
import de.thbingen.epro.model.dto.PrivilegeDto;
import de.thbingen.epro.model.entity.Privilege;
import de.thbingen.epro.model.mapper.PrivilegeMapper;
import de.thbingen.epro.repository.PrivilegeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * This Service represents the interface between presentation logic and the data layer for everything related to
 * {@link Privilege} and {@link PrivilegeDto}.
 */
@Service
public class PrivilegeService {

    private final PrivilegeRepository privilegeRepository;
    private final PrivilegeMapper privilegeMapper;
    private final PrivilegeAssembler assembler;

    /**
     * Default constructor to be used for Constructor Injection
     *
     * @param privilegeRepository The Repository for DB access
     * @param privilegeMapper     The Mapstruct mapper to convert from DTO to entity and back
     * @param assembler           The RepresentationModelAssembler to add the hateoas relations
     */
    public PrivilegeService(PrivilegeRepository privilegeRepository, PrivilegeMapper privilegeMapper, PrivilegeAssembler assembler) {
        this.privilegeRepository = privilegeRepository;
        this.privilegeMapper = privilegeMapper;
        this.assembler = assembler;
    }

    /**
     * Returns all {@link Privilege}s present in the DB as a Page of {@link PrivilegeDto}s
     *
     * @param pageable Pagination information to request a specific {@link Page}
     * @return a Page of {@link PrivilegeDto}s
     */
    public Page<PrivilegeDto> findAll(Pageable pageable) {
        Page<Privilege> pagedResult = privilegeRepository.findAll(pageable);

        if (pagedResult.hasContent()) {
            return pagedResult.map(assembler::toModel);
        }
        return Page.empty();
    }

    /**
     * Returns the {@link Privilege} with the corresponding {@code id}.
     * If there is no {@link Privilege} with the given {@code id}, an empty {@link Optional} will be returned
     *
     * @param id the requested {@code id}
     * @return an {@link Optional} of the {@link PrivilegeDto}, which corresponds to the {@link Privilege} with the given {@code id}
     */
    public Optional<PrivilegeDto> findById(Long id) {
        Optional<Privilege> privilege = privilegeRepository.findById(id);
        return privilege.map(assembler::toModel);
    }

    /**
     * Updates the {@link Privilege} in the database with the given {@code id}, using the values from the given {@link PrivilegeDto}
     *
     * @param id      The {@code id} of the {@link Privilege}, which is to be updated
     * @param privilegeDto The new data, with which a {@link Privilege} is to be updated
     * @return A {@link PrivilegeDto} of the new {@link Privilege}
     */
    public PrivilegeDto updatePrivilege(Long id, PrivilegeDto privilegeDto) {
        Privilege privilege = privilegeRepository.getById(id);
        privilegeMapper.updatePrivilegeFromDto(privilegeDto, privilege);
        return assembler.toModel(privilegeRepository.save(privilege));
    }

    /**
     * Creates a new {@link Privilege} in the DB using the data from the {@link PrivilegeDto}
     *
     * @param privilegeDto The new data, with which a {@link Privilege} is to be inserted
     * @return A {@link PrivilegeDto} of the new {@link Privilege}
     */
    public PrivilegeDto insertPrivilege(PrivilegeDto privilegeDto) {
        return assembler.toModel(privilegeRepository.save(privilegeMapper.dtoToPrivilege(privilegeDto)));
    }

    /**
     * Checks whether a {@link Privilege} with the given {@code id} exists in the DB
     *
     * @param id The {@code id} for which the existence check in the DB will be executed
     * @return true if a {@link Privilege} for the given {@code id} exists, false if there is no {@link Privilege} for the given {@code id}
     */
    public boolean existsById(Long id) {
        return privilegeRepository.existsById(id);
    }

    /**
     * Delete the {@link Privilege} with the given {@code id} from the DB
     *
     * @param id the {@code id} for which a role shall be deleted
     */
    public void deleteById(Long id) {
        privilegeRepository.deleteById(id);
    }
}
