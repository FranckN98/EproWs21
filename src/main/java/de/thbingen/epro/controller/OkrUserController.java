package de.thbingen.epro.controller;

import de.thbingen.epro.model.dto.OkrUserDto;
import de.thbingen.epro.model.dto.OkrUserPostDto;
import de.thbingen.epro.model.dto.RoleDto;
import de.thbingen.epro.service.OkrUserService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class OkrUserController {

    private final OkrUserService okrUserService;
    private final PagedResourcesAssembler<OkrUserDto> pagedResourcesAssembler;

    public OkrUserController(OkrUserService okrUserService, PagedResourcesAssembler<OkrUserDto> pagedResourcesAssembler) {
        this.okrUserService = okrUserService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('view_users')")
    public PagedModel<EntityModel<OkrUserDto>> findAll(@PageableDefault Pageable pageable) {
        return pagedResourcesAssembler.toModel(okrUserService.findAll(pageable));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('view_users') or (hasAuthority('read') and @customExpressions.isSameUser(#id, principal.username))")
    public OkrUserDto findById(@PathVariable Long id) {
        Optional<OkrUserDto> result = okrUserService.findById(id);
        if (result.isPresent())
            return result.get();
        throw new EntityNotFoundException("No User with this id exists");
    }

    @PostMapping
    @PreAuthorize("hasAuthority('add_users')")
    public ResponseEntity<OkrUserDto> addNew(@RequestBody @Valid OkrUserDto newUser) {
        OkrUserDto okrUserDto = okrUserService.insertOkrUser(newUser);
        return ResponseEntity.created(okrUserDto.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(okrUserDto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('change_users')")
    public ResponseEntity<OkrUserDto> updateById(@PathVariable Long id, @RequestBody @Valid OkrUserDto okrUserDto) {
        if (!okrUserService.existsById(id))
            throw new EntityNotFoundException("No OkrUser with this id exists");

        return ResponseEntity.ok(okrUserService.updateOkrUser(id, okrUserDto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('change_users')")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        if (!okrUserService.existsById(id)) {
            throw new EntityNotFoundException("No OkrUser with this id exists");
        }
        okrUserService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/roles")
    @PreAuthorize("hasAuthority('add_users')")
    public ResponseEntity<RoleDto> addNewRole(@PathVariable Long id, @RequestBody @Valid RoleDto newRoleDto) {
        RoleDto roleDto = okrUserService.addNewRole(id, newRoleDto);
        return ResponseEntity.created(roleDto.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(roleDto);
    }

}
