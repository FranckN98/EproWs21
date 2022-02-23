package de.thbingen.epro.repository;

import de.thbingen.epro.model.entity.BusinessUnit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BusinessUnitRepository extends JpaRepository<BusinessUnit, Long> {

    /*@Query (" SELECT * FROM business_unit_objective where business_unit_id = :id")
    List<Long> findAllObjectiveForUnit(@Param("id") Integer id);*/
}
