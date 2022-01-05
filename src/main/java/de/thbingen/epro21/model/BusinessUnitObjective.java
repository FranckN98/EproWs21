package de.thbingen.epro21.model;

import javax.persistence.*;

@Entity
@Table(name="businessunitobjective")
public class BusinessUnitObjective
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="coid")
    private Long id;
    private Integer achievement;
    private String name;
    private Long businessUnitId;

    public BusinessUnitObjective(Integer achievement, String name, Long businessUnitId) {
        this.achievement = achievement;
        this.name = name;
        this.businessUnitId = businessUnitId;
    }

    public BusinessUnitObjective() {
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

    public Long getBusinessUnitId() {
        return businessUnitId;
    }

    public void setBusinessUnitId(Long businessUnitId) {
        this.businessUnitId = businessUnitId;
    }
}
