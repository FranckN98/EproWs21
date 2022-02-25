package de.thbingen.epro.model.assembler;

import de.thbingen.epro.controller.BusinessUnitController;
import de.thbingen.epro.controller.OkrUserController;
import de.thbingen.epro.controller.RoleController;
import de.thbingen.epro.model.dto.OkrUserDto;
import de.thbingen.epro.model.entity.OkrUser;
import de.thbingen.epro.model.mapper.OkrUserMapper;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class OkrUserAssembler implements RepresentationModelAssembler<OkrUser, OkrUserDto> {

    private final OkrUserMapper okrUserMapper;

    public OkrUserAssembler(OkrUserMapper okrUserMapper) {
        this.okrUserMapper = okrUserMapper;
    }


    @Override
    public OkrUserDto toModel(OkrUser entity) {
        OkrUser okrUser = (OkrUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        OkrUserDto okrUserDto = okrUserMapper.okrUserToDto(entity)
                .add(linkTo(methodOn(OkrUserController.class).findById(entity.getId())).withSelfRel());

        if (okrUser.hasPrivilege("access_roles") && entity.getRole() != null) {
            okrUserDto.add(linkTo(methodOn(RoleController.class).findById(entity.getRole().getId())).withRel("role"));
        }
        if (entity.getBusinessUnit() != null) {
            okrUserDto.add(linkTo(methodOn(BusinessUnitController.class).findById(entity.getBusinessUnit().getId())).withRel("businessUnit"));
        }
        return okrUserDto;
    }
}
