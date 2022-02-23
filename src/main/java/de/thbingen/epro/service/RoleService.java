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
 * The Service
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
     * Returns all Roles present in the DB in the form of RoleDtos
     *
     * @param pageable Pagination information, may be null
     * @return a Page of RoleDtos
     */
    public Page<RoleDto> findAll(Pageable pageable) {
        Page<Role> pagedResult = roleRepository.findAll(pageable);

        if (pagedResult.hasContent()) {
            return pagedResult.map(assembler::toModel);
        }
        return Page.empty();
    }

    /**
     * Returns the Role with the corresponding {@code id}.
     * If there is no Role with the given id, an empty {@code Optional} will be returned
     *
     * @param id the requested id
     * @return the role with the given id
     */
    public Optional<RoleDto> findById(Long id) {
        Optional<Role> role = roleRepository.findById(id);
        return role.map(assembler::toModel);
    }

    /**
     * Updates the role in the database with the given {@code id}, using the values from the {@code roleDto}
     * If the {@code id} is null a new Role is inserted using the values from the {@code roleDto}
     *
     * @param id      The id of the role, which is to be updated. If null, a new role is inserted
     * @param roleDto The new data, with which a role is to be updated or inserted
     * @return A RoleDto of the new Role
     */
    public RoleDto updateRole(Long id, RoleDto roleDto) {
        Role role = roleMapper.dtoToRole(roleDto);
        role.setId(id);
        return assembler.toModel(roleRepository.save(role));
    }

    /**
     * Creates a new Role in the DB using the data from the {@code RoleDto}
     *
     * @param roleDto The new data, with which a role is to be inserted
     * @return A RoleDto of the new Role
     */
    public RoleDto insertRole(RoleDto roleDto) {
        return updateRole(null, roleDto);
    }

    /**
     * Checks whether a role with the given {@code id} exists in the DB
     *
     * @param id The id for which the existence check in the DB will be executed
     * @return true if a role for the given {@code id} exists, false if there is no Role for the given {@code id}
     */
    public boolean existsById(Long id) {
        return roleRepository.existsById(id);
    }

    /**
     * Delete the Role with the given id from the DB
     *
     * @param id the id for which a role shall be deleted
     */
    public void deleteById(Long id) {
        roleRepository.deleteById(id);
    }
}
