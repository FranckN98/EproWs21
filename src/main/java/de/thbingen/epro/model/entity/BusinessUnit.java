package de.thbingen.epro.model.entity;

import javax.persistence.*;
import java.util.Set;

@Entity
public class BusinessUnit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @OneToMany(targetEntity = BusinessUnitObjective.class, mappedBy = "businessUnit", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<BusinessUnitObjective> businessUnitObjectives;

    @OneToMany(targetEntity = OkrUser.class, mappedBy = "businessUnit", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<OkrUser> okrUsers;

    public BusinessUnit() {
    }

    public BusinessUnit(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public BusinessUnit(Long id, String name, Set<BusinessUnitObjective> businessUnitObjectives, Set<OkrUser> okrUsers) {
        this.id = id;
        this.name = name;
        this.businessUnitObjectives = businessUnitObjectives;
        this.okrUsers = okrUsers;
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

    public Set<BusinessUnitObjective> getBusinessUnitObjectives() {
        return businessUnitObjectives;
    }

    public void setBusinessUnitObjectives(Set<BusinessUnitObjective> businessUnitObjectives) {
        this.businessUnitObjectives = businessUnitObjectives;
    }

    public Set<OkrUser> getOkrUsers() {
        return okrUsers;
    }

    public void setOkrUsers(Set<OkrUser> okrUsers) {
        this.okrUsers = okrUsers;
    }
}
