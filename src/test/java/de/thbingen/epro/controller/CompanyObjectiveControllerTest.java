package de.thbingen.epro.controller;

import de.thbingen.epro.service.CompanyObjectiveService;
import org.junit.jupiter.api.Test;
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

    @Test
    public void shouldReturnAllBusinessUnits() throws Exception {
        /*List<BusinessUnitDto> businessUnits = List.of(
                new BusinessUnitDto(1L, "Personal"),
                new BusinessUnitDto(2L, "IT")
        );
        when(businessUnitService.findAll()).thenReturn(businessUnits);
        mockMvc.perform(get("/businessUnits"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Personal"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("IT"))*/;
    }
}
