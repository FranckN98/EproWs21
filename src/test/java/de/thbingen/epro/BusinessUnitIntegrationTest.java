package de.thbingen.epro;


import com.fasterxml.jackson.databind.ObjectMapper;
import de.thbingen.epro.controller.LoginController;
import de.thbingen.epro.model.dto.BusinessUnitDto;
import de.thbingen.epro.repository.BusinessUnitRepository;
import de.thbingen.epro.util.CamelCaseDisplayNameGenerator;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.hateoas.server.core.AnnotationLinkRelationProvider;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.Charset;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ComponentScan(basePackages = {"de.thbingen.epro"})
@AutoConfigureMockMvc
@DisplayNameGeneration(CamelCaseDisplayNameGenerator.class)
class BusinessUnitIntegrationTest {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    LoginController loginController;

    @Autowired
    private BusinessUnitRepository businessUnitRepository;

    @Autowired
    private AnnotationLinkRelationProvider annotationLinkRelationProvider;

    @LocalServerPort
    private Integer port;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public String doLogin() {
        return loginController.login(Map.of("username", "vor.nach1", "password", "password1"));
    }

    @Nested
    class TestCasesWithAdminAccount {

        @Test
        @Transactional
        void findAllReturnsTheTwoPresentBusinessUnits() throws Exception {
            String token = doLogin();

            String expectedLinkRelation = annotationLinkRelationProvider.getCollectionResourceRelFor(BusinessUnitDto.class).toString();

            mockMvc.perform(
                            get("/businessUnits")
                                    .header("Authorization", "Bearer " + token)
                    ).andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.*", hasSize(3)))
                    .andExpect(jsonPath("$.page").exists())
                    .andExpect(jsonPath("$._embedded").exists())
                    .andExpect(jsonPath("$._embedded." + expectedLinkRelation).exists())
                    .andExpect(jsonPath("$._embedded." + expectedLinkRelation, hasSize(2)))
                    .andExpect(jsonPath("$._embedded." + expectedLinkRelation + "[*]._links").exists())
                    .andExpect(jsonPath("$._embedded." + expectedLinkRelation + "[0]._links.self.href", endsWith("/businessUnits/1")))
                    .andExpect(jsonPath("$._embedded." + expectedLinkRelation + "[0]._links.businessUnitObjectives.href", matchesRegex("http:\\/\\/localhost\\/businessUnits\\/1\\/objectives(\\{\\?start,end\\})?")))
                    .andExpect(jsonPath("$._embedded." + expectedLinkRelation + "[0]._links.users.href", matchesRegex("http:\\/\\/localhost\\/businessUnits\\/1\\/users")))
                    .andExpect(jsonPath("$._embedded." + expectedLinkRelation + "[0].name", is("Personal")))
                    .andExpect(jsonPath("$._embedded." + expectedLinkRelation + "[1]._links.self.href", endsWith("/businessUnits/2")))
                    .andExpect(jsonPath("$._embedded." + expectedLinkRelation + "[1]._links.users.href", matchesRegex("http:\\/\\/localhost\\/businessUnits\\/2\\/users")))
                    .andExpect(jsonPath("$._embedded." + expectedLinkRelation + "[1].name", is("IT")))
                    .andExpect(jsonPath("$._links").exists())
                    .andExpect(jsonPath("$._links.self.href", matchesRegex("http:\\/\\/localhost\\/businessUnits(\\?page=\\d+&size=\\d+)?")));
        }

        @Test
        @Transactional
        void findByIdReturnsOneIfIdIsValid() throws Exception {
            String token = doLogin();

            mockMvc.perform(
                            get("/businessUnits/1")
                                    .header("Authorization", "Bearer " + token)
                    )
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.*", hasSize(2)))
                    .andExpect(jsonPath("$.name", is("Personal")))
                    .andExpect(jsonPath("$._links", aMapWithSize(3)))
                    .andExpect(jsonPath("$._links", hasKey("self")))
                    .andExpect(jsonPath("$._links.self.href", endsWith("/businessUnits/1")))
                    .andExpect(jsonPath("$._links", hasKey("businessUnitObjectives")))
                    .andExpect(jsonPath("$._links", hasKey("users")));
        }

        @Test
        @Transactional
        void findByIdReturns_404_IfIdDoesNotExist() throws Exception {
            String token = doLogin();

            mockMvc.perform(
                            get("/businessUnits/999999")
                                    .header("Authorization", "Bearer " + token)
                    )
                    .andDo(print())
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.*", hasSize(4)))
                    .andExpect(jsonPath("$.httpStatus", is("NOT_FOUND")))
                    .andExpect(jsonPath("$.timestamp").exists())
                    .andExpect(jsonPath("$.message", is("No BusinessUnit with this id exists")))
                    .andExpect(jsonPath("$.errors", nullValue()));
        }

