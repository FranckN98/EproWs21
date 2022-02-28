package de.thbingen.epro;

import de.thbingen.epro.model.dto.BusinessUnitKeyResultDto;
import de.thbingen.epro.model.dto.BusinessUnitKeyResultHistoryDto;
import de.thbingen.epro.model.dto.BusinessUnitKeyResultPostDto;
import de.thbingen.epro.util.UserLogin;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.LinkRelation;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class BusinessUnitKeyResultIntegrationTest extends IntegrationBase {

    @Nested
    class TestCasesWithAdminAccount {

        @Test
        @Transactional
        void newKeyResultShouldHaveOneHistoryElementAfterUpdatingItOnce() throws Exception {
            String token = doLogin(UserLogin.CO_ADMIN);

            BusinessUnitKeyResultPostDto inserted = new BusinessUnitKeyResultPostDto(
                    "Created",
                    0f,
                    100f,
                    100f,
                    "create"
            );
            String jsonToPost = objectMapper.writeValueAsString(inserted);

            // post new KR
            MvcResult mvcResult = mockMvc.perform(
                            post("/businessUnitObjectives/2/keyResults")
                                    .header("Authorization", "Bearer " + token)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(jsonToPost)
                                    .characterEncoding(Charset.defaultCharset())
                    )
                    .andExpect(status().isCreated()).andReturn();

            Pattern idAtEndOfUrlPattern = Pattern.compile("(\\d+)$");
            String locationHeader = mvcResult.getResponse().getHeader("Location");
            assertNotNull(locationHeader);
            Matcher matcher = idAtEndOfUrlPattern.matcher(locationHeader);
            Long id = null;
            if (matcher.find()) {
                id = Long.parseLong(matcher.group(1));
            }

            // make sure there is currently no history for KR
            mockMvc.perform(
                            get("/businessUnitKeyResults/" + id + "/history")
                                    .header("Authorization", "Bearer " + token)
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$._embedded").doesNotExist());

            // update KR

            BusinessUnitKeyResultDto updateKeyResult = new BusinessUnitKeyResultDto();
            updateKeyResult.setName("New Name");
            updateKeyResult.setComment("Change name");
            jsonToPost = objectMapper.writeValueAsString(updateKeyResult);

            mockMvc.perform(
                            put("/businessUnitKeyResults/" + id)
                                    .header("Authorization", "Bearer " + token)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(jsonToPost)
                                    .characterEncoding(Charset.defaultCharset())
                    )
                    .andExpect(status().isOk());

            LinkRelation bukrhCollectionRelation = annotationLinkRelationProvider.getCollectionResourceRelFor(BusinessUnitKeyResultHistoryDto.class);

            // make sure there is history now and the history object contains the values of the first inserted
            mockMvc.perform(
                            get("/businessUnitKeyResults/" + id + "/history")
                                    .header("Authorization", "Bearer " + token)
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$._embedded." + bukrhCollectionRelation).exists())
                    .andExpect(jsonPath("$._embedded." + bukrhCollectionRelation + ".length()", is(1)))
                    .andExpect(jsonPath("$._embedded." + bukrhCollectionRelation + "[0].historicalBusinessUnitKeyResult.name", is(inserted.getName())))
                    .andExpect(jsonPath("$._embedded." + bukrhCollectionRelation + "[0].historicalBusinessUnitKeyResult.comment", is(inserted.getComment())))
                    .andExpect(jsonPath("$._embedded." + bukrhCollectionRelation + "[0]._links.businessUnitKeyResult.href", endsWith("/businessUnitKeyResults/" + id)));
        }

        @Test
        @Disabled("This test can't be transactional, thus must be started in isolation, so that it does not interfere with other tests")
        void achievementShouldChangeAfterUpdatingCurrentValue() throws Exception {
            String token = doLogin(UserLogin.CO_ADMIN);

            BusinessUnitKeyResultPostDto inserted = new BusinessUnitKeyResultPostDto(
                    "Created",
                    0f,
                    100f,
                    100f,
                    "create"
            );
            String jsonToPost = objectMapper.writeValueAsString(inserted);

            // post new KR
            MvcResult mvcResult = mockMvc.perform(
                            post("/businessUnitObjectives/2/keyResults")
                                    .header("Authorization", "Bearer " + token)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(jsonToPost)
                                    .characterEncoding(Charset.defaultCharset())
                    )
                    .andExpect(status().isCreated()).andReturn();

            Pattern idAtEndOfUrlPattern = Pattern.compile("(\\d+)$");
            String locationHeader = mvcResult.getResponse().getHeader("Location");
            assertNotNull(locationHeader);
            Matcher matcher = idAtEndOfUrlPattern.matcher(locationHeader);
            Long id = null;
            if (matcher.find()) {
                id = Long.parseLong(matcher.group(1));
            }

            // update KR

            BusinessUnitKeyResultDto updateKeyResult = new BusinessUnitKeyResultDto();
            updateKeyResult.setCurrentValue(50f);
            updateKeyResult.setComment("Wow, we apparently did some Work");
            jsonToPost = objectMapper.writeValueAsString(updateKeyResult);

            mockMvc.perform(
                            put("/businessUnitKeyResults/" + id)
                                    .header("Authorization", "Bearer " + token)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(jsonToPost)
                                    .characterEncoding(Charset.defaultCharset())
                    )
                    .andExpect(status().isOk());

            mockMvc.perform(
                            get("/businessUnitKeyResults/" + id)
                                    .header("Authorization", "Bearer " + token)
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.achievement", is(0.5)));
        }

        @Test
        @Disabled("This test can't be transactional, thus must be started in isolation, so that it does not interfere with other tests")
        void businessUnitObjectiveAchievementShouldChangeDependingOnItsBusinessUnitKeyResults() throws Exception {
            String token = doLogin(UserLogin.CO_ADMIN);

            mockMvc.perform(
                            get("/businessUnitObjectives/2")
                                    .header("Authorization", "Bearer " + token)
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.achievement", is(0.0)));

            BusinessUnitKeyResultPostDto inserted = new BusinessUnitKeyResultPostDto(
                    "Created",
                    50f,
                    100f,
                    100f,
                    "create"
            );
            String jsonToPost = objectMapper.writeValueAsString(inserted);

            // post new KR
            mockMvc.perform(
                            post("/businessUnitObjectives/2/keyResults")
                                    .header("Authorization", "Bearer " + token)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(jsonToPost)
                                    .characterEncoding(Charset.defaultCharset())
                    )
                    .andExpect(status().isCreated());

            mockMvc.perform(
                            get("/businessUnitObjectives/2")
                                    .header("Authorization", "Bearer " + token)
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.achievement", is(0.5)));
        }
    }
}
