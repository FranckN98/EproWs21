package de.thbingen.epro.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.thbingen.epro.model.assembler.BusinessUnitKeyResultAssembler;
import de.thbingen.epro.model.dto.BusinessUnitKeyResultDto;
import de.thbingen.epro.model.entity.BusinessUnitKeyResult;
import de.thbingen.epro.model.mapper.BusinessUnitKeyResultMapper;
import de.thbingen.epro.service.BusinessUnitKeyResultHistoryService;
import de.thbingen.epro.service.BusinessUnitKeyResultService;
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

@WebMvcTest(controllers = BusinessUnitKeyResultController.class)
public class BusinessUnitKeyResultControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private BusinessUnitKeyResultService businessUnitKeyResultService;
    @MockBean
    private BusinessUnitKeyResultHistoryService businessUnitKeyResultHistoryService;
    @MockBean
    private CompanyKeyResultService companyKeyResultService;

    private final BusinessUnitKeyResultMapper businessUnitKeyResultMapper = Mappers.getMapper(BusinessUnitKeyResultMapper.class);
    private final BusinessUnitKeyResultAssembler businessUnitKeyResultAssembler = new BusinessUnitKeyResultAssembler(businessUnitKeyResultMapper);


    // region GET ALL

    @Test
    @DisplayName("Get All should return all Business Unit Objectives with 200 - OK")
    public void getAllShouldReturnAllBusinessUnitObjectives() throws Exception {
        Set<BusinessUnitKeyResultDto> businessUnitObjectiveDtos = Stream.of(
                new BusinessUnitKeyResult(1L, "BKR1", 10, 100, 50, "a comment", OffsetDateTime.now()),
                new BusinessUnitKeyResult(2L, "BKR2", 10, 100, 50, "no comment", OffsetDateTime.now())
        ).map(businessUnitKeyResultAssembler::toModel).collect(Collectors.toSet());


        when(businessUnitKeyResultService.getAllBusinessUnitKeyResults(Pageable.ofSize(10))).thenReturn(new PageImpl<>(new ArrayList<>(businessUnitObjectiveDtos)));

        mockMvc.perform(get("/businessUnitKeyResults").accept(MediaTypes.HAL_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(3)))
                .andExpect(jsonPath("$.page").exists())
                .andExpect(jsonPath("$._embedded").exists())
                .andExpect(jsonPath("$._embedded.businessUnitKeyResults").exists())
                .andExpect(jsonPath("$._embedded.businessUnitKeyResults", hasSize(2)))
                .andExpect(jsonPath("$._embedded.businessUnitKeyResults[*]._links").exists())
                .andExpect(jsonPath("$._embedded.businessUnitKeyResults[0]._links.self.href", endsWith("/businessUnitKeyResults/1")))
                .andExpect(jsonPath("$._embedded.businessUnitKeyResults[0].name", is("BKR1")))
                .andExpect(jsonPath("$._embedded.businessUnitKeyResults[1]._links.self.href", endsWith("/businessUnitKeyResults/2")))
                .andExpect(jsonPath("$._embedded.businessUnitKeyResults[1].name", is("BKR2")))
                .andExpect(jsonPath("$._links").exists())
                .andExpect(jsonPath("$._links.self.href", endsWith("/businessUnitKeyResults")))
                .andReturn();
        //TODO: fix Problem
    }

    // endregion
    // region GET with id

    @Test
    @DisplayName("Get With ID should Return a single Business Unit Key result with 200 - OK")
    public void getWithIdShouldReturnSingleBusinessUnitkeyResult() throws Exception {

        when(businessUnitKeyResultService.findById(1L)).thenReturn(Optional.of(businessUnitKeyResultAssembler.toModel(new BusinessUnitKeyResult(1L, "BKR1", 10, 100, 50, "a comment", OffsetDateTime.now()))));

        mockMvc.perform(get("/businessUnitKeyResults/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("BKR1"))
                .andExpect(jsonPath("$._links").exists())
                .andExpect(jsonPath("$._links.self.href", endsWith("/businessUnitKeyResults/1")));
    }

    // endregion

    // region POST

    @Test
    @DisplayName("Post with valid body should return 201 - Created with location header")
    public void postWithValidBodyShouldReturnCreatedWithLocationHeader() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        BusinessUnitKeyResult businessUnitKeyResult = new BusinessUnitKeyResult(1L, "BKR1", 10, 100, 50, "a comment", OffsetDateTime.now());
        BusinessUnitKeyResultDto toPost = businessUnitKeyResultAssembler.toModel(businessUnitKeyResult);
        String jsonToPost = objectMapper.writeValueAsString(toPost);

        when(businessUnitKeyResultService.saveBusinessUnitKeyResult(ArgumentMatchers.any(BusinessUnitKeyResultDto.class))).thenReturn(toPost);

        mockMvc.perform(
                        post("/businessUnitKeyResults")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonToPost)
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string("location", "/businessUnitKeyResults/1"))
                .andExpect(jsonPath("$.name").value("BKR1"))
                .andExpect(jsonPath("$._links").exists())
                .andExpect(jsonPath("$._links.self.href", endsWith("/businessUnitKeyResults/1")));
    }

    @Test
    @DisplayName("Post with invalid body should return 400 - Bad Request")
    public void postWithInvalidDtoShouldReturnBadRequest() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        BusinessUnitKeyResult businessUnitKeyResult = new BusinessUnitKeyResult(1L, "", 10, 100, 50, "a comment", OffsetDateTime.now());
        BusinessUnitKeyResultDto toPost = businessUnitKeyResultAssembler.toModel(businessUnitKeyResult);
        String invalidJson = objectMapper.writeValueAsString(toPost);

        mockMvc.perform(post("/businessUnitKeyResults")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.httpStatus").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.timestamp", matchesPattern("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}.\\d+")))
                .andExpect(jsonPath("$.message").value("Invalid JSON"))
                .andExpect(jsonPath("$.debugMessage").doesNotExist())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors[0].object").value("businessUnitKeyResultDto"))
                .andExpect(jsonPath("$.errors[0].rejectedValue").value(""))
                .andExpect(jsonPath("$.errors[0].message").value("must not be blank"));
    }

    @Test
    @DisplayName("Post with malformatted json should return 400 - Bad Request")
    public void postWithMalformattedJsonShouldReturnBadRequest() throws Exception {
        String malformattedJson = "{";

        mockMvc.perform(post("/businessUnitKeyResults")
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
        mockMvc.perform(post("/businessUnitKeyResults/1"))
                .andExpect(status().isMethodNotAllowed());
    }

    // endregion

    // region PUT

    @Test
    @DisplayName("Valid put should return 200 - OK when Object is being updated")
    public void validPutShouldReturnOkWhenObjectIsBeingUpdated() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        BusinessUnitKeyResult businessUnitKeyResult = new BusinessUnitKeyResult(1L, "changedName", 10, 100, 50, "a comment", OffsetDateTime.now());
        BusinessUnitKeyResultDto toPut = businessUnitKeyResultAssembler.toModel(businessUnitKeyResult);
        String jsonToPut = objectMapper.writeValueAsString(toPut);

        when(businessUnitKeyResultService.existsById(1L)).thenReturn(true);
        when(businessUnitKeyResultService.updateBusinessUnitKeyResult(anyLong(), any(BusinessUnitKeyResultDto.class))).thenReturn(toPut);

        mockMvc.perform(put("/businessUnitKeyResults/1").contentType(MediaType.APPLICATION_JSON).content(jsonToPut))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("changedName"))
                .andExpect(jsonPath("$._links").exists())
                .andExpect(jsonPath("$._links.self.href", endsWith("/businessUnitKeyResults/1")));
    }

    @Test
    @DisplayName("Valid put should return 201 - Created when Object does not already Exist")
    public void validPutShouldReturnCreatedWhenObjectDoesNotAlreadyExist() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        BusinessUnitKeyResult businessUnitKeyResult = new BusinessUnitKeyResult(1L, "changedName", 10, 100, 50, "a comment", OffsetDateTime.now());
        BusinessUnitKeyResultDto toPut = businessUnitKeyResultAssembler.toModel(businessUnitKeyResult);
        String jsonToPut = objectMapper.writeValueAsString(toPut);

        when(businessUnitKeyResultService.existsById(1L)).thenReturn(false);
        when(businessUnitKeyResultService.saveBusinessUnitKeyResult(any(BusinessUnitKeyResultDto.class))).thenReturn(toPut);

        mockMvc.perform(put("/businessUnitKeyResults/1").contentType(MediaType.APPLICATION_JSON).content(jsonToPut))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("changedName"))
                .andExpect(jsonPath("$._links").exists())
                .andExpect(jsonPath("$._links.self.href", endsWith("/businessUnitKeyResults/1")));
    }

    // endregion

    // region DELETE

    @Test
    @DisplayName("Delete with valid ID should return 204 - No Content")
    public void deleteWithValidIdShouldReturnNoContent() throws Exception {
        when(businessUnitKeyResultService.existsById(1L)).thenReturn(true);
        doNothing().when(businessUnitKeyResultService).deleteById(1L);

        mockMvc.perform(delete("/businessUnitKeyResults/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Delete with Invalid ID should return 404 - Not found")
    public void deleteWithInvalidIdShouldReturnNotFound() throws Exception {
        when(businessUnitKeyResultService.existsById(100L)).thenReturn(false);
        doNothing().when(businessUnitKeyResultService).deleteById(100L);

        mockMvc.perform(delete("/businessUnitKeyResults/100"))
                .andExpect(status().isNotFound());
    }

    // endregion


}
