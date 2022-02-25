package de.thbingen.epro.repository;

import de.thbingen.epro.model.entity.OkrUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OkrUserRepository extends JpaRepository<OkrUser, Long> {
    Page<OkrUser> findAllByBusinessUnitId(Long businessUnitId, Pageable pageable);
    Optional<OkrUser> findByUsername(String username);
}
