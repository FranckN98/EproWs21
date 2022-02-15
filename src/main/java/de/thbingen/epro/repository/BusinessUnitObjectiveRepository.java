package de.thbingen.epro.repository;

import de.thbingen.epro.model.business.BusinessUnitObjective;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface BusinessUnitObjectiveRepository extends JpaRepository<BusinessUnitObjective, Long> {
    Set<BusinessUnitObjective> findAllByBusinessUnitId(Long id);
}
