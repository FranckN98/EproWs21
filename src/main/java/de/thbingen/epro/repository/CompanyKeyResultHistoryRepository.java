package de.thbingen.epro.repository;

import de.thbingen.epro.model.business.CompanyKeyResultHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyKeyResultHistoryRepository extends JpaRepository<CompanyKeyResultHistory, Long> {
    Page<CompanyKeyResultHistory> findAllByCompanyKeyResultIdOrderByChangeTimeStampDesc(Long companyKeyResult_id, Pageable pageable);
}
