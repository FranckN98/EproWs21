package de.thbingen.epro.service;

import de.thbingen.epro.model.assembler.PrivilegeAssembler;
import de.thbingen.epro.model.assembler.RoleAssembler;
import de.thbingen.epro.model.dto.RoleDto;
import de.thbingen.epro.model.entity.Privilege;
import de.thbingen.epro.model.entity.Role;
import de.thbingen.epro.model.mapper.RoleMapper;
import de.thbingen.epro.repository.PrivilegeRepository;
import de.thbingen.epro.repository.RoleRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
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
    private final PrivilegeRepository privilegeRepository;

    private final PrivilegeAssembler privilegeAssembler;

    /**
     * Default constructor to be used for Constructor Injection
     *
     * @param roleRepository      The Repository for DB access
     * @param roleMapper          The Mapstruct mapper to convert from DTO to entity and back
     * @param assembler           The RepresentationModelAssembler to add the hateoas relations
     * @param privilegeRepository The Repository for DB Access to Privileges
     * @param privilegeAssembler  The Assembler to add hateoas relations to privileges
     */
    public RoleService(RoleRepository roleRepository, RoleMapper roleMapper, RoleAssembler assembler, PrivilegeRepository privilegeRepository, PrivilegeAssembler privilegeAssembler) {
        this.roleRepository = roleRepository;
        this.roleMapper = roleMapper;
        this.assembler = assembler;
        this.privilegeRepository = privilegeRepository;
        this.privilegeAssembler = privilegeAssembler;
    }

    /**
     * Returns all {@link Role}s present in the DB as a Page of {@link RoleDto}s
     *
     * @param pageable Pagination information to request a specific {@link Page}
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
     * @return an {@link Optional} of the {@link RoleDto}, which corresponds to the {@link Role} with the given {@code id}
     */
    public Optional<RoleDto> findById(Long id) {
        Optional<Role> role = roleRepository.findById(id);
        return role.map(assembler::toModel);
    }

    /**
     * Updates the {@link Role} in the database with the given {@code id}, using the values from the given {@link RoleDto}
     *
     * @param id      The {@code id} of the {@link Role}, which is to be updated
     * @param roleDto The new data, with which a {@link Role} is to be updated
     * @return A {@link RoleDto} of the new {@link Role}
     */
    public RoleDto updateRole(Long id, RoleDto roleDto) {
        Role role = roleRepository.getById(id);
        roleMapper.updateRoleFromDto(roleDto, role);
        return assembler.toModel(roleRepository.save(role));
    }

    /**
     * Creates a new {@link Role} in the DB using the data from the {@link RoleDto}
     *
     * @param roleDto The new data, with which a {@link Role} is to be inserted
     * @return A {@link RoleDto} of the new {@link Role}
     */
    public RoleDto insertRole(RoleDto roleDto) {
        return assembler.toModel(roleRepository.save(roleMapper.dtoToRole(roleDto)));
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

    public void addNewPrivilegeToRole(Long id, Long privilegeId) {
        Optional<Role> roleResult = roleRepository.findById(id);
        if (roleResult.isEmpty()) {
            throw new EntityNotFoundException("No role with this id exists");
        }
        Role role = roleResult.get();

        Optional<Privilege> privilegeResult = privilegeRepository.findById(privilegeId);
        if (privilegeResult.isEmpty()) {
            throw new EntityNotFoundException("No privilege with this id exists");
        }
        Privilege privilege = privilegeResult.get();

        if (role.getPrivileges().contains(privilege)) {
            return;
        }

        role.addPrivilege(privilege);
        roleRepository.save(role);
    }
}
