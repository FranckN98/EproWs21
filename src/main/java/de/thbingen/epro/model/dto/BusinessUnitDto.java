package de.thbingen.epro.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.thbingen.epro.model.business.BusinessUnit;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import javax.validation.constraints.NotBlank;

@Relation(collectionRelation = "businessUnits", itemRelation = "businessUnit")
public class BusinessUnitDto extends RepresentationModel<BusinessUnitDto> {

    @JsonIgnore
    private Long id;
    @NotBlank
    private String name;

    public BusinessUnitDto() {
    }

    public BusinessUnitDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    static BusinessUnitDto from(BusinessUnit businessUnit) {
        return new BusinessUnitDto(businessUnit.getId(), businessUnit.getName());
    }
}
