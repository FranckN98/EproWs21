package de.thbingen.epro.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.thbingen.epro.exception.RestExceptionHandler;
import de.thbingen.epro.model.assembler.BusinessUnitAssembler;
import de.thbingen.epro.model.assembler.BusinessUnitObjectiveAssembler;
import de.thbingen.epro.model.assembler.CompanyObjectiveAssembler;
import de.thbingen.epro.model.dto.CompanyObjectiveDto;
import de.thbingen.epro.model.entity.CompanyObjective;
import de.thbingen.epro.model.mapper.BusinessUnitMapper;
import de.thbingen.epro.model.mapper.BusinessUnitObjectiveMapper;
import de.thbingen.epro.model.mapper.CompanyObjectiveMapper;
import de.thbingen.epro.service.CompanyKeyResultService;
import de.thbingen.epro.service.CompanyObjectiveService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.server.core.AnnotationLinkRelationProvider;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.Charset;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.time.temporal.TemporalAdjusters.firstDayOfYear;
import static java.time.temporal.TemporalAdjusters.lastDayOfYear;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = CompanyObjectiveController.class,
        useDefaultFilters = false,
        includeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        value = {
                                CompanyObjectiveController.class,
                                CompanyObjectiveMapper.class,
                                CompanyObjectiveAssembler.class
                        }
                )}
)
@Import(RestExceptionHandler.class)
@AutoConfigureMockMvc(addFilters = false)
public class CompanyObjectiveControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CompanyObjectiveService companyObjectiveService;

    @MockBean
    private CompanyKeyResultService companyKeyResultService;

    @Autowired
    private CompanyObjectiveAssembler assembler;

    // region GET ALL

    @Test
    @DisplayName("Get All should return all Company Objectives with 200 - OK")
    public void getAllshouldReturnAllCompanyObjectives() throws Exception {
        List<CompanyObjectiveDto> companyObjectives = Stream.of(
                new CompanyObjective(1L, 0, "name", LocalDate.now(), LocalDate.now()),
                new CompanyObjective(2L, 0, "test", LocalDate.now(), LocalDate.now())
        ).map(assembler::toModel).collect(Collectors.toList());
        when(companyObjectiveService.getAllCompanyObjectives(Pageable.ofSize(10), LocalDate.now().with(firstDayOfYear()), LocalDate.now().with(lastDayOfYear()))).thenReturn(new PageImpl<>(companyObjectives));

        mockMvc.perform(get("/companyobjectives").accept(MediaTypes.HAL_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(3)))
                .andExpect(jsonPath("$.page").exists())
                .andExpect(jsonPath("$._embedded").exists())
                .andExpect(jsonPath("$._embedded.companyObjectives").exists())
                .andExpect(jsonPath("$._embedded.companyObjectives", hasSize(2)))
                .andExpect(jsonPath("$._embedded.companyObjectives[*]._links").exists())
                .andExpect(jsonPath("$._embedded.companyObjectives[0]._links.self.href", endsWith("/companyobjectives/1")))
                .andExpect(jsonPath("$._embedded.companyObjectives[0].name", is("name")))
                .andExpect(jsonPath("$._embedded.companyObjectives[0].achievement", is(0.0)))
                .andExpect(jsonPath("$._embedded.companyObjectives[1]._links.self.href", endsWith("/companyobjectives/2")))
                .andExpect(jsonPath("$._embedded.companyObjectives[1].name", is("test")))
                .andExpect(jsonPath("$._embedded.companyObjectives[1].achievement", is(0.0)))
                .andExpect(jsonPath("$._links").exists())
                .andExpect(jsonPath("$._links.self.href", endsWith("/companyobjectives")))
                .andReturn();
    }

    // endregion

    // region GET with id

    @Test
    @DisplayName("Get With ID should Return a single Company Objective with 200 - OK")
    public void getWithIdShouldReturnSingleCompanyObjective() throws Exception {
        when(companyObjectiveService.findById(1L)).thenReturn(Optional.of(assembler.toModel(new CompanyObjective(1L, 0, "name", LocalDate.now(), LocalDate.now()))));

        mockMvc.perform(get("/companyobjectives/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("name"))
                .andExpect(jsonPath("$._links").exists())
                .andExpect(jsonPath("$._links.self.href", endsWith("/companyobjectives/1")));
    }

    // endregion

    // region POST

    @Test
    @DisplayName("Post with valid body should return 201 - Created with location header")
    public void postWithValidBodyShouldReturnCreatedWithLocationHeader() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        CompanyObjective companyObjective = new CompanyObjective(1L, 0, "name", LocalDate.now(), LocalDate.now().plusDays(1));
        CompanyObjectiveDto toPost = assembler.toModel(companyObjective);
        String jsonToPost = objectMapper.writeValueAsString(toPost);

        when(companyObjectiveService.insertCompanyObjective(ArgumentMatchers.any(CompanyObjectiveDto.class))).thenReturn(toPost);

        mockMvc.perform(
                        post("/companyobjectives")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonToPost)
                                .characterEncoding(Charset.defaultCharset())
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string("location", "/companyobjectives/1"))
                .andExpect(jsonPath("$.name").value("name"))
                .andExpect(jsonPath("$.achievement").value(0))
                .andExpect(jsonPath("$._links").exists())
                .andExpect(jsonPath("$._links.self.href", endsWith("/companyobjectives/1")));
    }

    @Test
    @DisplayName("Post with invalid body should return 400 - Bad Request")
    public void postWithInvalidDtoShouldReturnBadRequest() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        CompanyObjective companyObjective = new CompanyObjective(1L, 0, "", LocalDate.now(), LocalDate.now().plusDays(1));
        CompanyObjectiveDto toPost = assembler.toModel(companyObjective);
        String invalidJson = objectMapper.writeValueAsString(toPost);

        when(companyObjectiveService.insertCompanyObjective(any(CompanyObjectiveDto.class))).thenReturn(toPost);

        mockMvc.perform(post("/companyobjectives")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson)
                        .characterEncoding(Charset.defaultCharset())
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.httpStatus").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.timestamp", matchesPattern("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}.\\d+")))
                .andExpect(jsonPath("$.message").value("Invalid JSON"))
                .andExpect(jsonPath("$.debugMessage").doesNotExist())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors[0].object").value("companyObjectiveDto"))
                .andExpect(jsonPath("$.errors[0].field").value("name"))
                .andExpect(jsonPath("$.errors[0].rejectedValue").value(""))
                .andExpect(jsonPath("$.errors[0].message").value("must not be blank"));
    }

    @Test
    @DisplayName("Post with malformatted json should return 400 - Bad Request")
    public void postWithMalformattedJsonShouldReturnBadRequest() throws Exception {
        String malformattedJson = "{";

        mockMvc.perform(post("/companyobjectives")
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
        mockMvc.perform(post("/companyobjectives/1"))
                .andExpect(status().isMethodNotAllowed());
    }

    // endregion

    // region PUT

    @Test
    @DisplayName("Valid put should return 200 - OK when Object is being updated")
    public void validPutShouldReturnOkWhenObjectIsBeingUpdated() throws Exception {
        CompanyObjectiveDto companyObjectiveDto = assembler.toModel(new CompanyObjective(1L, 0, "changedName", LocalDate.now(), LocalDate.now().plusDays(1)));
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        String jsonToPut = objectMapper.writeValueAsString(companyObjectiveDto);

        when(companyObjectiveService.existsById(1L)).thenReturn(true);
        when(companyObjectiveService.updateCompanyObjective(anyLong(), any(CompanyObjectiveDto.class))).thenReturn(companyObjectiveDto);

        mockMvc.perform(
                put("/companyobjectives/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonToPut)
                        .characterEncoding(Charset.defaultCharset())
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("changedName"))
                .andExpect(jsonPath("$._links").exists())
                .andExpect(jsonPath("$._links.self.href", endsWith("/companyobjectives/1")));
    }

    // endregion

    // region DELETE

    @Test
    @DisplayName("Delete with valid ID should return 204 - No Content")
    public void deleteWithValidIdShouldReturnNoContent() throws Exception {
        when(companyObjectiveService.existsById(1L)).thenReturn(true);
        doNothing().when(companyObjectiveService).deleteById(1L);

        mockMvc.perform(delete("/companyobjectives/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Delete with Invalid ID should return 404 - Not found")
    public void deleteWithInvalidIdShouldReturnNotFound() throws Exception {
        when(companyObjectiveService.existsById(100L)).thenReturn(false);
        doNothing().when(companyObjectiveService).deleteById(100L);

        mockMvc.perform(delete("/companyobjectives/100"))
                .andExpect(status().isNotFound());
    }

    // endregion

}
