package de.thbingen.epro.repository;

import de.thbingen.epro.model.business.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
