package de.thbingen.epro.service;

import de.thbingen.epro.model.assembler.OkrUserAssembler;
import de.thbingen.epro.model.business.Role;
import de.thbingen.epro.model.dto.OkrUserDto;
import de.thbingen.epro.model.dto.RoleDto;
import de.thbingen.epro.model.entity.OkrUser;
import de.thbingen.epro.model.mapper.OkrUserMapper;
import de.thbingen.epro.repository.OkrUserRepository;
import de.thbingen.epro.repository.RoleRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

/**
 * This Service represents the interface between presentation logic and the data layer for everything related to
 * {@link OkrUser} and {@link OkrUserDto}.
 */
@Service
public class OkrUserService {

    private final OkrUserRepository okrUserRepository;
    private final OkrUserMapper okrUserMapper;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final OkrUserAssembler assembler;

    /**
     * Default constructor to be used for Constructor Injection
     *  @param okrUserRepository The Repository for DB access
     * @param okrUserMapper     The Mapstruct mapper to convert from DTO to entity and back
     * @param roleRepository
     * @param passwordEncoder
     * @param assembler         The RepresentationModelAssembler to add the hateoas relations
     */
    public OkrUserService(OkrUserRepository okrUserRepository, OkrUserMapper okrUserMapper, RoleRepository roleRepository, PasswordEncoder passwordEncoder, OkrUserAssembler assembler) {
        this.okrUserRepository = okrUserRepository;
        this.okrUserMapper = okrUserMapper;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.assembler = assembler;
    }

    /**
     * Returns all {@link OkrUser}s present in the DB as a Page of {@link OkrUserDto}s
     *
     * @param pageable Pagination information to request a specific {@link Page}
     * @return a Page of {@link OkrUserDto}s
     */
    public Page<OkrUserDto> findAll(Pageable pageable) {
        Page<OkrUser> pagedResult = okrUserRepository.findAll(pageable);

        if (pagedResult.hasContent()) {
            return pagedResult.map(assembler::toModel);
        }
        return Page.empty();
    }

    /**
     * Returns the {@link OkrUser} with the corresponding {@code id}.
     * If there is no {@link OkrUser} with the given {@code id}, an empty {@link Optional} will be returned
     *
     * @param id the requested {@code id}
     * @return an {@link Optional} of the {@link OkrUserDto}, which corresponds to the {@link OkrUser} with the given {@code id}
     */
    public Optional<OkrUserDto> findById(Long id) {
        Optional<OkrUser> okrUser = okrUserRepository.findById(id);
        return okrUser.map(assembler::toModel);
    }

    /**
     * Returns all {@link OkrUser}s belonging to a certain {@link de.thbingen.epro.model.entity.BusinessUnit} as a
     * Page of {@link OkrUserDto}s.
     *
     * @param businessUnitId The {@code id} of the {@link de.thbingen.epro.model.entity.BusinessUnit} for which to search the {@link OkrUser}s
     * @param pageable       Pagination information to request a specific {@link Page} of {@link OkrUser}s
     * @return The requested {@link Page} of {@link OkrUser}s
     */
    public Page<OkrUserDto> findAllByBusinessUnitId(Long businessUnitId, Pageable pageable) {
        Page<OkrUser> pageadResult = okrUserRepository.findAllByBusinessUnitId(businessUnitId, pageable);

        if (pagedResult.hasContent()) {
            return pagedResult.map(assembler::toModel);
        } else {
            return Page.empty();
        }
    }

    /**
     * Creates a new {@link OkrUser} in the DB using the data from the {@link OkrUserDto}
     *
     * @param okrUserDto The new data, with which an {@link OkrUser} is to be inserted
     * @return An {@link OkrUserDto} of the new {@link OkrUser}
     */
    public OkrUserDto insertOkrUser(OkrUserDto okrUserDto) {
        OkrUser okrUser = okrUserMapper.dtoToOkrUser(okrUserDto);
        okrUser.setPassword(passwordEncoder.encode(okrUser.getPassword()));
        return assembler.toModel(okrUserRepository.save(okrUser));
    }

    /**
     * Updates the {@link OkrUser} in the database with the given {@code id}, using the values from the given {@link OkrUserDto}
     *
     * @param id         The {@code id} of the {@link OkrUser}, which is to be updated
     * @param okrUserDto The new data, with which a {@link OkrUser} is to be updated
     * @return A {@link OkrUserDto} of the new {@link OkrUser}
     */
    public OkrUserDto updateOkrUser(Long id, OkrUserDto okrUserDto) {
        OkrUser okrUser = okrUserRepository.getById(id);
        okrUserMapper.updateOkrUserFromDto(okrUserDto, okrUser);
        return assembler.toModel(okrUserRepository.save(okrUser));
    }

    /**
     * Checks whether a {@link OkrUser} with the given {@code id} exists in the DB
     *
     * @param id The {@code id} for which the existence check in the DB will be executed
     * @return true if a {@link OkrUser} for the given {@code id} exists, false if there is no {@link OkrUser} for the given {@code id}
     */
    public boolean existsById(Long id) {
        return okrUserRepository.existsById(id);
    }

    /**
     * Delete the {@link OkrUser} with the given {@code id} from the DB
     *
     * @param id the {@code id} for which a role shall be deleted
     */
    public void deleteById(Long id) {
        okrUserRepository.deleteById(id);
    }

    public RoleDto addNewRole(Long id, RoleDto roleDto) {
        Optional<OkrUser> okrUserResult = okrUserRepository.findById(id);
        if (!okrUserResult.isPresent()) {
            throw new EntityNotFoundException("No user with this id exists");
        }
        OkrUser okrUser = okrUserResult.get();
        Optional<Role> roleResult = roleRepository.findById(roleDto.getId());
        if (!roleResult.isPresent()) {
            throw new EntityNotFoundException(("No role with this id exists"));
        }
        Role role = roleResult.get();
        okrUser.setRole(role);
        okrUserRepository.save(okrUser);
        return roleDto;
    }

}
