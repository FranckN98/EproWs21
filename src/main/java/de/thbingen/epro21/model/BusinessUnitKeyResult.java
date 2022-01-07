package de.thbingen.epro21.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class BusinessUnitKeyResult
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long id;

    @Column(nullable = false)
    private Integer achievement;

    @Column(nullable = false)
    private String name;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "business_unit_objective_id")
    private  BusinessUnitObjective businessUnitObjective;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "company_key_result_id")
    private  CompanyKeyResult companyKeyResult;

    @OneToMany(targetEntity = BusinessUnitKeyResultHistory.class, cascade = CascadeType.ALL)
    @JoinColumn(name="business_unit_key_result_id")
    private Set<BusinessUnitKeyResultHistory> businessUnitKeyResultHistories = new HashSet<>();

    public BusinessUnitKeyResult(Integer achievement, String name) {
        this.achievement = achievement;
        this.name = name;
    }

    public BusinessUnitKeyResult() {
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

    public Long getId() {
        return id;
    }

    public CompanyKeyResult getCompanyKeyResult() {
        return companyKeyResult;
    }

    public BusinessUnitObjective getBusinessUnitObjective() {
        return businessUnitObjective;
    }

    public Set<BusinessUnitKeyResultHistory> getBusinessUnitKeyResultHistories() {
        return businessUnitKeyResultHistories;
    }
}
