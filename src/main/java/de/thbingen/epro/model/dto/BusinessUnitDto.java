package de.thbingen.epro.model.dto;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import javax.validation.constraints.NotBlank;

@Relation(collectionRelation = "businessUnits", itemRelation = "businessUnit")
public class BusinessUnitDto extends RepresentationModel<BusinessUnitDto> {

    @NotBlank
    private String name;

    public BusinessUnitDto() {
    }

    public BusinessUnitDto(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
