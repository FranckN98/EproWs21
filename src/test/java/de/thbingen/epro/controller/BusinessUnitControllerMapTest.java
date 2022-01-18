package de.thbingen.epro.controller;

import de.thbingen.epro.model.dto.BusinessUnitDto;
import de.thbingen.epro.service.BusinessUnitService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BusinessUnitController.class)
@Import(BusinessUnitController.class)
public class BusinessUnitControllerMapTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BusinessUnitService businessUnitService;

    @Test
    public void shouldReturnAllBusinessUnits() throws Exception {
        List<BusinessUnitDto> businessUnits = List.of(
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
                .andExpect(jsonPath("$[1].name").value("IT"));
    }
}
