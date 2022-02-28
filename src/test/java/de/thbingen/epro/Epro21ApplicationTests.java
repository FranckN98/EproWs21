package de.thbingen.epro;


import de.thbingen.epro.model.entity.BusinessUnit;
import de.thbingen.epro.repository.BusinessUnitRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class Epro21ApplicationTests {

    @Autowired
    private BusinessUnitRepository businessUnitRepository;

    @Test
    @Transactional
    void contextLoads() {
        assertNotNull(businessUnitRepository);
    }

    @Test
    @Transactional
    void dbIsInitializedCorrectly() {
        BusinessUnit businessUnit = businessUnitRepository.getById(1L);
        assertNotNull(businessUnit);
        assertEquals("Personal", businessUnit.getName());
    }
}
