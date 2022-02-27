package de.thbingen.epro.repository;

import de.thbingen.epro.model.entity.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Page<Role> findAllByPrivileges_Roles_Id(Long privileges_roles_id, Pageable pageable);
}
