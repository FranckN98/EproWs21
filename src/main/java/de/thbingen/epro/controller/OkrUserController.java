package de.thbingen.epro.controller;

import de.thbingen.epro.model.dto.OkrUserDto;
import de.thbingen.epro.model.dto.OkrUserPostDto;
import de.thbingen.epro.model.dto.OkrUserUpdateDto;
import de.thbingen.epro.service.OkrUserService;
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
@RequestMapping("/users")
public class OkrUserController {

    private final OkrUserService okrUserService;
    private final PagedResourcesAssembler<OkrUserDto> pagedResourcesAssembler;

    public OkrUserController(OkrUserService okrUserService, PagedResourcesAssembler<OkrUserDto> pagedResourcesAssembler) {
        this.okrUserService = okrUserService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @PreAuthorize("hasAuthority('view_users')")
    public PagedModel<EntityModel<OkrUserDto>> findAll(@PageableDefault Pageable pageable) {
        return pagedResourcesAssembler.toModel(okrUserService.findAll(pageable));
    }

    @GetMapping(
            value = "/{id}",
            produces = MediaTypes.HAL_JSON_VALUE
    )
    @PreAuthorize("hasAuthority('view_users') or (hasAuthority('read') and @customExpressions.isSameUser(#id, principal.username))")
    public OkrUserDto findById(@PathVariable Long id) {
        Optional<OkrUserDto> result = okrUserService.findById(id);
        if (result.isPresent())
            return result.get();
        throw new EntityNotFoundException("No User with this id exists");
    }

    @PostMapping(
            produces = MediaTypes.HAL_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    @PreAuthorize("hasAuthority('add_users')")
    public ResponseEntity<OkrUserDto> addNew(@RequestBody @Valid OkrUserPostDto newUser) {
        OkrUserDto okrUserDto = okrUserService.insertOkrUser(newUser);
        return ResponseEntity.created(okrUserDto.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(okrUserDto);
    }

    @PutMapping(
            value = "/{id}",
            produces = MediaTypes.HAL_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    @PreAuthorize("hasAuthority('change_users')")
    public ResponseEntity<OkrUserDto> updateById(@PathVariable Long id, @RequestBody @Valid OkrUserUpdateDto okrUserDto) {
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

    @PostMapping(
            value = "/{id}/roles/{roleId}",
            produces = MediaTypes.HAL_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    @PreAuthorize("hasAuthority('add_users')")
    public ResponseEntity<Void> setRole(@PathVariable Long id, @PathVariable Long roleId) {
        if (!okrUserService.existsById(id)) {
            throw new EntityNotFoundException("No OkrUser with this id exists");
        }
        okrUserService.setRole(id, roleId);
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(
            value = "/{id}/businessUnits/{businessUnitId}",
            method = {RequestMethod.POST, RequestMethod.PUT}
    )
    public ResponseEntity<Void> setBusinessUnit(
            @PathVariable Long id,
            @PathVariable Long businessUnitId
    ) {
        if (!okrUserService.existsById(id)) {
            throw new EntityNotFoundException("No OkrUser with this id exists");
        }
        okrUserService.setBusinessUnit(id, businessUnitId);
        return ResponseEntity.noContent().build();
    }

}
