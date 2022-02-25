package de.thbingen.epro.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.thbingen.epro.model.assembler.BusinessUnitAssembler;
import de.thbingen.epro.model.assembler.BusinessUnitObjectiveAssembler;
import de.thbingen.epro.model.dto.BusinessUnitDto;
import de.thbingen.epro.model.dto.BusinessUnitObjectiveDto;
import de.thbingen.epro.model.entity.BusinessUnit;
import de.thbingen.epro.model.entity.BusinessUnitObjective;
import de.thbingen.epro.model.mapper.BusinessUnitMapper;
import de.thbingen.epro.model.mapper.BusinessUnitObjectiveMapper;
import de.thbingen.epro.service.BusinessUnitObjectiveService;
import de.thbingen.epro.service.BusinessUnitService;
import de.thbingen.epro.service.OkrUserService;
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

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
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

@WebMvcTest(controllers = BusinessUnitController.class)
public class BusinessUnitControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BusinessUnitService businessUnitService;

    @MockBean
    private BusinessUnitObjectiveService businessUnitObjectiveService;

    @MockBean
    private OkrUserService okrUserService;

    private BusinessUnitMapper mapper = Mappers.getMapper(BusinessUnitMapper.class);
    private final BusinessUnitAssembler assembler = new BusinessUnitAssembler(mapper);

    private final BusinessUnitObjectiveMapper businessUnitObjectiveMapper = Mappers.getMapper(BusinessUnitObjectiveMapper.class);
    private final BusinessUnitObjectiveAssembler businessUnitObjectiveAssembler = new BusinessUnitObjectiveAssembler(businessUnitObjectiveMapper);

    // region GET ALL

    @Test
    @DisplayName("Get All should return all Business Units with 200 - OK")
    public void getAllShouldReturnAllBusinessUnits() throws Exception {
        List<BusinessUnitDto> businessUnits = Stream.of(
                new BusinessUnit(1L, "Personal"),
                new BusinessUnit(2L, "IT")
        ).map(assembler::toModel).collect(Collectors.toList());
        when(businessUnitService.findAll(Pageable.ofSize(10))).thenReturn(new PageImpl<>(businessUnits));
        mockMvc.perform(get("/businessUnits").accept(MediaTypes.HAL_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(3)))
                .andExpect(jsonPath("$.page").exists())
                .andExpect(jsonPath("$._embedded").exists())
                .andExpect(jsonPath("$._embedded.businessUnits").exists())
                .andExpect(jsonPath("$._embedded.businessUnits", hasSize(2)))
                .andExpect(jsonPath("$._embedded.businessUnits[*]._links").exists())
                .andExpect(jsonPath("$._embedded.businessUnits[0]._links.self.href", endsWith("/businessUnits/1")))
                .andExpect(jsonPath("$._embedded.businessUnits[0].name", is("Personal")))
                .andExpect(jsonPath("$._embedded.businessUnits[1]._links.self.href", endsWith("/businessUnits/2")))
                .andExpect(jsonPath("$._embedded.businessUnits[1].name", is("IT")))
                .andExpect(jsonPath("$._links").exists())
                .andExpect(jsonPath("$._links.self.href", endsWith("/businessUnits")))
                .andReturn();
    }

    // endregion

    // region GET with id

    @Test
    @DisplayName("Get With ID should Return a single BusinessUnit with 200 - OK")
    public void getWithIdShouldReturnSingleBusinessUnit() throws Exception {
        when(businessUnitService.findById(1L)).thenReturn(Optional.of(assembler.toModel(new BusinessUnit(1L, "Personal"))));

        mockMvc.perform(get("/businessUnits/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Personal"))
                .andExpect(jsonPath("$._links").exists())
                .andExpect(jsonPath("$._links.self.href", endsWith("/businessUnits/1")));
    }

    // endregion

    // region POST

    @Test
    @DisplayName("Post with valid body should return 201 - Created with location header")
    public void postWithValidBodyShouldReturnCreatedWithLocationHeader() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        BusinessUnit businessUnit = new BusinessUnit(1L, "TEST");
        BusinessUnitDto toPost = assembler.toModel(businessUnit);
        String jsonToPost = objectMapper.writeValueAsString(toPost);

        when(businessUnitService.insertBusinessUnit(ArgumentMatchers.any(BusinessUnitDto.class))).thenReturn(toPost);

        mockMvc.perform(
                        post("/businessUnits")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonToPost)
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string("location", "/businessUnits/1"))
                .andExpect(jsonPath("$.name").value("TEST"))
                .andExpect(jsonPath("$._links").exists())
                .andExpect(jsonPath("$._links.self.href", endsWith("/businessUnits/1")));
    }

    @Test
    @DisplayName("Post with invalid body should return 400 - Bad Request")
    public void postWithInvalidDtoShouldReturnBadRequest() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        BusinessUnitDto toPost = assembler.toModel(new BusinessUnit(1L, ""));
        String invalidJson = objectMapper.writeValueAsString(toPost);

        mockMvc.perform(post("/businessUnits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.httpStatus").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.timestamp", matchesPattern("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}.\\d+")))
                .andExpect(jsonPath("$.message").value("Invalid JSON"))
                .andExpect(jsonPath("$.debugMessage").doesNotExist())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors[0].object").value("businessUnitDto"))
                .andExpect(jsonPath("$.errors[0].field").value("name"))
                .andExpect(jsonPath("$.errors[0].rejectedValue").value(""))
                .andExpect(jsonPath("$.errors[0].message").value("must not be blank"));
    }

    @Test
    @DisplayName("Post with malformatted json should return 400 - Bad Request")
    public void postWithMalformattedJsonShouldReturnBadRequest() throws Exception {
        String malformattedJson = "{";

        mockMvc.perform(post("/businessUnits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(malformattedJson))
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
        mockMvc.perform(post("/businessUnits/1"))
                .andExpect(status().isMethodNotAllowed());
    }

    // endregion

    // region PUT

    @Test
    @DisplayName("Valid put should return 200 - OK when Object is being updated")
    public void validPutShouldReturnOkWhenObjectIsBeingUpdated() throws Exception {
        BusinessUnitDto businessUnitDto = assembler.toModel(new BusinessUnit(1L, "Test"));
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonToPut = objectMapper.writeValueAsString(businessUnitDto);

        when(businessUnitService.existsById(1L)).thenReturn(true);
        when(businessUnitService.updateBusinessUnit(anyLong(), any(BusinessUnitDto.class))).thenReturn(businessUnitDto);

        mockMvc.perform(put("/businessUnits/1").contentType(MediaType.APPLICATION_JSON).content(jsonToPut))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test"))
                .andExpect(jsonPath("$._links").exists())
                .andExpect(jsonPath("$._links.self.href", endsWith("/businessUnits/1")));
    }

    // endregion

    // region DELETE

    @Test
    @DisplayName("Delete with valid ID should return 204 - No Content")
    public void deleteWithValidIdShouldReturnNoContent() throws Exception {
        when(businessUnitService.existsById(1L)).thenReturn(true);
        doNothing().when(businessUnitService).deleteById(1L);

        mockMvc.perform(delete("/businessUnits/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Delete with Invalid ID should return 404 - Not found")
    public void deleteWithInvalidIdShouldReturnNotFound() throws Exception {
        when(businessUnitService.existsById(100L)).thenReturn(false);
        doNothing().when(businessUnitService).deleteById(100L);

        mockMvc.perform(delete("/businessUnits/100"))
                .andExpect(status().isNotFound());
    }

    // endregion





    @Test
    @DisplayName("Post Objective with valid body should return 201 - Created with location header")
    public void postObjectiveWithValidBodyShouldReturnCreatedWithLocationHeader() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        BusinessUnit businessUnit = new BusinessUnit(1L, "Personal");
        BusinessUnitObjective businessUnitObjective = new BusinessUnitObjective(1L, 0, "Test1", LocalDate.now(), LocalDate.now());
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
    @DisplayName("Post Objective with Invalid Business Unit Id should return 404 - Not found")
    public void postObjectiveWithInvalidBusinessUnitIdShouldReturnNoFound() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        BusinessUnit businessUnit = new BusinessUnit(1L, "Personal");
        BusinessUnitObjective businessUnitObjective = new BusinessUnitObjective(1L, 0, "Test1", LocalDate.now(), LocalDate.now());
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
}
