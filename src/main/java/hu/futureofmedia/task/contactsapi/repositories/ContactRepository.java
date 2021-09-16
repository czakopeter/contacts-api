package hu.futureofmedia.task.contactsapi.repositories;

import hu.futureofmedia.task.contactsapi.dto.ContactMinInfo;
import hu.futureofmedia.task.contactsapi.entities.Contact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ContactRepository extends JpaRepository<Contact, Long> {

    @Query("select new hu.futureofmedia.task.contactsapi.dto.ContactMinInfo(" +
            "c.id, concat(c.firstName, ' ', c.lastName), c.email, c.phoneNumber, c.company.name) from Contact c")
    Page<ContactMinInfo> getAllContactOrderByFullNameWithPagination(Pageable pageable);
}
