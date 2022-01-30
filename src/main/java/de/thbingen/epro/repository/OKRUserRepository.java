package de.thbingen.epro.repository;

import de.thbingen.epro.model.business.OKRUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OKRUserRepository extends JpaRepository<OKRUser, Long> {
}
