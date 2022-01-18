package de.thbingen.epro.repository;

import de.thbingen.epro.model.business.BusinessUnitKeyResultHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BKRH extends JpaRepository<BusinessUnitKeyResultHistory, Long> {
}
