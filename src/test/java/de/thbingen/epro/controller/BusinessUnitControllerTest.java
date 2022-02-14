package de.thbingen.epro.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.thbingen.epro.model.dto.BusinessUnitDto;
import de.thbingen.epro.service.BusinessUnitService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.matchesPattern;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = BusinessUnitController.class)
@Import(BusinessUnitController.class)
public class BusinessUnitControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BusinessUnitService businessUnitService;

    // region GET ALL

    /*@Test
    @DisplayName("Get All should return all Business Units with 200 - OK")
    public void getAllshouldReturnAllBusinessUnits() throws Exception {
        List<BusinessUnitDto> businessUnits = List.of(
                new BusinessUnitDto(1L, "Personal"),
                new BusinessUnitDto(2L, "IT")
        );
        when(businessUnitService.findAll(0, 10, "id")).thenReturn(PageImpl(businessUnits));
        mockMvc.perform(get("/businessUnits"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Personal"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("IT"));
    }*/

    // endregion

    // region GET with id

    @Test
    @DisplayName("Get With ID should Return a single BusinessUnit with 200 - OK")
    public void getWithIdShouldReturnSingleBusinessUnit() throws Exception {
        when(businessUnitService.findById(1L)).thenReturn(Optional.of(new BusinessUnitDto(1L, "Personal")));

        mockMvc.perform(get("/businessUnits/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Personal"));
    }

    // endregion

    // region POST

    @Test
    @DisplayName("Post with valid body should return 201 - Created with location header")
    public void postWithValidBodyShouldReturnCreatedWithLocationHeader() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        BusinessUnitDto toPost = new BusinessUnitDto(null, "TEST");
        String jsonToPost = objectMapper.writeValueAsString(toPost);

        when(businessUnitService.saveBusinessUnit(ArgumentMatchers.any(BusinessUnitDto.class))).thenReturn(new BusinessUnitDto(1L, "TEST"));

        mockMvc.perform(
                post("/businessUnits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonToPost)
        )
                .andExpect(status().isCreated())
                .andExpect(header().string("location", "http://localhost:8080/api/v1/businessUnits/1"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("TEST"));
    }

    @Test
    @DisplayName("Post with invalid body should return 400 - Bad Request")
    public void postWithInvalidDtoShouldReturnBadRequest() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        BusinessUnitDto toPost = new BusinessUnitDto(null, "");
        String invalidJson = objectMapper.writeValueAsString(toPost);

        mockMvc.perform(post("/businessUnits")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson))
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
        BusinessUnitDto businessUnitDto = new BusinessUnitDto(1L, "Test");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonToPut = objectMapper.writeValueAsString(businessUnitDto);

        when(businessUnitService.existsById(1L)).thenReturn(true);
        when(businessUnitService.saveBusinessUnit(any(BusinessUnitDto.class))).thenReturn(businessUnitDto);

        mockMvc.perform(put("/businessUnits/1").contentType(MediaType.APPLICATION_JSON).content(jsonToPut))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test"));
    }

    @Test
    @DisplayName("Valid put should return 201 - Created when Object does not already Exist")
    public void validPutShouldReturnCreatedWhenObjectDoesNotAlreadyExist() throws Exception {
        BusinessUnitDto businessUnitDto = new BusinessUnitDto(1L, "Test");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonToPut = objectMapper.writeValueAsString(businessUnitDto);

        when(businessUnitService.existsById(1L)).thenReturn(false);
        when(businessUnitService.saveBusinessUnit(any(BusinessUnitDto.class))).thenReturn(businessUnitDto);

        mockMvc.perform(put("/businessUnits/1").contentType(MediaType.APPLICATION_JSON).content(jsonToPut))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test"));
    }

    @Test
    @DisplayName("Valid put should return 400 - Bad Request when Id in path and object do not match")
    public void validPutShouldReturnBadRequestWhenIdInPathAndObjectDoNotMatch() throws Exception {
        BusinessUnitDto businessUnitDto = new BusinessUnitDto(100L, "Test");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonToPut = objectMapper.writeValueAsString(businessUnitDto);

        mockMvc.perform(put("/businessUnits/1").contentType(MediaType.APPLICATION_JSON).content(jsonToPut))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.httpStatus").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.timestamp", matchesPattern("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}.\\d+")))
                .andExpect(jsonPath("$.message").value("Ids in path and jsonObject do not match"))
                .andExpect(jsonPath("$.errors").isEmpty());
    }

    @Test
    @DisplayName("Valid put should return 200 - OK when Id in Object is null and an update is being done")
    public void validPutShouldReturnOkWhenIdInObjectIsNullAndAnUpdateIsBeingDone() throws Exception {
        BusinessUnitDto businessUnitDto = new BusinessUnitDto(null, "Test");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonToPut = objectMapper.writeValueAsString(businessUnitDto);

        when(businessUnitService.existsById(1L)).thenReturn(true);
        when(businessUnitService.saveBusinessUnit(any(BusinessUnitDto.class))).thenReturn(new BusinessUnitDto(1L, "Test"));

        mockMvc.perform(put("/businessUnits/1").contentType(MediaType.APPLICATION_JSON).content(jsonToPut))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test"));
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
}
