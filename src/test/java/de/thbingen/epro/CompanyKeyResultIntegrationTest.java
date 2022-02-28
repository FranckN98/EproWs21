package de.thbingen.epro;

import de.thbingen.epro.model.dto.CompanyKeyResultDto;
import de.thbingen.epro.model.dto.CompanyKeyResultHistoryDto;
import de.thbingen.epro.model.dto.CompanyObjectiveDto;
import de.thbingen.epro.util.UserLogin;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.LinkRelation;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CompanyKeyResultIntegrationTest extends IntegrationBase {

    @Nested
    class TestCasesWithAdminAccount {

        @Test
        @Transactional
        void newKeyResultShouldHaveOneHistoryElementAfterUpdatingItOnce() throws Exception {
            String token = doLogin(UserLogin.CO_ADMIN);

            CompanyObjectiveDto companyObjectiveDto = new CompanyObjectiveDto(0f, "Test", LocalDate.now(), LocalDate.now().plusDays(10));
            String jsonToPost = objectMapper.writeValueAsString(companyObjectiveDto);

            MvcResult mvcResult = mockMvc.perform(
                            post("/companyObjectives")
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
            Long companyObjectiveId = null;
            if (matcher.find()) {
                companyObjectiveId = Long.parseLong(matcher.group(1));
            }

            CompanyKeyResultDto inserted = new CompanyKeyResultDto(
                    "Created",
                    0f,
                    100f,
                    100f,
                    0f,
                    "create",
                    OffsetDateTime.now()
            );
            jsonToPost = objectMapper.writeValueAsString(inserted);

            // post new KR
            mvcResult = mockMvc.perform(
                            post("/companyObjectives/" + companyObjectiveId + "/keyResults")
                                    .header("Authorization", "Bearer " + token)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(jsonToPost)
                                    .characterEncoding(Charset.defaultCharset())
                    )
                    .andExpect(status().isCreated()).andReturn();

            idAtEndOfUrlPattern = Pattern.compile("(\\d+)$");
            locationHeader = mvcResult.getResponse().getHeader("Location");
            assertNotNull(locationHeader);
            matcher = idAtEndOfUrlPattern.matcher(locationHeader);
            Long id = null;
            if (matcher.find()) {
                id = Long.parseLong(matcher.group(1));
            }

            // make sure there is currently no history for KR
            mockMvc.perform(
                            get("/companyKeyResults/" + id + "/history")
                                    .header("Authorization", "Bearer " + token)
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$._embedded").doesNotExist());

            // update KR

            CompanyKeyResultDto updateKeyResult = new CompanyKeyResultDto();
            updateKeyResult.setName("New Name");
            updateKeyResult.setComment("Change name");
            jsonToPost = objectMapper.writeValueAsString(updateKeyResult);

            mockMvc.perform(
                            put("/companyKeyResults/" + id)
                                    .header("Authorization", "Bearer " + token)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(jsonToPost)
                                    .characterEncoding(Charset.defaultCharset())
                    )
                    .andExpect(status().isOk());

            LinkRelation ckrhCollectionRelation = annotationLinkRelationProvider.getCollectionResourceRelFor(CompanyKeyResultHistoryDto.class);

            // make sure there is history now and the history object contains the values of the first inserted
            mockMvc.perform(
                            get("/companyKeyResults/" + id + "/history")
                                    .header("Authorization", "Bearer " + token)
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$._embedded." + ckrhCollectionRelation).exists())
                    .andExpect(jsonPath("$._embedded." + ckrhCollectionRelation + ".length()", is(1)))
                    .andExpect(jsonPath("$._embedded." + ckrhCollectionRelation + "[0].historicalCompanyKeyResult.name", is(inserted.getName())))
                    .andExpect(jsonPath("$._embedded." + ckrhCollectionRelation + "[0].historicalCompanyKeyResult.comment", is(inserted.getComment())))
                    .andExpect(jsonPath("$._embedded." + ckrhCollectionRelation + "[0]._links.companyKeyResult.href", endsWith("/companyKeyResults/" + id)));
        }

        @Test
        @Disabled("This test can't be transactional, thus must be started in isolation, so that it does not interfere with other tests")
        void achievementShouldChangeAfterUpdatingCurrentValue() throws Exception {
            String token = doLogin(UserLogin.CO_ADMIN);

            CompanyKeyResultDto inserted = new CompanyKeyResultDto(
                    "Created",
                    0f,
                    100f,
                    100f,
                    0f,
                    "create",
                    OffsetDateTime.now()
            );
            String jsonToPost = objectMapper.writeValueAsString(inserted);

            // post new KR
            MvcResult mvcResult = mockMvc.perform(
                            post("/companyObjectives/1/keyResults")
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

            CompanyKeyResultDto updateKeyResult = new CompanyKeyResultDto();
            updateKeyResult.setCurrentValue(50f);
            updateKeyResult.setComment("Wow, we apparently did some Work");
            jsonToPost = objectMapper.writeValueAsString(updateKeyResult);

            mockMvc.perform(
                            put("/companyKeyResults/" + id)
                                    .header("Authorization", "Bearer " + token)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(jsonToPost)
                                    .characterEncoding(Charset.defaultCharset())
                    )
                    .andExpect(status().isOk());

            Thread.sleep(2000);

            mockMvc.perform(
                            get("/companyKeyResults/" + id)
                                    .header("Authorization", "Bearer " + token)
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.achievement", is(0.5)));
        }

        @Test
        @Disabled("This test can't be transactional, thus must be started in isolation, so that it does not interfere with other tests")
        void companyObjectiveAchievementShouldChangeDependingOnItsBusinessUnitKeyResults() throws Exception {
            String token = doLogin(UserLogin.CO_ADMIN);

            mockMvc.perform(
                            get("/companyObjectives/1")
                                    .header("Authorization", "Bearer " + token)
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.achievement", is(0.1)));

            CompanyKeyResultDto inserted = new CompanyKeyResultDto(
                    "Created",
                    50f,
                    100f,
                    100f,
                    0f,
                    "create",
                    OffsetDateTime.now()
            );
            String jsonToPost = objectMapper.writeValueAsString(inserted);

            // post new KR
            mockMvc.perform(
                            post("/companyObjectives/1/keyResults")
                                    .header("Authorization", "Bearer " + token)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(jsonToPost)
                                    .characterEncoding(Charset.defaultCharset())
                    )
                    .andExpect(status().isCreated());

            mockMvc.perform(
                            get("/companyObjectives/1")
                                    .header("Authorization", "Bearer " + token)
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.achievement", is(0.3)));
        }
    }
}
