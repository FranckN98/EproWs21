package de.thbingen.epro.model.dto;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import javax.validation.constraints.NotBlank;

@Relation(collectionRelation = "privileges", itemRelation = "privilege")
public class PrivilegeDto extends RepresentationModel<PrivilegeDto> {

    @NotBlank
    private String name;

    public PrivilegeDto() {
    }

    public PrivilegeDto(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
