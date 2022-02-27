package de.thbingen.epro.controller;


import de.thbingen.epro.model.dto.PrivilegeDto;
import de.thbingen.epro.model.dto.RoleDto;
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
    private final PrivilegeService privilegeService;
    private final PagedResourcesAssembler<RoleDto> pagedResourcesAssembler;

    public RoleController(RoleService roleService, PrivilegeService privilegeService, PagedResourcesAssembler<RoleDto> pagedResourcesAssembler) {
        this.roleService = roleService;
        this.privilegeService = privilegeService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
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
            return this.addNew(roleDto);

        return ResponseEntity.ok(roleService.updateRole(id, roleDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        if (!roleService.existsById(id))
            throw new EntityNotFoundException("No Role with this id exists");
        roleService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(
            value = "/{id}/privileges",
            produces = MediaTypes.HAL_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<PrivilegeDto> addNewPrivilege(@PathVariable Long id, @RequestBody @Valid PrivilegeDto newPrivilegeDto) {
        PrivilegeDto privilegeDto = roleService.addNewPrivilege(id, newPrivilegeDto);
        return ResponseEntity.created(privilegeDto.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(privilegeDto);
    }

}
