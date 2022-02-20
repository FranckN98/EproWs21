package de.thbingen.epro.repository;

import de.thbingen.epro.model.business.BusinessUnitKeyResultHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BusinessUnitKeyResultHistoryRepository extends JpaRepository<BusinessUnitKeyResultHistory, Long> {
    Page<BusinessUnitKeyResultHistory> findAllByCurrentBusinessUnitKeyResultIdOrderByChangeTimeStampDesc(Long currentBusinessUnitKeyResult_id, Pageable pageable);
}
