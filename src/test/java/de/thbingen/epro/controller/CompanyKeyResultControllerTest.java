package de.thbingen.epro.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.thbingen.epro.exception.RestExceptionHandler;
import de.thbingen.epro.model.assembler.CompanyKeyResultAssembler;
import de.thbingen.epro.model.assembler.CompanyKeyResultHistoryAssembler;
import de.thbingen.epro.model.dto.CompanyKeyResultDto;
import de.thbingen.epro.model.dto.CompanyKeyResultUpdateDto;
import de.thbingen.epro.model.entity.CompanyKeyResult;
import de.thbingen.epro.model.mapper.CompanyKeyResultHistoryMapper;
import de.thbingen.epro.model.mapper.CompanyKeyResultMapper;
import de.thbingen.epro.model.mapper.HistoricalCompanyKeyResultMapper;
import de.thbingen.epro.service.CompanyKeyResultHistoryService;
import de.thbingen.epro.service.CompanyKeyResultService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CompanyKeyResultController.class,
        useDefaultFilters = false,
        includeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        value = {
                                CompanyKeyResultController.class,
                                CompanyKeyResultMapper.class,
                                CompanyKeyResultAssembler.class,
                                CompanyKeyResultHistoryMapper.class,
                                CompanyKeyResultHistoryAssembler.class,
                                HistoricalCompanyKeyResultMapper.class
                        }
                )}
)
@Import(RestExceptionHandler.class)
@AutoConfigureMockMvc(addFilters = false)
public class CompanyKeyResultControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CompanyKeyResultService companyKeyResultService;
    @MockBean
    private CompanyKeyResultHistoryService companyKeyResultHistoryService;

    @Autowired
    private CompanyKeyResultAssembler companyKeyResultAssembler;

    @Autowired
    private CompanyKeyResultMapper companyKeyResultMapper;


    // region GET ALL

    @Test
    @DisplayName("Get All should return all Company Key Result with 200 - OK")
    public void getAllshouldReturnAllCompanyKeyresults() throws Exception {
        List<CompanyKeyResultDto> companyKeyResultDtos = Stream.of(
                new CompanyKeyResult(1L, "CK1", 10f, 100f, 0f, 50f, "a comment", OffsetDateTime.now()),
                new CompanyKeyResult(2L, "CK2", 30f, 150f, 0f, 30f, "no comment", OffsetDateTime.now())
        ).map(companyKeyResultAssembler::toModel).collect(Collectors.toList());

        when(companyKeyResultService.findAllCompanyKeyResults(ArgumentMatchers.any(Pageable.class)))
                .thenReturn(new PageImpl<>(companyKeyResultDtos));


        when(companyKeyResultService.findAllCompanyKeyResults(Pageable.ofSize(10)))
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
        when(companyKeyResultService.findById(1L)).thenReturn(Optional.of(companyKeyResultAssembler.toModel(new CompanyKeyResult(1L, "CK1", 10f, 100f, 0f, 50f, "a comment", OffsetDateTime.now()))));

        mockMvc.perform(get("/companyKeyResults/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("CK1"))
                .andExpect(jsonPath("$._links").exists())
                .andExpect(jsonPath("$._links.self.href", endsWith("/companyKeyResults/1")));
    }

    // endregion

    // region PUT

    @Test
    @DisplayName("Valid put should return 200 - OK when Object is being updated")
    public void validPutShouldReturnOkWhenObjectIsBeingUpdated() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        CompanyKeyResult companyKeyResult = new CompanyKeyResult(1L, "changedName", 10f, 100f, 0f, 50f, "a comment", OffsetDateTime.now());
        CompanyKeyResultUpdateDto toPut = new CompanyKeyResultUpdateDto(10f, null, "Comment");
        String jsonToPut = objectMapper.writeValueAsString(toPut);

        when(companyKeyResultService.existsById(1L)).thenReturn(true);
        when(companyKeyResultService.updateCompanyKeyResult(anyLong(), any(CompanyKeyResultUpdateDto.class))).thenReturn(companyKeyResultAssembler.toModel(companyKeyResult));

        mockMvc.perform(put("/companyKeyResults/1").contentType(MediaType.APPLICATION_JSON).content(jsonToPut))
                .andDo(print())
                .andExpect(status().isOk())
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
