package de.thbingen.epro.repository;

import de.thbingen.epro.model.business.BusinessUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BusinessUnitRepository extends JpaRepository<BusinessUnit, Long> {

    /*@Query (" SELECT * FROM business_unit_objective where business_unit_id = :id")
    List<Long> findAllObjectiveForUnit(@Param("id") Integer id);*/
}
