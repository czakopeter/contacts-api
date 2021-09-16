package hu.futureofmedia.task.contactsapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import hu.futureofmedia.task.contactsapi.dto.ContactCreateUpdateCommand;
import hu.futureofmedia.task.contactsapi.dto.ContactInfo;
import hu.futureofmedia.task.contactsapi.exception.ErrorMessage;
import hu.futureofmedia.task.contactsapi.service.ContactService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(controllers = ContactController.class)
@AutoConfigureMockMvc
class ContactControllerTest {

    private final String contactBaseUrl = "/api/v1/contacts";

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ContactService contactService;

    ObjectMapper objectMapper;

    ContactCreateUpdateCommand johnDoeCreateCommand;
    ContactInfo johnDoeInfo;

    @BeforeEach
    void setUp() {
        initObjectMapper();
        initContact();
    }

    @Test
    void testContactCreate_successful() throws Exception {
        when(contactService.createContact(johnDoeCreateCommand)).thenReturn(johnDoeInfo);

        mockMvc.perform(post(contactBaseUrl)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(johnDoeCreateCommand)))
                .andExpect(status().isCreated())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(content().json(objectMapper.writeValueAsString(johnDoeInfo)));

        verify(contactService).createContact(johnDoeCreateCommand);
        verifyNoMoreInteractions(contactService);
    }

    @ParameterizedTest()
    @MethodSource("getInvalidFirstNameData")
    void testContactCreate_invalidFirstName(String firstName, List<ErrorMessage> expectedMessages, int expectedStatusCode) throws Exception {
        johnDoeCreateCommand.setFirstName(firstName);

        mockMvc.perform(post(contactBaseUrl)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(johnDoeCreateCommand)))
                .andExpect(status().is(expectedStatusCode))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedMessages)));

        Mockito.verifyNoInteractions(contactService);
    }

    @ParameterizedTest()
    @MethodSource("getInvalidLastNameData")
    void testContactCreate_invalidLastName(String lastName, List<ErrorMessage> expectedMessages, int expectedStatusCode) throws Exception {
        johnDoeCreateCommand.setLastName(lastName);

        mockMvc.perform(post(contactBaseUrl)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(johnDoeCreateCommand)))
                .andExpect(status().is(expectedStatusCode))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedMessages)));

        Mockito.verifyNoInteractions(contactService);
    }

    @ParameterizedTest()
    @MethodSource("getInvalidEmailData")
    void testContactCreate_invalidEmail(String email, List<ErrorMessage> expectedMessages, int expectedStatusCode) throws Exception {
        johnDoeCreateCommand.setEmail(email);

        mockMvc.perform(post(contactBaseUrl)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(johnDoeCreateCommand)))
                .andExpect(status().is(expectedStatusCode))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedMessages)));

        Mockito.verifyNoInteractions(contactService);
    }

    @ParameterizedTest()
    @MethodSource("getInvalidPhoneNumberData")
    void testContactCreate_invalidPhoneNumber(String phoneNumber, List<ErrorMessage> expectedMessages, int expectedStatusCode) throws Exception {
        johnDoeCreateCommand.setPhoneNumber(phoneNumber);

        mockMvc.perform(post(contactBaseUrl)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(johnDoeCreateCommand)))
                .andExpect(status().is(expectedStatusCode))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedMessages)));

        Mockito.verifyNoInteractions(contactService);
    }

    @ParameterizedTest()
    @MethodSource("getInvalidCompanyIdData")
    void testContactCreate_invalidCompanyId(Long companyId, List<ErrorMessage> expectedMessages, int expectedStatusCode) throws Exception {
        johnDoeCreateCommand.setCompanyId(companyId);

        mockMvc.perform(post(contactBaseUrl)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(johnDoeCreateCommand)))
                .andExpect(status().is(expectedStatusCode))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedMessages)));

        Mockito.verifyNoInteractions(contactService);
    }

    @ParameterizedTest()
    @MethodSource("getInvalidCommentData")
    void testContactCreate_invalidComment(String comment, List<ErrorMessage> expectedMessages, int expectedStatusCode) throws Exception {
        johnDoeCreateCommand.setComment(comment);

        mockMvc.perform(post(contactBaseUrl)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(johnDoeCreateCommand)))
                .andExpect(status().is(expectedStatusCode))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedMessages)));

        Mockito.verifyNoInteractions(contactService);
    }

    @ParameterizedTest()
    @MethodSource("getInvalidFirstNameData")
    void testContactUpdate_invalidFirstName(String firstName, List<ErrorMessage> expectedMessages, int expectedStatusCode) throws Exception {
        johnDoeCreateCommand.setFirstName(firstName);

        mockMvc.perform(put(contactBaseUrl + "/1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(johnDoeCreateCommand)))
                .andExpect(status().is(expectedStatusCode))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedMessages)));

        Mockito.verifyNoInteractions(contactService);
    }

    @ParameterizedTest()
    @MethodSource("getInvalidLastNameData")
    void testContactUpdate_invalidLastName(String lastName, List<ErrorMessage> expectedMessages, int expectedStatusCode) throws Exception {
        johnDoeCreateCommand.setLastName(lastName);

        mockMvc.perform(put(contactBaseUrl + "/1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(johnDoeCreateCommand)))
                .andExpect(status().is(expectedStatusCode))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedMessages)));

        Mockito.verifyNoInteractions(contactService);
    }

    @ParameterizedTest()
    @MethodSource("getInvalidEmailData")
    void testContactUpdate_invalidEmail(String email, List<ErrorMessage> expectedMessages, int expectedStatusCode) throws Exception {
        johnDoeCreateCommand.setEmail(email);

        mockMvc.perform(put(contactBaseUrl + "/1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(johnDoeCreateCommand)))
                .andExpect(status().is(expectedStatusCode))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedMessages)));

        Mockito.verifyNoInteractions(contactService);
    }

    @ParameterizedTest()
    @MethodSource("getInvalidPhoneNumberData")
    void testContactUpdate_invalidPhoneNumber(String phoneNumber, List<ErrorMessage> expectedMessages, int expectedStatusCode) throws Exception {
        johnDoeCreateCommand.setPhoneNumber(phoneNumber);

        mockMvc.perform(put(contactBaseUrl + "/1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(johnDoeCreateCommand)))
                .andExpect(status().is(expectedStatusCode))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedMessages)));

        Mockito.verifyNoInteractions(contactService);
    }

    @ParameterizedTest()
    @MethodSource("getInvalidCompanyIdData")
    void testContactUpdate_invalidCompanyId(Long companyId, List<ErrorMessage> expectedMessages, int expectedStatusCode) throws Exception {
        johnDoeCreateCommand.setCompanyId(companyId);

        mockMvc.perform(put(contactBaseUrl + "/1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(johnDoeCreateCommand)))
                .andExpect(status().is(expectedStatusCode))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedMessages)));

        Mockito.verifyNoInteractions(contactService);
    }

    @ParameterizedTest()
    @MethodSource("getInvalidCommentData")
    void testContactUpdate_invalidComment(String comment, List<ErrorMessage> expectedMessages, int expectedStatusCode) throws Exception {
        johnDoeCreateCommand.setComment(comment);

        mockMvc.perform(put(contactBaseUrl + "/1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(johnDoeCreateCommand)))
                .andExpect(status().is(expectedStatusCode))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedMessages)));

        Mockito.verifyNoInteractions(contactService);
    }

    @Test
    void testGetContact_UrlIdCanNotConvertToNumber() throws Exception {
        mockMvc.perform(get(contactBaseUrl + "/notNumber"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].field", nullValue()))
                .andExpect(jsonPath("$[0].errorMessage", equalTo(
                        "Failed to convert value of type 'java.lang.String' to required type 'java.lang.Long'; nested exception is java.lang.NumberFormatException: For input string: \"notNumber\"")));
    }

    private static Stream<Arguments> getInvalidFirstNameData() {
        ErrorMessage firstNameIsBlank = new ErrorMessage("firstName", "must not be blank");
        return Stream.of(
                Arguments.of(null, List.of(firstNameIsBlank), 400),
                Arguments.of("", List.of(firstNameIsBlank), 400),
                Arguments.of("   ", List.of(firstNameIsBlank), 400)
        );
    }


    private static Stream<Arguments> getInvalidLastNameData() {
        ErrorMessage lastNameIsBlank = new ErrorMessage("lastName", "must not be blank");
        return Stream.of(
                Arguments.of(null, List.of(lastNameIsBlank), 400),
                Arguments.of("", List.of(lastNameIsBlank), 400),
                Arguments.of("   ", List.of(lastNameIsBlank), 400)
        );
    }

    private static Stream<Arguments> getInvalidEmailData() {
        ErrorMessage emailIsEmpty = new ErrorMessage("email", "must not be empty");
        ErrorMessage emailIsNotWellFormed = new ErrorMessage("email", "must be a well-formed email address");
        return Stream.of(
                Arguments.of(null, List.of(emailIsEmpty), 400),
                Arguments.of("", List.of(emailIsEmpty), 400),
                Arguments.of("   ", List.of(emailIsNotWellFormed), 400),
                Arguments.of("testmail.com", List.of(emailIsNotWellFormed), 400),
                Arguments.of(" test@mail.com", List.of(emailIsNotWellFormed), 400),
                Arguments.of("test@mail.com ", List.of(emailIsNotWellFormed), 400),
                Arguments.of("@testmail.com", List.of(emailIsNotWellFormed), 400),
                Arguments.of("testmail.com@", List.of(emailIsNotWellFormed), 400),
                Arguments.of("test@@mail.com", List.of(emailIsNotWellFormed), 400)
        );
    }

    private static Stream<Arguments> getInvalidPhoneNumberData() {
        ErrorMessage phoneNumberIsNotGreaterThanOrEqualTo1 = new ErrorMessage("phoneNumber", "size must be greater than or equal to 1");
        ErrorMessage phoneNumberIsNotE164Formatted = new ErrorMessage("phoneNumber", "must suit the requirements of E.164 format");
        return Stream.of(
                Arguments.of("", List.of(phoneNumberIsNotGreaterThanOrEqualTo1), 400),
                Arguments.of("   ", List.of(phoneNumberIsNotE164Formatted), 400),
                Arguments.of("201234567", List.of(phoneNumberIsNotE164Formatted), 400),
                Arguments.of("06201234567", List.of(phoneNumberIsNotE164Formatted), 400),
                Arguments.of("+36201234567", List.of(phoneNumberIsNotE164Formatted), 400),
                Arguments.of("3620123456", List.of(phoneNumberIsNotE164Formatted), 400),
                Arguments.of("362012345678", List.of(phoneNumberIsNotE164Formatted), 400),
                Arguments.of(" 36201234567", List.of(phoneNumberIsNotE164Formatted), 400),
                Arguments.of("36201234567 ", List.of(phoneNumberIsNotE164Formatted), 400)
        );
    }

    private static Stream<Arguments> getInvalidCompanyIdData() {
        ErrorMessage companyIdIsNull = new ErrorMessage("companyId", "must not be null");
        ErrorMessage companyIdIsNotGreaterThan0 = new ErrorMessage("companyId", "must be greater than 0");
        return Stream.of(
                Arguments.of(null, List.of(companyIdIsNull), 400),
                Arguments.of(-1L, List.of(companyIdIsNotGreaterThan0), 400),
                Arguments.of(0L, List.of(companyIdIsNotGreaterThan0), 400)
        );
    }

    private static Stream<Arguments> getInvalidCommentData() {
        ErrorMessage commentIsNotGreaterThanOrEqualTo1 = new ErrorMessage("comment", "must not be null");
        return Stream.of(
                Arguments.of(null, List.of(commentIsNotGreaterThanOrEqualTo1), 400)
        );
    }

    private void initObjectMapper() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    private void initContact() {
        johnDoeCreateCommand = new ContactCreateUpdateCommand("John", "Doe", "john.doe@gmail.com", "36201234567", 1L, "");
        LocalDateTime createdAndUpdate = LocalDateTime.of(2021, Month.SEPTEMBER, 17, 10, 0);
        johnDoeInfo = new ContactInfo(1L, "John", "Doe", "john.doe@gmail.com", "36201234567", "Company #1", "", createdAndUpdate, createdAndUpdate);
    }
}