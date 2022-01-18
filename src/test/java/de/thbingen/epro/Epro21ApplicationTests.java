package de.thbingen.epro;

import de.thbingen.epro.controller.CompanyObjectiveController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class Epro21ApplicationTests {

	@Autowired
	private CompanyObjectiveController companyObjectiveController;

	@Test
	void contextLoads() {
		assertThat(companyObjectiveController).isNotNull();
	}

}
