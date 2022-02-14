package de.thbingen.epro.controller;

import de.thbingen.epro.service.CompanyObjectiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = CompanyObjectiveController.class)
public class CompanyObjectiveControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CompanyObjectiveService companyObjectiveService;

    /*@Test
    @DisplayName("Get All should return all Company Objectives with 200 - OK")
    public void getAllshouldReturnAllBusinessUnits() throws Exception {
        List<CompanyObjectiveDto> companyObjectives = List.of(
                new CompanyObjectiveDto(1L, 0, "name", new HashSet<>(), LocalDate.now(), LocalDate.now()),
                new CompanyObjectiveDto(2L, 0, "test", new HashSet<>(), LocalDate.now(), LocalDate.now())
        );
        when(companyObjectiveService.getAllCompanyObjectives(0, 10, "")).thenReturn(companyObjectives);
        mockMvc.perform(get("/businessUnits"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Personal"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("IT"));
    }*/
}
