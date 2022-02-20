package de.thbingen.epro.controller.businessunit;

import de.thbingen.epro.model.dto.BusinessUnitObjectiveDto;
import de.thbingen.epro.model.dto.OkrUserDto;
import de.thbingen.epro.service.OkrUserService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/businessUnits/{id}/users")
public class BusinessUnitOkrUserController {

    private final OkrUserService okrUserService;
    private final PagedResourcesAssembler<OkrUserDto> pagedResourcesAssembler;

    public BusinessUnitOkrUserController(OkrUserService okrUserService, PagedResourcesAssembler<OkrUserDto> pagedResourcesAssembler) {
        this.okrUserService = okrUserService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public PagedModel<EntityModel<OkrUserDto>> getAllOkrUsers(@PageableDefault Pageable pageable, @PathVariable Long id) {
        return pagedResourcesAssembler.toModel(okrUserService.findAllByBusinessUnitId(id, pageable));
    }

    @PostMapping(consumes = MediaTypes.HAL_JSON_VALUE, produces = MediaTypes.HAL_JSON_VALUE)
    public OkrUserDto addNewUser() {
        //TODO
        return new OkrUserDto();
    }
}
