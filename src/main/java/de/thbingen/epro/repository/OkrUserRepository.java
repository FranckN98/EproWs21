package de.thbingen.epro.repository;

import de.thbingen.epro.model.business.OkrUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OkrUserRepository extends JpaRepository<OkrUser, Long> {
    OkrUser findByUsername(String username);
}
