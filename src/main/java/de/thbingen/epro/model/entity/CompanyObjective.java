package de.thbingen.epro.model.entity;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table
public class CompanyObjective {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long id;

    @Column(nullable = false, insertable = false, updatable = false)
    private Float achievement;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "companyObjective", targetEntity = CompanyKeyResult.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<CompanyKeyResult> companyKeyResults = new HashSet<>();

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    public CompanyObjective(Long id, Float achievement, String name, LocalDate startDate, LocalDate endDate) {
        this.id = id;
        this.achievement = achievement;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public CompanyObjective() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Set<CompanyKeyResult> getCompanyKeyResults() {
        return companyKeyResults;
    }

    public void setCompanyKeyResults(Set<CompanyKeyResult> companyKeyResults) {
        this.companyKeyResults = companyKeyResults;
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
