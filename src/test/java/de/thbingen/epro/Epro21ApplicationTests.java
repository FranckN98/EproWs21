package de.thbingen.epro;

import de.thbingen.epro.repository.BusinessUnitRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class Epro21ApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private BusinessUnitRepository businessUnitRepository;

	@Test
	void contextLoads() {
		assertThat(businessUnitRepository).isNotNull();
	}

	@Test
	void test() throws Exception{
		mockMvc.perform(get("/businessUnits"))
				.andExpect(jsonPath("$.*").isArray())
				.andExpect(jsonPath("$.*", hasSize(1)));
	}

}
