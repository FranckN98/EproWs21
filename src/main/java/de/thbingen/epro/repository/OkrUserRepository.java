package de.thbingen.epro.repository;

import de.thbingen.epro.model.business.OkrUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OkrUserRepository extends JpaRepository<OkrUser, Long> {
    OkrUser findBySurname(String surname);

    Page<OkrUser> findAllByBusinessUnitId(Long businessUnitId, Pageable pageable);
}
