package de.thbingen.epro.repository;

import de.thbingen.epro.model.entity.CompanyObjective;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface CompanyObjectiveRepository extends JpaRepository<CompanyObjective, Long> {
    Page<CompanyObjective> findAllByStartDateAfterAndEndDateBefore(LocalDate startDate, LocalDate endDate, Pageable pageable);
    List<CompanyObjective> findAllByStartDateBeforeAndEndDateAfter(LocalDate startDate, LocalDate endDate);
}