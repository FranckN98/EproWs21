package de.thbingen.epro.repository;

import de.thbingen.epro.model.entity.BusinessUnitObjective;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface BusinessUnitObjectiveRepository extends JpaRepository<BusinessUnitObjective, Long> {
    Page<BusinessUnitObjective> findAllByBusinessUnitIdAndStartDateAfterAndEndDateBefore(Long businessUnit_id, LocalDate startDate, LocalDate endDate, Pageable pageable);

    Page<BusinessUnitObjective> findAllByBusinessUnitIdAndStartDateGreaterThanEqualAndEndDateLessThanEqual(Long businessUnit_id, LocalDate startDate, LocalDate endDate, Pageable pageable);

    Page<BusinessUnitObjective> findAllByStartDateAfterAndEndDateBefore(LocalDate startDate, LocalDate endDate, Pageable pageable);

    List<BusinessUnitObjective> findAllByStartDateBeforeAndEndDateAfter(LocalDate startDate, LocalDate endDate);
}
