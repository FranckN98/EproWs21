package de.thbingen.epro.controller;

import de.thbingen.epro.model.dto.PrivilegeDto;
import de.thbingen.epro.service.PrivilegeService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/privileges")
public class PrivilegeController {

    private final PrivilegeService privilegeService;
    private final PagedResourcesAssembler<PrivilegeDto> pagedResourcesAssembler;

    public PrivilegeController(PrivilegeService privilegeService, PagedResourcesAssembler<PrivilegeDto> pagedResourcesAssembler) {
        this.privilegeService = privilegeService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @GetMapping
    public PagedModel<EntityModel<PrivilegeDto>> findAll(@PageableDefault Pageable pageable) {
        return pagedResourcesAssembler.toModel(privilegeService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public PrivilegeDto findById(@RequestParam Long id) {
        Optional<PrivilegeDto> result = privilegeService.findById(id);
        if (result.isPresent())
            return result.get();
        throw new EntityNotFoundException("No Privilege with this id exists");
    }

    @PostMapping
    public ResponseEntity<PrivilegeDto> addNew(@RequestBody @Valid PrivilegeDto newPrivilege) {
        PrivilegeDto privilegeDto = privilegeService.insertPrivilege(newPrivilege);
        return ResponseEntity.created(privilegeDto.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(privilegeDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PrivilegeDto> updateById(@PathVariable Long id, @RequestBody @Valid PrivilegeDto privilegeDto) {
        if (!privilegeService.existsById(id))
            return this.addNew(privilegeDto);

        return ResponseEntity.ok(privilegeService.updatePrivilege(id, privilegeDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        if (!privilegeService.existsById(id))
            throw new EntityNotFoundException("No Privilege with this id exists");
        privilegeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
