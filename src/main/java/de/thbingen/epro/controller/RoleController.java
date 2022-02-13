package de.thbingen.epro.controller;


import de.thbingen.epro.exception.NonMatchingIdsException;
import de.thbingen.epro.model.dto.RoleDto;
import de.thbingen.epro.service.RoleService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/roles")
@PreAuthorize("hasAuthority('access_roles')")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping
    public List<RoleDto> findAll() {
        return roleService.findAll();
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
        UriComponents uriComponents = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host("localhost")
                .port(8080)
                .path("/api/v1/users/{id}")
                .buildAndExpand(roleDto.getId());
        return ResponseEntity.created(uriComponents.toUri()).body(roleDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoleDto> updateById(@PathVariable Long id, @RequestBody @Valid RoleDto roleDto) {
        if (roleDto.getId() == null)
            roleDto.setId(id);
        if (!Objects.equals(roleDto.getId(), id))
            throw new NonMatchingIdsException("Ids in path and jsonObject do not match");
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
