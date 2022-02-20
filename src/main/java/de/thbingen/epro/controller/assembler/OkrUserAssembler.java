package de.thbingen.epro.controller.assembler;

import de.thbingen.epro.controller.OkrUserController;
import de.thbingen.epro.model.business.OkrUser;
import de.thbingen.epro.model.dto.OkrUserDto;
import de.thbingen.epro.model.mapper.OkrUserMapper;
import org.springframework.hateoas.server.RepresentationModelAssembler;
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
        return okrUserMapper.okrUserToDto(entity)
                .add(linkTo(methodOn(OkrUserController.class).findById(entity.getId())).withSelfRel());
    }
}
