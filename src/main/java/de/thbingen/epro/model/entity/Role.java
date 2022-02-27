package de.thbingen.epro.model.entity;


import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "privileges_in_role",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "privilege_id")
    )
    private Set<Privilege> privileges;

    @OneToMany(targetEntity = OkrUser.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "role_id")
    private Set<OkrUser> okrUsers = new HashSet<>();

    public Role(String name) {
        this.name = name;
    }

    public Role() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<Privilege> getPrivileges() {
        return privileges;
    }

    public void setPrivileges(Set<Privilege> privileges) {
        this.privileges = privileges;
    }

    public void setOkrUsers(Set<OkrUser> okrUsers) {
        okrUsers = okrUsers;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<OkrUser> getOkrUsers() {
        return okrUsers;
    }

    public void addPrivilege(Privilege privilege) {
        privileges.add(privilege);
    }


}
