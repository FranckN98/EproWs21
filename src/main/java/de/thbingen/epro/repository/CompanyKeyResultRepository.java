package de.thbingen.epro.repository;

import de.thbingen.epro.model.entity.CompanyKeyResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CompanyKeyResultRepository extends JpaRepository<CompanyKeyResult, Long> {
    Page<CompanyKeyResult> findAllByCompanyObjectiveId(Long companyObjective_id, Pageable pageable);

    List<CompanyKeyResult> findAllByCompanyObjectiveId(Long companyObjective_id);
}
