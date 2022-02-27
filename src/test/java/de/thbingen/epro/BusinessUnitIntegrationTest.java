package de.thbingen.epro;


import de.thbingen.epro.model.dto.*;
import de.thbingen.epro.repository.BusinessUnitRepository;
import de.thbingen.epro.repository.OkrUserRepository;
import de.thbingen.epro.util.UserLogin;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.LinkRelation;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.Charset;
import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class BusinessUnitIntegrationTest extends IntegrationBase {

    @Autowired
    private BusinessUnitRepository businessUnitRepository;

    @Autowired
    private OkrUserRepository okrUserRepository;

    @Nested
    class TestCasesWithAdminAccount {

        @Test
        @Transactional
        void findAllReturnsAllPresentBusinessUnitsWithAllPossibleRelations() throws Exception {
            String token = doLogin(UserLogin.CO_ADMIN);

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
                    .andExpect(jsonPath("$._embedded." + expectedLinkRelation + "[0]._links.businessUnitObjectives.href", matchesRegex("http://localhost/businessUnits/1/objectives(\\{\\?start,end})?")))
                    .andExpect(jsonPath("$._embedded." + expectedLinkRelation + "[0]._links.users.href", matchesRegex("http://localhost/businessUnits/1/users")))
                    .andExpect(jsonPath("$._embedded." + expectedLinkRelation + "[0].name", is("Personal")))
                    .andExpect(jsonPath("$._embedded." + expectedLinkRelation + "[1]._links.self.href", endsWith("/businessUnits/2")))
                    .andExpect(jsonPath("$._embedded." + expectedLinkRelation + "[1]._links.users.href", matchesRegex("http://localhost/businessUnits/2/users")))
                    .andExpect(jsonPath("$._embedded." + expectedLinkRelation + "[1].name", is("IT")))
                    .andExpect(jsonPath("$._links").exists())
                    .andExpect(jsonPath("$._links.self.href", matchesRegex("http://localhost/businessUnits(\\?page=\\d+&size=\\d+)?")));
        }

        @Test
        @Transactional
        void findByIdReturnsOneIfIdIsValid() throws Exception {
            String token = doLogin(UserLogin.CO_ADMIN);

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
            String token = doLogin(UserLogin.CO_ADMIN);

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
        void postReturns_201_IfValidItemIsPostedAndGetByIdReturnsNewlyPostedItem() throws Exception {
            String token = doLogin(UserLogin.CO_ADMIN);

            BusinessUnitDto businessUnitDto = new BusinessUnitDto("Insert Me");
            String jsonToPost = objectMapper.writeValueAsString(businessUnitDto);

            MvcResult mvcResult = mockMvc.perform(
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
                    .andExpect(jsonPath("$._links.self.href", endsWith("/businessUnits/3")))
                    .andReturn();

            Pattern idAtEndOfUrlPattern = Pattern.compile("(\\d+)$");
            String locationHeader = mvcResult.getResponse().getHeader("Location");
            assertNotNull(locationHeader);
            Matcher matcher = idAtEndOfUrlPattern.matcher(locationHeader);
            Long id = null;
            if (matcher.find()) {
                id = Long.parseLong(matcher.group(1));
            }

            mockMvc.perform(
                            get("/businessUnits/" + id)
                                    .header("Authorization", "Bearer " + token)
                    )
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.*", hasSize(2)))
                    .andExpect(jsonPath("$.name", is(businessUnitDto.getName())))
                    .andExpect(jsonPath("$._links", hasKey("self")))
                    .andExpect(jsonPath("$._links", aMapWithSize(1)))
                    .andExpect(jsonPath("$._links.self.href", endsWith("/businessUnits/3")));
        }

        @Test
        @Transactional
        void postWithBlankNameReturns_400() throws Exception {
            String token = doLogin(UserLogin.CO_ADMIN);

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
        void validPutShouldReturn_200_OkAndItemShouldBeUpdated() throws Exception {
            String token = doLogin(UserLogin.CO_ADMIN);

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


            mockMvc.perform(
                            get("/businessUnits/1")
                                    .header("Authorization", "Bearer " + token)
                    )
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.*", hasSize(2)))
                    .andExpect(jsonPath("$.name", is(businessUnitDto.getName())))
                    .andExpect(jsonPath("$._links", hasKey("self")))
                    .andExpect(jsonPath("$._links", aMapWithSize(3)))
                    .andExpect(jsonPath("$._links.self.href", endsWith("/businessUnits/1")));
        }

        @Test
        @Transactional
        void putOnInvalidIdShouldReturn_404() throws Exception {
            String token = doLogin(UserLogin.CO_ADMIN);

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
            String token = doLogin(UserLogin.CO_ADMIN);

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
            String token = doLogin(UserLogin.CO_ADMIN);

            mockMvc.perform(
                            delete("/businessUnits/1")
                                    .header("Authorization", "Bearer " + token)
                    )
                    .andDo(print())
                    .andExpect(status().isNoContent())
                    .andExpect(jsonPath("$").doesNotExist());
        }

        @Test
        @Transactional
        void findBusinessObjectivesByBusinessUnitIdWithDefaultTimeFrameShouldReturnListOfObjectivesInCurrentYear() throws Exception {
            String token = doLogin(UserLogin.CO_ADMIN);

            LinkRelation buoCollectionRelation = annotationLinkRelationProvider.getCollectionResourceRelFor(BusinessUnitObjectiveDto.class);

            int currentYear = LocalDate.now().getYear();

            mockMvc.perform(
                            get("/businessUnits/1/objectives")
                                    .header("Authorization", "Bearer " + token)
                    ).andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.*", hasSize(3)))
                    .andExpect(jsonPath("$._links.self.href", matchesRegex("http://localhost/businessUnits/1/objectives(\\?page=0&size=10)?")))
                    .andExpect(jsonPath("$.page").exists())
                    .andExpect(jsonPath("$._embedded", aMapWithSize(1)))
                    .andExpect(jsonPath("$._embedded." + buoCollectionRelation + "..startDate", everyItem(matchesRegex(currentYear + "-\\d{2}-\\d{2}"))))
                    .andExpect(jsonPath("$._embedded." + buoCollectionRelation + "..endDate", everyItem(matchesRegex(currentYear + "-\\d{2}-\\d{2}"))));
        }

        @Test
        @Transactional
        void findBusinessObjectivesByBusinessUnitIdWithInvalidTimeFrameShouldReturnBadRequest() throws Exception {
            String token = doLogin(UserLogin.CO_ADMIN);

            mockMvc.perform(
                            get("/businessUnits/1/objectives?start=2030-01-01")
                                    .header("Authorization", "Bearer " + token)
                    ).andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.*", hasSize(4)))
                    .andExpect(jsonPath("$.httpStatus", is("BAD_REQUEST")))
                    .andExpect(jsonPath("$.timestamp").exists())
                    .andExpect(jsonPath("$.message", is("Invalid Date Range. Make sure the Start Date is before the End Date")))
                    .andExpect(jsonPath("$.errors", nullValue()));
        }

        @Test
        @Transactional
        void findBusinessObjectivesByBusinessUnitIdWithHugeTimeFrameShouldReturnAllObjectives() throws Exception {
            String token = doLogin(UserLogin.CO_ADMIN);

            LinkRelation buoCollectionRelation = annotationLinkRelationProvider.getCollectionResourceRelFor(BusinessUnitObjectiveDto.class);

            mockMvc.perform(
                            get("/businessUnits/1/objectives?start=1900-01-01&end=2999-12-31")
                                    .header("Authorization", "Bearer " + token)
                    ).andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.*", hasSize(3)))
                    .andExpect(jsonPath("$._links.self.href", matchesRegex("http://localhost/businessUnits/1/objectives(\\?.*)?")))
                    .andExpect(jsonPath("$.page").exists())
                    .andExpect(jsonPath("$._embedded", aMapWithSize(1)))
                    .andExpect(jsonPath("$._embedded." + buoCollectionRelation, hasSize(2)));
        }

        @Test
        @Transactional
        void findBusinessObjectivesByBusinessUnitIdWith_2021_AsTimeFrameShouldReturnAllObjectivesFrom_2021() throws Exception {
            String token = doLogin(UserLogin.CO_ADMIN);

            LinkRelation buoCollectionRelation = annotationLinkRelationProvider.getCollectionResourceRelFor(BusinessUnitObjectiveDto.class);

            mockMvc.perform(
                            get("/businessUnits/1/objectives?start=2021-01-01&end=2021-12-31")
                                    .header("Authorization", "Bearer " + token)
                    ).andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.*", hasSize(3)))
                    .andExpect(jsonPath("$._links.self.href", matchesRegex("http://localhost/businessUnits/1/objectives(\\?.*)?")))
                    .andExpect(jsonPath("$.page").exists())
                    .andExpect(jsonPath("$._embedded", aMapWithSize(1)))
                    .andExpect(jsonPath("$._embedded." + buoCollectionRelation + "..startDate", everyItem(matchesRegex("2021-\\d{2}-\\d{2}"))))
                    .andExpect(jsonPath("$._embedded." + buoCollectionRelation + "..endDate", everyItem(matchesRegex("2021-\\d{2}-\\d{2}"))));
        }

        @Test
        @Transactional
        void findBusinessObjectivesByBusinessUnitIdWith_1900_AsTimeFrameShouldReturnNoObjectives() throws Exception {
            String token = doLogin(UserLogin.CO_ADMIN);

            mockMvc.perform(
                            get("/businessUnits/1/objectives?start=1900-01-01&end=1900-12-31")
                                    .header("Authorization", "Bearer " + token)
                    ).andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.*", hasSize(2)))
                    .andExpect(jsonPath("$._links.self.href", matchesRegex("http://localhost/businessUnits/1/objectives(\\?.*)?")))
                    .andExpect(jsonPath("$.page").exists())
                    .andExpect(jsonPath("$._embedded").doesNotExist());
        }

        @Test
        @Transactional
        void postBusinessUnitObjectiveShouldReturn_201_AndObjectiveShouldBeCreated() throws Exception {
            String token = doLogin(UserLogin.CO_ADMIN);

            BusinessUnitObjectiveDto toPost = new BusinessUnitObjectiveDto(0f, "Post me!", LocalDate.now(), LocalDate.now().plusDays(1));
            String jsonToPost = objectMapper.writeValueAsString(toPost);

            MvcResult mvcResult = mockMvc.perform(
                            post("/businessUnits/1/objectives")
                                    .header("Authorization", "Bearer " + token)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(jsonToPost)
                                    .characterEncoding(Charset.defaultCharset())
                    )
                    .andDo(print())
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.*", hasSize(5)))
                    .andExpect(jsonPath("$.name", is(toPost.getName())))
                    .andExpect(jsonPath("$.achievement", is(0.0)))
                    .andExpect(jsonPath("$.startDate", matchesRegex("\\d{4}-\\d{2}-\\d{2}")))
                    .andExpect(jsonPath("$.endDate", matchesRegex("\\d{4}-\\d{2}-\\d{2}")))
                    .andExpect(jsonPath("$._links", hasKey("self")))
                    .andExpect(jsonPath("$._links", aMapWithSize(2)))
                    .andExpect(jsonPath("$._links.self.href", endsWith("/businessUnitObjectives/3")))
                    .andExpect(jsonPath("$._links.businessUnit.href", endsWith("/businessUnits/1")))
                    .andReturn();


            Pattern idAtEndOfUrlPattern = Pattern.compile("(\\d+)$");
            String locationHeader = mvcResult.getResponse().getHeader("Location");
            assertNotNull(locationHeader);
            Matcher matcher = idAtEndOfUrlPattern.matcher(locationHeader);
            Long id = null;
            if (matcher.find()) {
                id = Long.parseLong(matcher.group(1));
            }

            mockMvc.perform(
                            get("/businessUnitObjectives/" + id)
                                    .header("Authorization", "Bearer " + token)
                    )
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.*", hasSize(5)))
                    .andExpect(jsonPath("$.name", is(toPost.getName())))
                    .andExpect(jsonPath("$.achievement", is(0.0)))
                    .andExpect(jsonPath("$.startDate", matchesRegex("\\d{4}-\\d{2}-\\d{2}")))
                    .andExpect(jsonPath("$.endDate", matchesRegex("\\d{4}-\\d{2}-\\d{2}")))
                    .andExpect(jsonPath("$._links", hasKey("self")))
                    .andExpect(jsonPath("$._links", aMapWithSize(2)))
                    .andExpect(jsonPath("$._links.self.href", endsWith("/businessUnitObjectives/3")))
                    .andExpect(jsonPath("$._links.businessUnit.href", endsWith("/businessUnits/1")));
        }

        @Test
        @Transactional
        void postBusinessUnitObjectiveShouldReturn_400_OnMalformedJson() throws Exception {
            String token = doLogin(UserLogin.CO_ADMIN);

            mockMvc.perform(
                            post("/businessUnits/1/objectives")
                                    .header("Authorization", "Bearer " + token)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content("{\"name\": \"TestName\",startDate: \"2022-01-01\"}")
                                    .characterEncoding(Charset.defaultCharset())
                    )
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.*", hasSize(5)))
                    .andExpect(jsonPath("$.httpStatus", is("BAD_REQUEST")))
                    .andExpect(jsonPath("$.timestamp").exists())
                    .andExpect(jsonPath("$.message", is("Malformed JSON request")))
                    .andExpect(jsonPath("$.debugMessage").exists())
                    .andExpect(jsonPath("$.errors", nullValue()));
        }

        @Test
        @Transactional
        void postBusinessUnitObjectiveShouldReturn_404_OnInvalidBusinessUnitId() throws Exception {
            String token = doLogin(UserLogin.CO_ADMIN);

            BusinessUnitObjectiveDto toPost = new BusinessUnitObjectiveDto(0f, "Post me!", LocalDate.now(), LocalDate.now().plusDays(1));
            String jsonToPost = objectMapper.writeValueAsString(toPost);

            mockMvc.perform(
                            post("/businessUnits/9999/objectives")
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
                    .andExpect(jsonPath("$.message", is("No BusinessUnit with the given ID exists")))
                    .andExpect(jsonPath("$.errors", nullValue()));
        }

        @Test
        @Transactional
        void postBusinessUnitObjectiveShouldReturn_400_OnInvalidTimeFrame() throws Exception {
            String token = doLogin(UserLogin.CO_ADMIN);

            BusinessUnitObjectiveDto toPost = new BusinessUnitObjectiveDto(0f, "Post me!", LocalDate.now().plusDays(2), LocalDate.now().plusDays(1));
            String jsonToPost = objectMapper.writeValueAsString(toPost);

            mockMvc.perform(
                            post("/businessUnits/9999/objectives")
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
                    .andExpect(jsonPath("$.errors", hasSize(1)))
                    .andExpect(jsonPath("$.errors[0].object", is("businessUnitObjectiveDto")))
                    .andExpect(jsonPath("$.errors[0].field", is("endAfterBeginning")))
                    .andExpect(jsonPath("$.errors[0].rejectedValue", is(false)))
                    .andExpect(jsonPath("$.errors[0].message", is("The End date must be after the startDate")));
        }

        @Test
        @Transactional
        void postBusinessUnitObjectiveShouldReturn_400_OnEndDateInPast() throws Exception {
            String token = doLogin(UserLogin.CO_ADMIN);

            BusinessUnitObjectiveDto toPost = new BusinessUnitObjectiveDto(0f, "Post me!", LocalDate.now().minusYears(1), LocalDate.now().minusDays(1));
            String jsonToPost = objectMapper.writeValueAsString(toPost);

            mockMvc.perform(
                            post("/businessUnits/9999/objectives")
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
                    .andExpect(jsonPath("$.errors", hasSize(1)))
                    .andExpect(jsonPath("$.errors[0].object", is("businessUnitObjectiveDto")))
                    .andExpect(jsonPath("$.errors[0].field", is("endDate")))
                    .andExpect(jsonPath("$.errors[0].rejectedValue", matchesRegex("\\d{4}-\\d{2}-\\d{2}")))
                    .andExpect(jsonPath("$.errors[0].message", is("must be a future date")));
        }

        @Test
        @Transactional
        void getAllUsersByBusinessUnitShouldReturnAllUsers() throws Exception {
            String token = doLogin(UserLogin.CO_ADMIN);

            LinkRelation usersCollectionRelation = annotationLinkRelationProvider.getCollectionResourceRelFor(OkrUserDto.class);
            LinkRelation roleItemRelation = annotationLinkRelationProvider.getItemResourceRelFor(RoleDto.class);
            LinkRelation businessUnitItemRelation = annotationLinkRelationProvider.getItemResourceRelFor(BusinessUnitDto.class);

            mockMvc.perform(
                            get("/businessUnits/1/users")
                                    .header("Authorization", "Bearer " + token)
                    )
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.*", hasSize(3)))
                    .andExpect(jsonPath("$.page").exists())
                    .andExpect(jsonPath("$._links.self.href", matchesRegex("http://localhost/businessUnits/1/users(\\?page=0&size=10)?")))
                    .andExpect(jsonPath("$._embedded." + usersCollectionRelation + "[0]._links", aMapWithSize(3)))
                    .andExpect(jsonPath("$._embedded." + usersCollectionRelation + "[0]._links", hasKey("self")))
                    .andExpect(jsonPath("$._embedded." + usersCollectionRelation + "[0]._links", hasKey(roleItemRelation.toString())))
                    .andExpect(jsonPath("$._embedded." + usersCollectionRelation + "[0]._links", hasKey(businessUnitItemRelation.toString())));
        }

        @Test
        @Transactional
        void getAllUsersByBusinessUnitShouldReturn_404_IfBusinessUnitIdIsInvalid() throws Exception {
            String token = doLogin(UserLogin.CO_ADMIN);

            mockMvc.perform(
                            get("/businessUnits/9999/users")
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
        void postUserToBusinessUnitShouldReturn_201_IfValid() throws Exception {
            String token = doLogin(UserLogin.CO_ADMIN);

            String jsonToPost = objectMapper.writeValueAsString(
                    new OkrUserPostDto("Bob", "Belcher", "bob.belch1", "burgers")
            );

            mockMvc.perform(
                            post("/businessUnits/1/users")
                                    .header("Authorization", "Bearer " + token)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(jsonToPost)
                                    .characterEncoding(Charset.defaultCharset())
                    )
                    .andDo(print())
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$").exists());
        }

        @Test
        @Transactional
        void postUserToBusinessUnitShouldReturn_400_IfAnyValueIsBlank() throws Exception {
            String token = doLogin(UserLogin.CO_ADMIN);

            String jsonToPost = objectMapper.writeValueAsString(
                    new OkrUserPostDto("", "", "", "")
            );

            mockMvc.perform(
                            post("/businessUnits/1/users")
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
                    .andExpect(jsonPath("$.errors.length()", is(4)))
                    //name
                    .andExpect(jsonPath("$.errors..object", everyItem(is("okrUserPostDto"))))
                    .andExpect(jsonPath("$.errors..field", containsInAnyOrder("name", "username", "surname", "password")))
                    .andExpect(jsonPath("$.errors..rejectedValue", everyItem(is(""))))
                    .andExpect(jsonPath("$.errors..message", everyItem(is("must not be blank"))));
        }

        @Test
        @Transactional
        void postUserToBusinessUnitShouldReturn_404_IfBusinessUnitIdIsInvalid() throws Exception {
            String token = doLogin(UserLogin.CO_ADMIN);

            String jsonToPost = objectMapper.writeValueAsString(
                    new OkrUserPostDto("Tina", "Belcher", "dina.belch1", "jimmyPestoJunior")
            );

            mockMvc.perform(
                            post("/businessUnits/9999/users")
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
                    .andExpect(jsonPath("$.message", is("No BusinessUnit with the given ID exists")))
                    .andExpect(jsonPath("$.errors", nullValue()));
        }
    }

    @Nested
    class TestCasesWithReadOnlyAccount {

    }
}
