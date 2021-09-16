package hu.futureofmedia.task.contactsapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import hu.futureofmedia.task.contactsapi.dto.ContactCreateUpdateCommand;
import hu.futureofmedia.task.contactsapi.dto.ContactInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ContactControllerIT {

    private final String contactBaseUrl = "/api/v1/contacts";

    @Autowired
    MockMvc mockMvc;

    ObjectMapper objectMapper = new ObjectMapper();

    ContactCreateUpdateCommand johnDoeCreate, janeDoeCreate, jackDoeCreate, jillDoeUpdate;

    @BeforeEach
    void setUp() {
        initObjectMapper();
        initContactCommand();
    }

    @Test
    void testCreateContact_saveJohnDoe() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post(contactBaseUrl)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(johnDoeCreate)))
                .andExpect(status().isCreated())
                .andReturn();

        ContactInfo johnDoeInfo = getContactInfo(mvcResult);
        assertEquals("John", johnDoeInfo.getFirstName());
        assertEquals("Doe", johnDoeInfo.getLastName());
        assertEquals("john.doe@gmail.com", johnDoeInfo.getEmail());
        assertEquals("36201234567", johnDoeInfo.getPhoneNumber());
        assertEquals("Company #1", johnDoeInfo.getCompanyName());
        assertEquals("John Doe comment", johnDoeInfo.getComment());
        assertNotNull(johnDoeInfo.getCreatedDate());
        assertNotNull(johnDoeInfo.getLastUpdatedDate());
        assertTrue(johnDoeInfo.getCreatedDate().isEqual(johnDoeInfo.getLastUpdatedDate()));
    }

    @Test
    void testCreateContact_saveJohnDoeWithInvalidCompanyId_IdNotFoundException() throws Exception {
        johnDoeCreate.setCompanyId(100L);

        mockMvc.perform(post(contactBaseUrl)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(johnDoeCreate)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$[0].field", nullValue()))
                .andExpect(jsonPath("$[0].errorMessage", equalTo("not found class Company entity with id 100")));
    }

    @Test
    void testGetAllContacts_emptyRepo() throws Exception {
        mockMvc.perform(get(contactBaseUrl))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(0)))
                .andExpect(jsonPath("$.empty", is(true)));
    }

    @Test
    void testGetAllContacts_TwoSavedInRepo_Returned() throws Exception {
        createContact(johnDoeCreate);
        createContact(janeDoeCreate);

        mockMvc.perform(get(contactBaseUrl))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pageable.pageNumber", is(0)))
                .andExpect(jsonPath("$.pageable.pageSize", is(10)))
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].fullName", equalTo("Jane Doe")))
                .andExpect(jsonPath("$.content[1].fullName", equalTo("John Doe")));
    }

    @Test
    void testGetAllContacts_SavedThirteenNumberedJohnDoe_ReturnedPageZeroAndOne() throws Exception {
        for(int i = 1; i <= 13; i++) {
            johnDoeCreate.setFirstName(String.format("John%02d", i));
            createContact(johnDoeCreate);
        }

        mockMvc.perform(get(contactBaseUrl))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.number", is(0)))
                .andExpect(jsonPath("$.totalPages", is(2)))
                .andExpect(jsonPath("$.totalElements", is(13)))
                .andExpect(jsonPath("$.totalPages", is(2)))
                .andExpect(jsonPath("$.content", hasSize(10)))
                .andExpect(jsonPath("$.content[0].fullName", equalTo("John01 Doe")))
                .andExpect(jsonPath("$.content[1].fullName", equalTo("John02 Doe")))
                .andExpect(jsonPath("$.content[2].fullName", equalTo("John03 Doe")))
                .andExpect(jsonPath("$.content[3].fullName", equalTo("John04 Doe")))
                .andExpect(jsonPath("$.content[4].fullName", equalTo("John05 Doe")))
                .andExpect(jsonPath("$.content[5].fullName", equalTo("John06 Doe")))
                .andExpect(jsonPath("$.content[6].fullName", equalTo("John07 Doe")))
                .andExpect(jsonPath("$.content[7].fullName", equalTo("John08 Doe")))
                .andExpect(jsonPath("$.content[8].fullName", equalTo("John09 Doe")))
                .andExpect(jsonPath("$.content[9].fullName", equalTo("John10 Doe")));

        mockMvc.perform(get(contactBaseUrl + "?page=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.number", is(1)))
                .andExpect(jsonPath("$.totalPages", is(2)))
                .andExpect(jsonPath("$.totalElements", is(13)))
                .andExpect(jsonPath("$.totalPages", is(2)))
                .andExpect(jsonPath("$.content", hasSize(3)))
                .andExpect(jsonPath("$.content[0].fullName", equalTo("John11 Doe")))
                .andExpect(jsonPath("$.content[1].fullName", equalTo("John12 Doe")))
                .andExpect(jsonPath("$.content[2].fullName", equalTo("John13 Doe")));
    }

    @Test
    void testGetAllContacts_NegativePage() throws Exception {
        mockMvc.perform(get(contactBaseUrl + "?page=-1"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].field", nullValue()))
                .andExpect(jsonPath("$[0].errorMessage", equalTo("Page index must not be less than zero!")));
    }

    @Test
    void testGetContact_GetJaneDoeById() throws Exception {
        ContactInfo janeDoeInfo = createContact(janeDoeCreate);

        MvcResult mvcResult = mockMvc.perform(get(contactBaseUrl + "/" + janeDoeInfo.getId()))
                .andExpect(status().isOk())
                .andReturn();

        janeDoeInfo = getContactInfo(mvcResult);
        assertEquals("Jane", janeDoeInfo.getFirstName());
        assertEquals("Doe", janeDoeInfo.getLastName());
        assertEquals("jane.doe@gmail.com", janeDoeInfo.getEmail());
        assertNull(janeDoeInfo.getPhoneNumber());
        assertEquals("Company #2", janeDoeInfo.getCompanyName());
        assertEquals("Jane Doe comment", janeDoeInfo.getComment());
        assertNotNull(janeDoeInfo.getCreatedDate());
        assertNotNull(janeDoeInfo.getLastUpdatedDate());
        assertTrue(janeDoeInfo.getCreatedDate().isEqual(janeDoeInfo.getLastUpdatedDate()));
    }

    @Test
    void testGetContact_IdNotExistsOrAlreadyDeleted_IdNotFoundException() throws Exception {
        mockMvc.perform(get(contactBaseUrl + "/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$[0].field", nullValue()))
                .andExpect(jsonPath("$[0].errorMessage", equalTo("not found class Contact entity with id 1")));
    }

    @Test
    void testUpdateContact_SavedJaneUpdateToJill() throws Exception {
        ContactInfo janeDoeInfo = createContact(janeDoeCreate);

        MvcResult mvcResult = mockMvc.perform(put(contactBaseUrl + "/" + janeDoeInfo.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(jillDoeUpdate)))
                .andExpect(status().isOk())
                .andReturn();

        ContactInfo jillDoeInfo = getContactInfo(mvcResult);
        assertEquals("Jill", jillDoeInfo.getFirstName());
        assertEquals("Doe", jillDoeInfo.getLastName());
        assertEquals("jill.doe@gmail.com", jillDoeInfo.getEmail());
        assertEquals("36207654321", jillDoeInfo.getPhoneNumber());
        assertEquals("Company #3", jillDoeInfo.getCompanyName());
        assertEquals("Updated comment for Jill Doe", jillDoeInfo.getComment());
        assertNotNull(jillDoeInfo.getCreatedDate());
        assertNotNull(jillDoeInfo.getLastUpdatedDate());
        assertTrue(jillDoeInfo.getCreatedDate().isBefore(jillDoeInfo.getLastUpdatedDate()));
    }

    @Test
    void testUpdateContact_IdNotExistsOrAlreadyDeleted_IdNotFoundException() throws Exception {
        mockMvc.perform(put(contactBaseUrl + "/1")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(jillDoeUpdate)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$[0].field", nullValue()))
                .andExpect(jsonPath("$[0].errorMessage", equalTo("not found class Contact entity with id 1")));
    }

    @Test
    void testDeleteContact() throws Exception {
        ContactInfo janeDoeInfo = createContact(janeDoeCreate);

        mockMvc.perform(delete(contactBaseUrl + "/" + janeDoeInfo.getId()))
                .andExpect(status().isOk());

        mockMvc.perform(get(contactBaseUrl + "/" + janeDoeInfo.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteContact_IdNotExistsOrAlreadyDeleted_IdNotFoundException() throws Exception {
        mockMvc.perform(delete(contactBaseUrl + "/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$[0].field", nullValue()))
                .andExpect(jsonPath("$[0].errorMessage", equalTo("not found class Contact entity with id 1")));
    }

    private void initObjectMapper() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    private void initContactCommand() {
        johnDoeCreate = new ContactCreateUpdateCommand(
                "John", "Doe", "john.doe@gmail.com", "36201234567", 1L, "John Doe comment");

        janeDoeCreate = new ContactCreateUpdateCommand(
                "Jane", "Doe", "jane.doe@gmail.com", null, 2L, "Jane Doe comment");

        jackDoeCreate = new ContactCreateUpdateCommand(
                "Jack", "Doe", "jack.doe@gmail.com", null, 2L, "");

        jillDoeUpdate = new ContactCreateUpdateCommand(
                "Jill", "Doe", "jill.doe@gmail.com", "36207654321", 3L, "Updated comment for Jill Doe");
    }

    private ContactInfo createContact(ContactCreateUpdateCommand command) throws Exception {
        MvcResult mvcResult = mockMvc.perform(post(contactBaseUrl)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isCreated())
                .andReturn();
        return getContactInfo(mvcResult);
    }

    private ContactInfo getContactInfo(MvcResult mvcResult) throws Exception {
        String responseContent = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        return objectMapper.readValue(responseContent, ContactInfo.class);
    }
}