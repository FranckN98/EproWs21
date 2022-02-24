package de.thbingen.epro.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.thbingen.epro.model.assembler.BusinessUnitObjectiveAssembler;
import de.thbingen.epro.model.dto.BusinessUnitObjectiveDto;
import de.thbingen.epro.model.entity.BusinessUnit;
import de.thbingen.epro.model.entity.BusinessUnitObjective;
import de.thbingen.epro.model.mapper.BusinessUnitObjectiveMapper;
import de.thbingen.epro.service.BusinessUnitKeyResultService;
import de.thbingen.epro.service.BusinessUnitObjectiveService;
import de.thbingen.epro.service.CompanyKeyResultService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = BusinessUnitObjectiveController.class)
public class BusinessUnitObjectiveControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private BusinessUnitObjectiveService businessUnitObjectiveService;
    @MockBean
    private BusinessUnitKeyResultService businessUnitKeyResultService;
    @MockBean
    private CompanyKeyResultService companyKeyResultService;

    private final BusinessUnitObjectiveMapper mapper = Mappers.getMapper(BusinessUnitObjectiveMapper.class);

    private final BusinessUnitObjectiveAssembler assembler = new BusinessUnitObjectiveAssembler(mapper);

    // region GET ALL

    @Test
    @DisplayName("Get All should return all Business Unit Objectives with 200 - OK")
    public void getAllShouldReturnAllBusinessUnitObjectives() throws Exception {
        BusinessUnit businessUnit = new BusinessUnit(1L, "Personal");
        Set<BusinessUnitObjectiveDto> businessUnitObjectives = Stream.of(
                new BusinessUnitObjective(1L, 0, "Test1", OffsetDateTime.now(), OffsetDateTime.now()),
                new BusinessUnitObjective(2L, 0, "Test2", OffsetDateTime.now(), OffsetDateTime.now())
        ).map(businessUnitObjective -> {
            businessUnitObjective.setBusinessUnit(businessUnit);
            return assembler.toModel(businessUnitObjective);
        }).collect(Collectors.toSet());

        List<BusinessUnitObjectiveDto> businessUnitObjectiveDtos = new ArrayList<>(businessUnitObjectives);

        when(businessUnitObjectiveService.getAllBusinessUnitObjectives(Pageable.ofSize(10))).thenReturn(new PageImpl<>(businessUnitObjectiveDtos));

        mockMvc.perform(get("/businessUnitObjectives").accept(MediaTypes.HAL_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(3)))
                .andExpect(jsonPath("$.page").exists())
                .andExpect(jsonPath("$._embedded").exists())
                .andExpect(jsonPath("$._embedded.businessUnitObjectives").exists())
                .andExpect(jsonPath("$._embedded.businessUnitObjectives", hasSize(2)))
                .andExpect(jsonPath("$._embedded.businessUnitObjectives[*]._links").exists())
                .andExpect(jsonPath("$._embedded.businessUnitObjectives[0]._links.self.href", endsWith("/businessUnitObjectives/1")))
                .andExpect(jsonPath("$._embedded.businessUnitObjectives[0].name", is("Test1")))
                .andExpect(jsonPath("$._embedded.businessUnitObjectives[0].achievement", is(0)))
                .andExpect(jsonPath("$._embedded.businessUnitObjectives[1]._links.self.href", endsWith("/businessUnitObjectives/2")))
                .andExpect(jsonPath("$._embedded.businessUnitObjectives[1].name", is("Test2")))
                .andExpect(jsonPath("$._embedded.businessUnitObjectives[1].achievement", is(0)))
                .andExpect(jsonPath("$._links").exists())
                .andExpect(jsonPath("$._links.self.href", endsWith("/businessUnitObjectives")))
                .andReturn();
    }

    // endregion

    // region GET with id

    @Test
    @DisplayName("Get With ID should Return a single Company Objective with 200 - OK")
    public void getWithIdShouldReturnSingleCompanyObjective() throws Exception {
        BusinessUnit businessUnit = new BusinessUnit(1L, "Personal");
        BusinessUnitObjective businessUnitObjective = new BusinessUnitObjective(1L, 0, "Test1", OffsetDateTime.now(), OffsetDateTime.now());
        businessUnitObjective.setBusinessUnit(businessUnit);
        when(businessUnitObjectiveService.findById(1L)).thenReturn(Optional.of(assembler.toModel(businessUnitObjective)));

        mockMvc.perform(get("/businessUnitObjectives/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test1"))
                .andExpect(jsonPath("$._links").exists())
                .andExpect(jsonPath("$._links.self.href", endsWith("/businessUnitObjectives/1")));
    }

    // region POST

    @Test
    @DisplayName("Post with valid body should return 201 - Created with location header")
    public void postWithValidBodyShouldReturnCreatedWithLocationHeader() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        BusinessUnitObjective businessUnitObjective = new BusinessUnitObjective(1L, 0, "Test1", OffsetDateTime.now(), OffsetDateTime.now());
        BusinessUnit businessUnit = new BusinessUnit(1L, "Personal");
        businessUnitObjective.setBusinessUnit(businessUnit);
        BusinessUnitObjectiveDto toPost = assembler.toModel(businessUnitObjective);
        String jsonToPost = objectMapper.writeValueAsString(toPost);

        when(businessUnitObjectiveService.insertBusinessUnitObjective(ArgumentMatchers.any(BusinessUnitObjectiveDto.class))).thenReturn(toPost);

        mockMvc.perform(
                        post("/businessUnitObjectives")
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
    @DisplayName("Post with invalid body should return 400 - Bad Request")
    public void postWithInvalidDtoShouldReturnBadRequest() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        BusinessUnitObjective businessUnitObjective = new BusinessUnitObjective(1L, 0, "", OffsetDateTime.now(), OffsetDateTime.now());
        BusinessUnit businessUnit = new BusinessUnit(1L, "Personal");
        businessUnitObjective.setBusinessUnit(businessUnit);
        BusinessUnitObjectiveDto toPost = assembler.toModel(businessUnitObjective);
        String invalidJson = objectMapper.writeValueAsString(toPost);

        when(businessUnitObjectiveService.updateBusinessUnitObjective(anyLong(), ArgumentMatchers.any(BusinessUnitObjectiveDto.class))).thenReturn(toPost);

        mockMvc.perform(
                        post("/businessUnitObjectives")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(invalidJson)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.httpStatus").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.timestamp", matchesPattern("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}.\\d+")))
                .andExpect(jsonPath("$.message").value("Invalid JSON"))
                .andExpect(jsonPath("$.debugMessage").doesNotExist())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors[0].object").value("businessUnitObjectiveDto"))
                .andExpect(jsonPath("$.errors[0].field").value("name"))
                .andExpect(jsonPath("$.errors[0].rejectedValue").value(""))
                .andExpect(jsonPath("$.errors[0].message").value("must not be blank"));
    }

    @Test
    @DisplayName("Post with malformatted json should return 400 - Bad Request")
    public void postWithMalformattedJsonShouldReturnBadRequest() throws Exception {
        String malformattedJson = "{";

        mockMvc.perform(post("/businessUnitObjectives")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(malformattedJson))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.httpStatus").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.timestamp", matchesPattern("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}.\\d+")))
                .andExpect(jsonPath("$.message").value("Malformed JSON request"))
                .andExpect(jsonPath("$.debugMessage", Matchers.startsWith("JSON parse error:")))
                .andExpect(jsonPath("$.errors").isEmpty());
    }

    @Test
    @DisplayName("Post with id should return 405 - Method not allowed")
    public void postWithIdShouldReturnMethodNotAllowed() throws Exception {
        mockMvc.perform(post("/businessUnitObjectives/1"))
                .andExpect(status().isMethodNotAllowed());
    }
    // endregion
    // region PUT

    @Test
    @DisplayName("Valid put should return 200 - OK when Object is being updated")
    public void validPutShouldReturnOkWhenObjectIsBeingUpdated() throws Exception {
        BusinessUnitObjective businessUnitObjective = new BusinessUnitObjective(1L, 0, "changedName", OffsetDateTime.now(), OffsetDateTime.now());
        BusinessUnit businessUnit = new BusinessUnit(1L, "Personal");
        businessUnitObjective.setBusinessUnit(businessUnit);
        BusinessUnitObjectiveDto businessUnitObjectiveDto = assembler.toModel(businessUnitObjective);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        String jsonToPut = objectMapper.writeValueAsString(businessUnitObjectiveDto);

        when(businessUnitObjectiveService.existsById(1L)).thenReturn(true);
        when(businessUnitObjectiveService.updateBusinessUnitObjective(anyLong(), any(BusinessUnitObjectiveDto.class))).thenReturn(businessUnitObjectiveDto);

        mockMvc.perform(put("/businessUnitObjectives/1").contentType(MediaType.APPLICATION_JSON).content(jsonToPut))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("changedName"))
                .andExpect(jsonPath("$._links").exists())
                .andExpect(jsonPath("$._links.self.href", endsWith("/businessUnitObjectives/1")));
    }

    @Test
    @DisplayName("Valid put should return 201 - Created when Object does not already Exist")
    public void validPutShouldReturnCreatedWhenObjectDoesNotAlreadyExist() throws Exception {
        BusinessUnitObjective businessUnitObjective = new BusinessUnitObjective(1L, 0, "changedName", OffsetDateTime.now(), OffsetDateTime.now());
        BusinessUnit businessUnit = new BusinessUnit(1L, "Personal");
        businessUnitObjective.setBusinessUnit(businessUnit);
        BusinessUnitObjectiveDto businessUnitObjectiveDto = assembler.toModel(businessUnitObjective);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        String jsonToPut = objectMapper.writeValueAsString(businessUnitObjectiveDto);

        when(businessUnitObjectiveService.existsById(1L)).thenReturn(false);
        when(businessUnitObjectiveService.insertBusinessUnitObjective(any(BusinessUnitObjectiveDto.class))).thenReturn(businessUnitObjectiveDto);

        mockMvc.perform(put("/businessUnitObjectives/1").contentType(MediaType.APPLICATION_JSON).content(jsonToPut))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("changedName"))
                .andExpect(jsonPath("$._links").exists())
                .andExpect(jsonPath("$._links.self.href", endsWith("/businessUnitObjectives/1")));
    }

    // endregion

    // region DELETE

    @Test
    @DisplayName("Delete with valid ID should return 204 - No Content")
    public void deleteWithValidIdShouldReturnNoContent() throws Exception {
        when(businessUnitObjectiveService.existsById(1L)).thenReturn(true);
        doNothing().when(businessUnitObjectiveService).deleteById(1L);

        mockMvc.perform(delete("/businessUnitObjectives/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Delete with Invalid ID should return 404 - Not found")
    public void deleteWithInvalidIdShouldReturnNotFound() throws Exception {
        when(businessUnitObjectiveService.existsById(100L)).thenReturn(false);
        doNothing().when(businessUnitObjectiveService).deleteById(100L);

        mockMvc.perform(delete("/businessUnitObjectives/100"))
                .andExpect(status().isNotFound());
    }

    // endregion
}
