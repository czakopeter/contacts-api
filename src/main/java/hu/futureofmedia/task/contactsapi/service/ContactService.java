package hu.futureofmedia.task.contactsapi.service;

import hu.futureofmedia.task.contactsapi.dto.ContactCreateUpdateCommand;
import hu.futureofmedia.task.contactsapi.dto.ContactInfo;
import hu.futureofmedia.task.contactsapi.dto.ContactMinInfo;
import hu.futureofmedia.task.contactsapi.entities.Contact;
import hu.futureofmedia.task.contactsapi.exception.IdNotFoundException;
import hu.futureofmedia.task.contactsapi.property.ContactProperties;
import hu.futureofmedia.task.contactsapi.repositories.ContactRepository;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@EnableConfigurationProperties(ContactProperties.class)
public class ContactService {

    private final ContactRepository contactRepository;

    private final CompanyService companyService;

    private final ContactProperties contactProperties;

    public ContactService(
            ContactRepository contactRepository,
            CompanyService companyService,
            ContactProperties contactProperties) {
        this.contactRepository = contactRepository;
        this.companyService = companyService;
        this.contactProperties = contactProperties;
    }

    public ContactInfo createContact(ContactCreateUpdateCommand command) {
        Contact toSave = mapToContact(command);
        Contact saved = contactRepository.save(toSave);
        return mapToInfo(saved);
    }

    public Page<ContactMinInfo> getAllContacts(Integer page) {
        Pageable pageable = PageRequest.of(page, contactProperties.getPageSize(), Sort.by("firstName", "lastName").ascending());
        return contactRepository.getAllContactOrderByFullNameWithPagination(pageable);
    }

    public ContactInfo getContact(Long id) {
        return mapToInfo(findContactById(id));
    }

    public ContactInfo updateContact(Long id, ContactCreateUpdateCommand command) {
        Contact toUpdate = mapToContact(findContactById(id), command);
        Contact updated = contactRepository.saveAndFlush(toUpdate);
        return mapToInfo(updated);
    }

    public void deleteContact(Long id) {
        Contact toDelete = findContactById(id);
        contactRepository.delete(toDelete);
    }

    private Contact findContactById(Long id) {
        return contactRepository.findById(id)
                .orElseThrow(() -> new IdNotFoundException(id, Contact.class));
    }

    private Contact mapToContact(ContactCreateUpdateCommand command) {
        return new Contact(
                command.getFirstName().trim(),
                command.getLastName().trim(),
                command.getEmail(),
                command.getPhoneNumber(),
                companyService.findCompanyById(command.getCompanyId()),
                command.getComment().trim(),
                Contact.Status.ACTIVE
        );
    }

    private Contact mapToContact(Contact saved, ContactCreateUpdateCommand command) {
        return saved
                .setFirstName(command.getFirstName().trim())
                .setLastName(command.getLastName().trim())
                .setEmail(command.getEmail())
                .setPhoneNumber(command.getPhoneNumber())
                .setComment(command.getComment().trim())
                .setCompany(companyService.findCompanyById(command.getCompanyId()));
    }

    private ContactInfo mapToInfo(Contact contact) {
        return new ContactInfo(
                contact.getId(),
                contact.getFirstName(),
                contact.getLastName(),
                contact.getEmail(),
                contact.getPhoneNumber(),
                contact.getCompany().getName(),
                contact.getComment(),
                contact.getCreatedDate(),
                contact.getLastModifiedDate()
        );
    }

}
