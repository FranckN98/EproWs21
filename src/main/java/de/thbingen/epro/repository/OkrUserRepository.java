package de.thbingen.epro.repository;

import de.thbingen.epro.model.entity.OkrUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OkrUserRepository extends JpaRepository<OkrUser, Long> {
    OkrUser findBySurname(String surname);

    Page<OkrUser> findAllByBusinessUnitId(Long businessUnitId, Pageable pageable);
}
