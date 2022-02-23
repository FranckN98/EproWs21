package de.thbingen.epro.repository;

import de.thbingen.epro.model.entity.BusinessUnitObjective;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BusinessUnitObjectiveRepository extends JpaRepository<BusinessUnitObjective, Long> {
    Page<BusinessUnitObjective> findAllByBusinessUnitId(Long id, Pageable pageable);
}
