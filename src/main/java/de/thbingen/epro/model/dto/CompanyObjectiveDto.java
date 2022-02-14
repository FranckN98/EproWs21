package de.thbingen.epro.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import de.thbingen.epro.model.business.CompanyObjective;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Relation(collectionRelation = "companyObjectives", itemRelation = "companyObjective")
public class CompanyObjectiveDto extends RepresentationModel<CompanyObjectiveDto> {

    @JsonIgnore
    private Long id;
    @Min(value = 0, message = "Achievement must be 0 when creating a new Company Objective")
    @Max(value = 0, message = "Achievement must be 0 when creating a new Company Objective")
    private Integer achievement;
    @NotBlank
    private String name;
    /*@JsonInclude(JsonInclude.Include.NON_NULL)
    private Set<CompanyKeyResultDto> companyKeyResults = new HashSet<>();*/
    private LocalDate startDate;
    private LocalDate endDate;

    public CompanyObjectiveDto() {
    }

    public CompanyObjectiveDto(Long id, Integer achievement, String name, LocalDate startDate, LocalDate endDate) {
        this.id = id;
        this.achievement = achievement;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
