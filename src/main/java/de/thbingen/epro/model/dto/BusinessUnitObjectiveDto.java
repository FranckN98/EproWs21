package de.thbingen.epro.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Future;
import java.time.LocalDate;

@Relation(collectionRelation = "businessUnitObjectives", itemRelation = "businessUnitObjective")
public class BusinessUnitObjectiveDto extends RepresentationModel<BusinessUnitObjectiveDto> {

    private Float achievement = 0f;
    private String name;

    private LocalDate startDate;
    @Future
    private LocalDate endDate;

    public BusinessUnitObjectiveDto() {
    }

    public BusinessUnitObjectiveDto(Float achievement, String name, LocalDate startDate, LocalDate endDate) {
        this.achievement = achievement;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Float getAchievement() {
        return achievement;
    }

    public void setAchievement(Float achievement) {
        this.achievement = achievement;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    @AssertTrue(message = "The End date must be after the startDate")
    @JsonIgnore
    public boolean isEndAfterBeginning() {
        return endDate != null && startDate != null && endDate.isAfter(startDate);
    }

}
