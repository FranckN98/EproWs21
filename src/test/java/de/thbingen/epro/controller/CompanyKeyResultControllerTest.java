package de.thbingen.epro.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.thbingen.epro.model.assembler.CompanyKeyResultAssembler;
import de.thbingen.epro.model.dto.CompanyKeyResultDto;
import de.thbingen.epro.model.entity.CompanyKeyResult;
import de.thbingen.epro.model.mapper.CompanyKeyResultMapper;
import de.thbingen.epro.service.CompanyKeyResultHistoryService;
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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

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

@WebMvcTest(controllers = {CompanyKeyResultController.class, CompanyKeyResultHistoryController.class})
public class CompanyKeyResultControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CompanyKeyResultService companyKeyResultService;
    @MockBean
    private CompanyKeyResultHistoryService companyKeyResultHistoryService;

    private final CompanyKeyResultMapper companyKeyResultMapper = Mappers.getMapper(CompanyKeyResultMapper.class);
    private final CompanyKeyResultAssembler companyKeyResultAssembler = new CompanyKeyResultAssembler(companyKeyResultMapper);


    // region GET ALL

    @Test
    @DisplayName("Get All should return all Company Key Result with 200 - OK")
    public void getAllshouldReturnAllCompanyKeyresults() throws Exception {
        List<CompanyKeyResultDto> companyKeyResultDtos = Stream.of(
                new CompanyKeyResult(1L, "CK1", 10, 100, 0, 50, "a comment", OffsetDateTime.now()),
                new CompanyKeyResult(2L, "CK2", 30, 150, 0, 30, "no comment", OffsetDateTime.now())
        ).map(companyKeyResultAssembler::toModel).collect(Collectors.toList());

        when(companyKeyResultService.getAllCompanyKeyResults(ArgumentMatchers.any(Pageable.class)))
                .thenReturn(new PageImpl<>(companyKeyResultDtos));


        when(companyKeyResultService.getAllCompanyKeyResults(Pageable.ofSize(10)))
                .thenReturn(new PageImpl<>(companyKeyResultDtos));

        mockMvc.perform(get("/companyKeyResults"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(3)));
        //TODO: fix Problem

    }

    // endregion

    // region GET with id

    @Test
    @DisplayName("Get With ID should Return a single Company Key result with 200 - OK")
    public void getWithIdShouldReturnSingleCompanykeyResult() throws Exception {
        when(companyKeyResultService.findById(1L)).thenReturn(Optional.of(companyKeyResultAssembler.toModel(new CompanyKeyResult(1L, "CK1", 10, 100, 0, 50, "a comment", OffsetDateTime.now()))));

        mockMvc.perform(get("/companyKeyResults/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("CK1"))
                .andExpect(jsonPath("$._links").exists())
                .andExpect(jsonPath("$._links.self.href", endsWith("/companyKeyResults/1")));
    }

    // endregion
    // region POST

    @Test
    @DisplayName("Post with valid body should return 201 - Created with location header")
    public void postWithValidBodyShouldReturnCreatedWithLocationHeader() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        CompanyKeyResult companyKeyResult = new CompanyKeyResult(1L, "CK1", 10, 100, 0, 50, "a comment", OffsetDateTime.now());
        CompanyKeyResultDto toPost = companyKeyResultAssembler.toModel(companyKeyResult);
        String jsonToPost = objectMapper.writeValueAsString(toPost);

        when(companyKeyResultService.insertCompanyKeyResult(ArgumentMatchers.any(CompanyKeyResultDto.class))).thenReturn(toPost);

        mockMvc.perform(
                        post("/companyKeyResults")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonToPost)
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string("location", "/companyKeyResults/1"))
                .andExpect(jsonPath("$.name").value("CK1"))
                .andExpect(jsonPath("$.achievement").value(0))
                .andExpect(jsonPath("$._links").exists())
                .andExpect(jsonPath("$._links.self.href", endsWith("/companyKeyResults/1")));
    }

    @Test
    @DisplayName("Post with invalid body should return 400 - Bad Request")
    public void postWithInvalidDtoShouldReturnBadRequest() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        CompanyKeyResult companyKeyResult = new CompanyKeyResult(1L, "", 10, 100, 0, 50, "a comment", OffsetDateTime.now());
        CompanyKeyResultDto toPost = companyKeyResultAssembler.toModel(companyKeyResult);
        String invalidJson = objectMapper.writeValueAsString(toPost);

        mockMvc.perform(post("/companyKeyResults")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.httpStatus").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.timestamp", matchesPattern("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}.\\d+")))
                .andExpect(jsonPath("$.message").value("Invalid JSON"))
                .andExpect(jsonPath("$.debugMessage").doesNotExist())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors[0].object").value("companyKeyResultDto"))
                .andExpect(jsonPath("$.errors[0].field").value("name"))
                .andExpect(jsonPath("$.errors[0].rejectedValue").value(""))
                .andExpect(jsonPath("$.errors[0].message").value("must not be blank"));
    }

    @Test
    @DisplayName("Post with malformatted json should return 400 - Bad Request")
    public void postWithMalformattedJsonShouldReturnBadRequest() throws Exception {
        String malformattedJson = "{";

        mockMvc.perform(post("/companyKeyResults")
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
        mockMvc.perform(post("/companyKeyResults/1"))
                .andExpect(status().isMethodNotAllowed());
    }

    // endregion

    // region PUT

    @Test
    @DisplayName("Valid put should return 200 - OK when Object is being updated")
    public void validPutShouldReturnOkWhenObjectIsBeingUpdated() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        CompanyKeyResult companyKeyResult = new CompanyKeyResult(1L, "changedName", 10, 100, 0, 50, "a comment", OffsetDateTime.now());
        CompanyKeyResultDto toPut = companyKeyResultAssembler.toModel(companyKeyResult);
        String jsonToPut = objectMapper.writeValueAsString(toPut);

        when(companyKeyResultService.existsById(1L)).thenReturn(true);
        when(companyKeyResultService.updateCompanyKeyResult(anyLong(), any(CompanyKeyResultDto.class))).thenReturn(toPut);

        mockMvc.perform(put("/companyKeyResults/1").contentType(MediaType.APPLICATION_JSON).content(jsonToPut))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("changedName"))
                .andExpect(jsonPath("$._links").exists())
                .andExpect(jsonPath("$._links.self.href", endsWith("/companyKeyResults/1")));
    }

    @Test
    @DisplayName("Valid put should return 201 - Created when Object does not already Exist")
    public void validPutShouldReturnCreatedWhenObjectDoesNotAlreadyExist() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        CompanyKeyResult companyKeyResult = new CompanyKeyResult(1L, "changedName", 10, 100, 0, 50, "a comment", OffsetDateTime.now());
        CompanyKeyResultDto toPut = companyKeyResultAssembler.toModel(companyKeyResult);
        String jsonToPut = objectMapper.writeValueAsString(toPut);

        when(companyKeyResultService.existsById(1L)).thenReturn(false);
        when(companyKeyResultService.insertCompanyKeyResult(any(CompanyKeyResultDto.class))).thenReturn(toPut);

        mockMvc.perform(put("/companyKeyResults/1").contentType(MediaType.APPLICATION_JSON).content(jsonToPut))
                .andDo(print())
                .andExpect(status().isCreated()
                )
                .andExpect(jsonPath("$.name").value("changedName"))
                .andExpect(jsonPath("$._links").exists())
                .andExpect(jsonPath("$._links.self.href", endsWith("/companyKeyResults/1")));
    }

    // endregion

    // region DELETE

    @Test
    @DisplayName("Delete with valid ID should return 204 - No Content")
    public void deleteWithValidIdShouldReturnNoContent() throws Exception {
        when(companyKeyResultService.existsById(1L)).thenReturn(true);
        doNothing().when(companyKeyResultService).deleteById(1L);

        mockMvc.perform(delete("/companyKeyResults/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Delete with Invalid ID should return 404 - Not found")
    public void deleteWithInvalidIdShouldReturnNotFound() throws Exception {
        when(companyKeyResultService.existsById(100L)).thenReturn(false);
        doNothing().when(companyKeyResultService).deleteById(100L);

        mockMvc.perform(delete("/companyKeyResults/100"))
                .andExpect(status().isNotFound());
    }

    // endregion


}
