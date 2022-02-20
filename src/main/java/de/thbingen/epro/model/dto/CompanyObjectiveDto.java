package de.thbingen.epro.model.dto;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Relation(collectionRelation = "companyObjectives", itemRelation = "companyObjective")
public class CompanyObjectiveDto extends RepresentationModel<CompanyObjectiveDto> {

    @Min(value = 0, message = "Achievement must be 0 when creating a new Company Objective")
    @Max(value = 0, message = "Achievement must be 0 when creating a new Company Objective")
    private Integer achievement;
    @NotBlank
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;

    public CompanyObjectiveDto() {
    }

    public CompanyObjectiveDto(Integer achievement, String name, LocalDate startDate, LocalDate endDate) {
        this.achievement = achievement;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Integer getAchievement() {
        return achievement;
    }

    public void setAchievement(Integer achievement) {
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

}
