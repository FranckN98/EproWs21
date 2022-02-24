package de.thbingen.epro.service;

import de.thbingen.epro.model.assembler.RoleAssembler;
import de.thbingen.epro.model.dto.RoleDto;
import de.thbingen.epro.model.entity.Role;
import de.thbingen.epro.model.mapper.RoleMapper;
import de.thbingen.epro.repository.RoleRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * This Service represents the interface between presentation logic and the data layer for everything related to
 * {@link Role} and {@link RoleDto}.
 */
@Service
public class RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;
    private final RoleAssembler assembler;

    /**
     * Default constructor to be used for Constructor Injection
     *
     * @param roleRepository The Repository for DB access
     * @param roleMapper     The Mapstruct mapper to convert from DTO to entity and back
     * @param assembler      The RepresentationModelAssembler to add the hateoas relations
     */
    public RoleService(RoleRepository roleRepository, RoleMapper roleMapper, RoleAssembler assembler) {
        this.roleRepository = roleRepository;
        this.roleMapper = roleMapper;
        this.assembler = assembler;
    }

    /**
     * Returns all {@link Role}s present in the DB in the form of {@link RoleDto}s
     *
     * @param pageable Pagination information, may be null
     * @return a Page of {@link RoleDto}s
     */
    public Page<RoleDto> findAll(Pageable pageable) {
        Page<Role> pagedResult = roleRepository.findAll(pageable);

        if (pagedResult.hasContent()) {
            return pagedResult.map(assembler::toModel);
        }
        return Page.empty();
    }

    /**
     * Returns the {@link Role} with the corresponding {@code id}.
     * If there is no {@link Role} with the given {@code id}, an empty {@link Optional} will be returned
     *
     * @param id the requested {@code id}
     * @return the {@link Role} with the given {@code id}
     */
    public Optional<RoleDto> findById(Long id) {
        Optional<Role> role = roleRepository.findById(id);
        return role.map(assembler::toModel);
    }

    /**
     * Updates the {@link Role} in the database with the given {@code id}, using the values from the given {@link RoleDto}
     * If the {@code id} is null a new {@link Role} is inserted using the values from the {@link RoleDto}
     *
     * @param id      The {@code id} of the role, which is to be updated. If null, a new {@link Role} is inserted
     * @param roleDto The new data, with which a {@link Role} is to be updated or inserted
     * @return A {@link RoleDto} of the new {@link Role}
     */
    public RoleDto updateRole(Long id, RoleDto roleDto) {
        Role role = roleMapper.dtoToRole(roleDto);
        role.setId(id);
        return assembler.toModel(roleRepository.save(role));
    }

    /**
     * Creates a new {@link Role} in the DB using the data from the {@link RoleDto}
     *
     * @param roleDto The new data, with which a {@link Role} is to be inserted
     * @return A {@link RoleDto} of the new {@link Role}
     */
    public RoleDto insertRole(RoleDto roleDto) {
        return updateRole(null, roleDto);
    }

    /**
     * Checks whether a {@link Role} with the given {@code id} exists in the DB
     *
     * @param id The {@code id} for which the existence check in the DB will be executed
     * @return true if a {@link Role} for the given {@code id} exists, false if there is no {@link Role} for the given {@code id}
     */
    public boolean existsById(Long id) {
        return roleRepository.existsById(id);
    }

    /**
     * Delete the {@link Role} with the given {@code id} from the DB
     *
     * @param id the {@code id} for which a role shall be deleted
     */
    public void deleteById(Long id) {
        roleRepository.deleteById(id);
    }
}
