package de.thbingen.epro.controller;


import de.thbingen.epro.model.dto.OkrUserDto;
import de.thbingen.epro.model.dto.PrivilegeDto;
import de.thbingen.epro.model.dto.RoleDto;
import de.thbingen.epro.service.OkrUserService;
import de.thbingen.epro.service.PrivilegeService;
import de.thbingen.epro.service.RoleService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/roles")
@PreAuthorize("hasAuthority('access_roles')")
public class RoleController {

    private final RoleService roleService;
    private final PagedResourcesAssembler<RoleDto> pagedResourcesAssembler;
    private final OkrUserService okrUserService;
    private final PagedResourcesAssembler<OkrUserDto> okrUserDtoPagedResourcesAssembler;
    private final PrivilegeService privilegeService;
    private final PagedResourcesAssembler<PrivilegeDto> privilegeDtoPagedResourcesAssembler;


    public RoleController(RoleService roleService, PagedResourcesAssembler<RoleDto> pagedResourcesAssembler, PagedResourcesAssembler<OkrUserDto> okrUserDtoPagedResourcesAssembler, OkrUserService okrUserService, PrivilegeService privilegeService, PagedResourcesAssembler<PrivilegeDto> privilegeDtoPagedResourcesAssembler) {
        this.roleService = roleService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
        this.okrUserDtoPagedResourcesAssembler = okrUserDtoPagedResourcesAssembler;
        this.okrUserService = okrUserService;
        this.privilegeService = privilegeService;
        this.privilegeDtoPagedResourcesAssembler = privilegeDtoPagedResourcesAssembler;
    }

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public PagedModel<EntityModel<RoleDto>> findAll(@PageableDefault Pageable pageable) {
        return pagedResourcesAssembler.toModel(roleService.findAll(pageable));
    }

    @GetMapping(
            value = "/{id}",
            produces = MediaTypes.HAL_JSON_VALUE
    )
    public RoleDto findById(@PathVariable Long id) {
        Optional<RoleDto> result = roleService.findById(id);
        if (result.isPresent())
            return result.get();
        throw new EntityNotFoundException("No Role with this id exists");
    }

    @PostMapping(
            produces = MediaTypes.HAL_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<RoleDto> addNew(@RequestBody @Valid RoleDto newRole) {
        RoleDto roleDto = roleService.insertRole(newRole);
        return ResponseEntity.created(roleDto.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(roleDto);
    }

    @PutMapping(
            value = "/{id}",
            produces = MediaTypes.HAL_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<RoleDto> updateById(@PathVariable Long id, @RequestBody @Valid RoleDto roleDto) {
        if (!roleService.existsById(id))
            throw new EntityNotFoundException("No Role with this id exists");

        return ResponseEntity.ok(roleService.updateRole(id, roleDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        if (!roleService.existsById(id))
            throw new EntityNotFoundException("No Role with this id exists");
        roleService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Returns all Privileges contained in the role paged.
     *
     * @param pageable the parameters, determining which Page to return
     * @param id       The id which the Privileges to be returned belong to
     * @return The requested Page of Privileges
     */
    @GetMapping(
            value = "/{id}/privileges",
            produces = MediaTypes.HAL_JSON_VALUE
    )
    public PagedModel<EntityModel<PrivilegeDto>> findAllPrivilegesInRole(
            @PageableDefault Pageable pageable,
            @PathVariable Long id
    ) {
        if (!roleService.existsById(id))
            throw new EntityNotFoundException("No Role with this id exists");
        return privilegeDtoPagedResourcesAssembler.toModel(privilegeService.findAllByRoleId(id, pageable));
    }

    @RequestMapping(
            value = "/{id}/privileges/{privilegeId}",
            method = {RequestMethod.PUT, RequestMethod.POST},
            produces = MediaTypes.HAL_JSON_VALUE
    )
    public ResponseEntity<Void> addNewPrivilege(@PathVariable Long id, @PathVariable Long privilegeId) {
        if (!roleService.existsById(id))
            throw new EntityNotFoundException("No Role with this id exists");
        roleService.addNewPrivilege(id, privilegeId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Returns the requested Page of users with this Role
     *
     * @param pageable the parameters, determining which role to return
     * @param id       the id of the role, which the users should have
     * @return the requested page of users with the given role
     */
    @GetMapping(
            value = "/{id}/users",
            produces = MediaTypes.HAL_JSON_VALUE
    )
    public PagedModel<EntityModel<OkrUserDto>> findAllUsersWithRole(
            @PageableDefault Pageable pageable,
            @PathVariable Long id
    ) {
        if (!roleService.existsById(id))
            throw new EntityNotFoundException("No Role with this id exists");

        return okrUserDtoPagedResourcesAssembler.toModel(okrUserService.findAllUsersWithRole(id, pageable));
    }
}
