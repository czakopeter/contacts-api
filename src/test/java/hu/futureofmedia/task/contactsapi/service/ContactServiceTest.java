package hu.futureofmedia.task.contactsapi.service;

import hu.futureofmedia.task.contactsapi.dto.ContactCreateUpdateCommand;
import hu.futureofmedia.task.contactsapi.dto.ContactInfo;
import hu.futureofmedia.task.contactsapi.dto.ContactMinInfo;
import hu.futureofmedia.task.contactsapi.entities.Company;
import hu.futureofmedia.task.contactsapi.entities.Contact;
import hu.futureofmedia.task.contactsapi.exception.IdNotFoundException;
import hu.futureofmedia.task.contactsapi.property.ContactProperties;
import hu.futureofmedia.task.contactsapi.repositories.ContactRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContactServiceTest {

    @InjectMocks
    ContactService contactService;

    @Mock
    ContactRepository contactRepository;

    @Mock
    CompanyService companyService;

    @Spy
    ContactProperties contactProperties = new ContactProperties(10);

    ContactCreateUpdateCommand johnDoeCreate, jillDoeUpdate;
    Contact johnDoeToSave, johnDoeSaved, jillDoeToUpdate, jillDoeUpdated;
    ContactInfo johnDoeInfo, jillDoeUpdatedInfo;

    Company companyOne;

    @BeforeEach
    void setUp() {
        initCompany();
        initContactCommand();

    }

    @Test
    void testCreateContact_CompanyIdNotExists_IdNodFoundException() {
        when(companyService.findCompanyById(1L)).thenThrow(new IdNotFoundException(1L, Company.class));

        assertThrows(IdNotFoundException.class, () -> contactService.createContact(johnDoeCreate));

        verify(companyService).findCompanyById(1L);

        verifyNoInteractions(contactRepository);
        verifyNoMoreInteractions(companyService);
        verifyNoInteractions(contactProperties);
    }

    @Test
    void testCreateContact_SuccessfulCreating() {
        when(companyService.findCompanyById(1L)).thenReturn(companyOne);
        when(contactRepository.save(johnDoeToSave)).thenReturn(johnDoeSaved);

        assertEquals(johnDoeInfo, contactService.createContact(johnDoeCreate));

        verify(companyService).findCompanyById(1L);
        verify(contactRepository).save(johnDoeToSave);

        verifyNoMoreInteractions(contactRepository);
        verifyNoMoreInteractions(companyService);
        verifyNoInteractions(contactProperties);
    }

    @Test
    void testCreateContact_FirstNameHasHeadingAndTrailingWhitespace_FirstNameTrimmed() {
        johnDoeCreate.setFirstName("  " + johnDoeCreate.getFirstName() + "  ");
        when(companyService.findCompanyById(1L)).thenReturn(companyOne);
        when(contactRepository.save(johnDoeToSave)).thenReturn(johnDoeSaved);

        assertEquals(johnDoeInfo ,contactService.createContact(johnDoeCreate));

        verify(companyService).findCompanyById(1L);
        verify(contactRepository).save(johnDoeToSave);

        verifyNoMoreInteractions(contactRepository);
        verifyNoMoreInteractions(companyService);
        verifyNoInteractions(contactProperties);
    }

    @Test
    void testCreateContact_LastNameHasHeadingAndTrailingWhitespace_LastNameTrimmed() {
        johnDoeCreate.setLastName("  " + johnDoeCreate.getLastName() + "  ");
        when(companyService.findCompanyById(1L)).thenReturn(companyOne);
        when(contactRepository.save(johnDoeToSave)).thenReturn(johnDoeSaved);

        assertEquals(johnDoeInfo ,contactService.createContact(johnDoeCreate));

        verify(companyService).findCompanyById(1L);
        verify(contactRepository).save(johnDoeToSave);

        verifyNoMoreInteractions(contactRepository);
        verifyNoMoreInteractions(companyService);
        verifyNoInteractions(contactProperties);
    }

    @Test
    void testCreateContact_CommentHasHeadingAndTrailingWhitespace_CommentNameTrimmed() {
        johnDoeCreate.setComment("  " + johnDoeCreate.getComment() + "  ");
        when(companyService.findCompanyById(1L)).thenReturn(companyOne);
        when(contactRepository.save(johnDoeToSave)).thenReturn(johnDoeSaved);

        assertEquals(johnDoeInfo ,contactService.createContact(johnDoeCreate));

        verify(companyService).findCompanyById(1L);
        verify(contactRepository).save(johnDoeToSave);

        verifyNoMoreInteractions(contactRepository);
        verifyNoMoreInteractions(companyService);
        verifyNoInteractions(contactProperties);
    }

    @Test
    void testGetAllContacts_emptyRepo() {
        Sort sort = Sort.by("firstName", "lastName").ascending();
        Pageable pageable = PageRequest.of(0, 10, sort);
        Page<ContactMinInfo> contactPage = new PageImpl<>(List.of(), pageable, 0);
        when(contactProperties.getPageSize()).thenReturn(10);
        when(contactRepository.getAllContactOrderByFullNameWithPagination(pageable)).thenReturn(contactPage);

        Page<ContactMinInfo> contactInfoPage = contactService.getAllContacts(0);
        assertTrue(contactInfoPage.isEmpty());
        assertEquals(contactInfoPage.getSort(), sort);
        assertTrue(contactInfoPage.getContent().isEmpty());

        verify(contactProperties).getPageSize();
        verify(contactRepository).getAllContactOrderByFullNameWithPagination(pageable);

        verifyNoMoreInteractions(contactRepository);
        verifyNoInteractions(companyService);
        verifyNoMoreInteractions(contactProperties);
    }

    @Test
    void testGetContactById_GetIdOne_ReturnJohnDoe() {
        when(contactRepository.findById(1L)).thenReturn(Optional.of(johnDoeSaved));

        assertEquals(johnDoeInfo, contactService.getContact(1L));

        verify(contactRepository).findById(1L);

        verifyNoMoreInteractions(contactRepository);
        verifyNoInteractions(companyService);
        verifyNoInteractions(contactProperties);
    }

    @Test
    void testGetContactById_IdNotExistsOrAlreadyDeleted_IdNotFoundException() {
        when(contactRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IdNotFoundException.class, () -> contactService.getContact(1L));

        verify(contactRepository).findById(1L);

        verifyNoMoreInteractions(contactRepository);
        verifyNoInteractions(companyService);
        verifyNoInteractions(contactProperties);
    }

    @Test
    void testUpdateContact() {
        when(contactRepository.findById(1L)).thenReturn(Optional.of(johnDoeSaved));
        when(companyService.findCompanyById(1L)).thenReturn(companyOne);
        when(contactRepository.saveAndFlush(jillDoeToUpdate)).thenReturn(jillDoeUpdated);

        assertEquals(jillDoeUpdatedInfo, contactService.updateContact(1L, jillDoeUpdate));

        verify(contactRepository).findById(1L);
        verify(contactRepository).saveAndFlush(jillDoeToUpdate);

        verifyNoMoreInteractions(contactRepository);
        verifyNoMoreInteractions(companyService);
        verifyNoInteractions(contactProperties);
    }

    @Test
    void testUpdateContact_IdNotExistsOrAlreadyDeleted_IdNotFoundException() {
        when(contactRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IdNotFoundException.class, () -> contactService.updateContact(1L, any()));

        verify(contactRepository).findById(1L);

        verifyNoMoreInteractions(contactRepository);
        verifyNoInteractions(companyService);
        verifyNoInteractions(contactProperties);
    }

    @Test
    void testDeleteContact() {
        when(contactRepository.findById(1L)).thenReturn(Optional.of(johnDoeSaved));
        doNothing().when(contactRepository).delete(johnDoeSaved);

        contactService.deleteContact(1L);

        verify(contactRepository).findById(1L);
        verify(contactRepository).delete(johnDoeSaved);

        verifyNoMoreInteractions(contactRepository);
        verifyNoInteractions(companyService);
        verifyNoInteractions(contactProperties);
    }

    @Test
    void testDeleteContact_IdNotExistsOrAlreadyDeleted_IdNotFoundException() {
        when(contactRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IdNotFoundException.class, () -> contactService.deleteContact(1L));

        verify(contactRepository).findById(1L);

        verifyNoMoreInteractions(contactRepository);
        verifyNoInteractions(companyService);
        verifyNoInteractions(contactProperties);
    }

    private void initContactCommand() {
        LocalDateTime now = LocalDateTime.now();

        johnDoeCreate = new ContactCreateUpdateCommand(
                "John", "Doe", "john.doe@gmail.com", "36201234567", 1L, "John Doe comment");

        johnDoeToSave = new Contact("John", "Doe", "john.doe@gmail.com", "36201234567", companyOne, "John Doe comment", Contact.Status.ACTIVE);

        johnDoeSaved = new Contact(1L, "John", "Doe", "john.doe@gmail.com", "36201234567", companyOne, "John Doe comment", Contact.Status.ACTIVE, now, now);
        johnDoeInfo = new ContactInfo(1L, "John", "Doe", "john.doe@gmail.com", "36201234567", companyOne.getName(), "John Doe comment", now, now);

        jillDoeUpdate = new ContactCreateUpdateCommand(
                "Jill", "Doe", "jill.doe@gmail.com", "36207654321", 1L, "Updated comment for Jill Doe");

        jillDoeToUpdate = new Contact(1L, "Jill", "Doe", "jill.doe@gmail.com", "36207654321",  companyOne, "Updated comment for Jill Doe", Contact.Status.ACTIVE, now, now);
        jillDoeUpdated = new Contact(1L, "Jill", "Doe", "jill.doe@gmail.com", "36207654321",  companyOne, "Updated comment for Jill Doe", Contact.Status.ACTIVE, now, now.plusYears(1));
        jillDoeUpdatedInfo = new ContactInfo(1L, "Jill", "Doe", "jill.doe@gmail.com", "36207654321",  companyOne.getName(), "Updated comment for Jill Doe", now, now.plusYears(1));

    }

    private void initCompany() {
        companyOne = new Company(1L, "Company #1");
    }
}