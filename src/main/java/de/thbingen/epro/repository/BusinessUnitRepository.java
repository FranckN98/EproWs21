package de.thbingen.epro.repository;

import de.thbingen.epro.model.business.BusinessUnit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BusinessUnitRepository extends JpaRepository<BusinessUnit, Long> {
}
