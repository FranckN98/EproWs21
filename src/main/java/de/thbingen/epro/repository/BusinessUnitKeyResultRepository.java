package de.thbingen.epro.repository;

import de.thbingen.epro.model.entity.BusinessUnitKeyResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BusinessUnitKeyResultRepository extends JpaRepository<BusinessUnitKeyResult, Long> {
    Page<BusinessUnitKeyResult> findAllByBusinessUnitObjectiveId(Long id, Pageable pageable);
    List<BusinessUnitKeyResult> findAllByBusinessUnitObjectiveId(Long id);
}
