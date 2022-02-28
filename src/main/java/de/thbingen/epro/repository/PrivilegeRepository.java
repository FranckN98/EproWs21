package de.thbingen.epro.repository;

import de.thbingen.epro.model.entity.Privilege;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrivilegeRepository extends JpaRepository<Privilege, Long> {
    Page<Privilege> findAllByRoles_Id(Long roleId, Pageable pageable);
}
