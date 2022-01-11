package de.thbingen.epro21.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Role
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "privilege_id")
    private Privilege privilege;

    @OneToMany(targetEntity = OKRUser.class, cascade = CascadeType.ALL)
    @JoinColumn(name="role_id")
    private Set<OKRUser> okrUsers = new HashSet<>();

    public Role(String name) {
        this.name = name;
    }

    public Role() {}

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

    public Privilege getPrivilege() {
        return privilege;
    }

    public Set<OKRUser> getOkrUsers() {
        return okrUsers;
    }
}