        @Test
        @Transactional
        void postReturns_201_IfValidItemIsPosted() throws Exception {
            String token = doLogin();

            BusinessUnitDto businessUnitDto = new BusinessUnitDto("Insert Me");
            String jsonToPost = objectMapper.writeValueAsString(businessUnitDto);

            mockMvc.perform(
                            post("/businessUnits")
                                    .header("Authorization", "Bearer " + token)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(jsonToPost)
                                    .characterEncoding(Charset.defaultCharset())
                    )
                    .andDo(print())
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.*", hasSize(2)))
                    .andExpect(jsonPath("$.name", is(businessUnitDto.getName())))
                    .andExpect(jsonPath("$._links", hasKey("self")))
                    .andExpect(jsonPath("$._links", aMapWithSize(1)))
                    .andExpect(jsonPath("$._links.self.href", endsWith("/businessUnits/3")));
        }

        @Test
        @Transactional
        void postWithBlankNameReturns_400() throws Exception {
            String token = doLogin();

            BusinessUnitDto businessUnitDto = new BusinessUnitDto("");
            String jsonToPost = objectMapper.writeValueAsString(businessUnitDto);

            mockMvc.perform(
                            post("/businessUnits")
                                    .header("Authorization", "Bearer " + token)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(jsonToPost)
                                    .characterEncoding(Charset.defaultCharset())
                    )
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.*", hasSize(4)))
                    .andExpect(jsonPath("$.httpStatus", is("BAD_REQUEST")))
                    .andExpect(jsonPath("$.timestamp").exists())
                    .andExpect(jsonPath("$.message", is("Invalid JSON")))
                    .andExpect(jsonPath("$.errors.length()", is(1)))
                    .andExpect(jsonPath("$.errors[0].object", is("businessUnitDto")))
                    .andExpect(jsonPath("$.errors[0].field", is("name")))
                    .andExpect(jsonPath("$.errors[0].rejectedValue", is("")))
                    .andExpect(jsonPath("$.errors[0].message", is("must not be blank")));
        }

        @Test
        @Transactional
        void validPutShouldReturn_200_Ok() throws Exception {
            String token = doLogin();

            BusinessUnitDto businessUnitDto = new BusinessUnitDto("Lala");
            String jsonToPost = objectMapper.writeValueAsString(businessUnitDto);

            mockMvc.perform(
                            put("/businessUnits/1")
                                    .header("Authorization", "Bearer " + token)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(jsonToPost)
                                    .characterEncoding(Charset.defaultCharset())
                    )
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.*", hasSize(2)))
                    .andExpect(jsonPath("$.name", is(businessUnitDto.getName())))
                    .andExpect(jsonPath("$._links", aMapWithSize(3)))
                    .andExpect(jsonPath("$._links", hasKey("self")))
                    .andExpect(jsonPath("$._links.self.href", endsWith("/businessUnits/1")))
                    .andExpect(jsonPath("$._links", hasKey("businessUnitObjectives")))
                    .andExpect(jsonPath("$._links", hasKey("users")));
        }

        @Test
        @Transactional
        void putOnInvalidIdShouldReturn_404() throws Exception {
            String token = doLogin();

            BusinessUnitDto businessUnitDto = new BusinessUnitDto("Lala");
            String jsonToPost = objectMapper.writeValueAsString(businessUnitDto);

            mockMvc.perform(
                            put("/businessUnits/9999")
                                    .header("Authorization", "Bearer " + token)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(jsonToPost)
                                    .characterEncoding(Charset.defaultCharset())
                    )
                    .andDo(print())
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.*", hasSize(4)))
                    .andExpect(jsonPath("$.httpStatus", is("NOT_FOUND")))
                    .andExpect(jsonPath("$.timestamp").exists())
                    .andExpect(jsonPath("$.message", is("No BusinessUnit with this id exists")))
                    .andExpect(jsonPath("$.errors", nullValue()));
        }

        @Test
        @Transactional
        void deleteOnInvalidIdShouldReturn_404() throws Exception {
            String token = doLogin();

            mockMvc.perform(
                            delete("/businessUnits/9999")
                                    .header("Authorization", "Bearer " + token)
                    )
                    .andDo(print())
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.*", hasSize(4)))
                    .andExpect(jsonPath("$.httpStatus", is("NOT_FOUND")))
                    .andExpect(jsonPath("$.timestamp").exists())
                    .andExpect(jsonPath("$.message", is("No BusinessUnit with this id exists")))
                    .andExpect(jsonPath("$.errors", nullValue()));
        }

        @Test
        @Transactional
        void deleteOnValidIdShouldReturn_204() throws Exception {
            String token = doLogin();

            mockMvc.perform(
                            delete("/businessUnits/1")
                                    .header("Authorization", "Bearer " + token)
                    )
                    .andDo(print())
                    .andExpect(status().isNoContent())
                    .andExpect(jsonPath("$").doesNotExist());
        }
    }
}
