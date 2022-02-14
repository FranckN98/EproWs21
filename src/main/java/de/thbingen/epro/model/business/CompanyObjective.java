package de.thbingen.epro.model.business;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.*;

@Entity
@Table
public class CompanyObjective {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long id;

    @Column(nullable = false)
    private Integer achievement;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "companyObjective", targetEntity = CompanyKeyResult.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<CompanyKeyResult> companyKeyResults = new HashSet<>();

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    public CompanyObjective(Long id, Integer achievement, String name, LocalDate startDate, LocalDate endDate) {
        this.id = id;
        this.achievement = achievement;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public CompanyObjective() {
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
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

    public Set<CompanyKeyResult> getCompanyKeyResults() {
        return companyKeyResults;
    }

}
