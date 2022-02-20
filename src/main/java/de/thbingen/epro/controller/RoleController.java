package de.thbingen.epro.controller;


import de.thbingen.epro.exception.NonMatchingIdsException;
import de.thbingen.epro.model.dto.RoleDto;
import de.thbingen.epro.service.RoleService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/roles")
public class RoleController {

    private final RoleService roleService;
    private final PagedResourcesAssembler<RoleDto> pagedResourcesAssembler;

    public RoleController(RoleService roleService, PagedResourcesAssembler<RoleDto> pagedResourcesAssembler) {
        this.roleService = roleService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @GetMapping
    public PagedModel<EntityModel<RoleDto>> findAll(@PageableDefault Pageable pageable) {
        return pagedResourcesAssembler.toModel(roleService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public RoleDto findById(@PathVariable Long id) {
        Optional<RoleDto> result = roleService.findById(id);
        if (result.isPresent())
            return result.get();
        throw new EntityNotFoundException("No Role with this id exists");
    }

    @PostMapping
    public ResponseEntity<RoleDto> addNew(@RequestBody @Valid RoleDto newRole) {
        RoleDto roleDto = roleService.saveRole(newRole);
        return ResponseEntity.created(roleDto.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(roleDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoleDto> updateById(@PathVariable Long id, @RequestBody @Valid RoleDto roleDto) {
        if (!roleService.existsById(id))
            return this.addNew(roleDto);

        return ResponseEntity.ok(roleService.saveRole(roleDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        if (!roleService.existsById(id))
            throw new EntityNotFoundException("No Role with this id exists");
        roleService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
