package de.thbingen.epro.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.thbingen.epro.controller.businessunit.BusinessUnitController;
import de.thbingen.epro.controller.businessunit.BusinessUnitObjectiveByBusinessUnitController;
import de.thbingen.epro.model.assembler.BusinessUnitAssembler;
import de.thbingen.epro.model.assembler.BusinessUnitObjectiveAssembler;
import de.thbingen.epro.model.dto.BusinessUnitObjectiveDto;
import de.thbingen.epro.model.entity.BusinessUnit;
import de.thbingen.epro.model.entity.BusinessUnitObjective;
import de.thbingen.epro.model.mapper.BusinessUnitMapper;
import de.thbingen.epro.model.mapper.BusinessUnitObjectiveMapper;
import de.thbingen.epro.service.BusinessUnitObjectiveService;
import de.thbingen.epro.service.BusinessUnitService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.OffsetDateTime;

import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.matchesPattern;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = {BusinessUnitObjectiveByBusinessUnitController.class, BusinessUnitController.class})
public class BusinessUnitObjectiveByBusinessUnitControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private BusinessUnitService businessUnitService;
    @MockBean
    private BusinessUnitObjectiveService businessUnitObjectiveService;

    private final BusinessUnitMapper businessUnitMapper = Mappers.getMapper(BusinessUnitMapper.class);
    private final BusinessUnitAssembler businessUnitAssembler = new BusinessUnitAssembler(businessUnitMapper);

    private final BusinessUnitObjectiveMapper businessUnitObjectiveMapper = Mappers.getMapper(BusinessUnitObjectiveMapper.class);
    private final BusinessUnitObjectiveAssembler businessUnitObjectiveAssembler = new BusinessUnitObjectiveAssembler(businessUnitObjectiveMapper);


    @Test
    @DisplayName("Post with valid body should return 201 - Created with location header")
    public void postWithValidBodyShouldReturnCreatedWithLocationHeader() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        BusinessUnit businessUnit = new BusinessUnit(1L, "Personal");
        BusinessUnitObjective businessUnitObjective = new BusinessUnitObjective(1L, 0, "Test1", OffsetDateTime.now(), OffsetDateTime.now());
        businessUnitObjective.setBusinessUnit(businessUnit);
        BusinessUnitObjectiveDto toPost = businessUnitObjectiveAssembler.toModel(businessUnitObjective);
        String jsonToPost = objectMapper.writeValueAsString(toPost);

        when(businessUnitService.existsById(anyLong())).thenReturn(true);
        when(businessUnitObjectiveService.insertBusinessUnitObjectiveWithBusinessUnit(ArgumentMatchers.any(BusinessUnitObjectiveDto.class), ArgumentMatchers.any(Long.class))).thenReturn(toPost);

        mockMvc.perform(
                        post("/businessUnits/1/objectives")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonToPost)
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string("location", "/businessUnitObjectives/1"))
                .andExpect(jsonPath("$.name").value("Test1"))
                .andExpect(jsonPath("$.achievement").value(0))
                .andExpect(jsonPath("$._links").exists())
                .andExpect(jsonPath("$._links.self.href", endsWith("/businessUnitObjectives/1")));


    }

    @Test
    @DisplayName("Post with Invalid Business Unit Id should return 404 - Not found")
    public void postWithInvalidBusinessUnitIdShouldReturnNoFound() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        BusinessUnit businessUnit = new BusinessUnit(1L, "Personal");
        BusinessUnitObjective businessUnitObjective = new BusinessUnitObjective(1L, 0, "Test1", OffsetDateTime.now(), OffsetDateTime.now());
        businessUnitObjective.setBusinessUnit(businessUnit);
        BusinessUnitObjectiveDto toPost = businessUnitObjectiveAssembler.toModel(businessUnitObjective);
        String jsonToPost = objectMapper.writeValueAsString(toPost);

        when(businessUnitObjectiveService.insertBusinessUnitObjectiveWithBusinessUnit(ArgumentMatchers.any(BusinessUnitObjectiveDto.class), ArgumentMatchers.any(Long.class))).thenReturn(toPost);

        mockMvc.perform(
                        post("/businessUnits/1/objectives")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonToPost)
                )
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.timestamp", matchesPattern("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}.\\d+")))
                .andExpect(jsonPath("$.message").value("No BusinessUnit with the given ID exists"))
                .andExpect(jsonPath("$.errors").isEmpty());


    }
    //TODO : (Nice to have) Test getAllByBusinessUnitId Controller
}
