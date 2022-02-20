package de.thbingen.epro.repository;

import de.thbingen.epro.model.business.BusinessUnitObjective;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface BusinessUnitObjectiveRepository extends JpaRepository<BusinessUnitObjective, Long> {
    Page<BusinessUnitObjective> findAllByBusinessUnitId(Long id, Pageable pageable);
}
