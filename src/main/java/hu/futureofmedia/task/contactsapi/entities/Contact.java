package hu.futureofmedia.task.contactsapi.entities;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@EntityListeners(AuditingEntityListener.class)
@SQLDelete(sql = "UPDATE contact SET status = 'DELETED' WHERE id=?")
@Where(clause = "status = 'ACTIVE'")
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;

    @ManyToOne
    private Company company;

    private String comment;

    @Enumerated(EnumType.STRING)
    private Status status;

    @CreatedDate
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime lastModifiedDate;

    public Contact() {
    }

    public Contact(String firstName, String lastName, String email, String phoneNumber, Company company, String comment, Status status) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.company = company;
        this.comment = comment;
        this.status = status;
    }

    public Contact(Long id, String firstName, String lastName, String email, String phoneNumber, Company company, String comment, Status status, LocalDateTime createdDate, LocalDateTime lastModifiedDate) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.company = company;
        this.comment = comment;
        this.status = status;
        this.createdDate = createdDate;
        this.lastModifiedDate = lastModifiedDate;
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getComment() {
        return comment;
    }

    public Company getCompany() {
        return company;
    }

    public Status getStatus() {
        return status;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public LocalDateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    public Contact setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public Contact setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public Contact setEmail(String email) {
        this.email = email;
        return this;
    }

    public Contact setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public Contact setCompany(Company company) {
        this.company = company;
        return this;
    }

    public Contact setComment(String comment) {
        this.comment = comment;
        return this;
    }

    public Contact setStatus(Status status) {
        this.status = status;
        return this;
    }

    public enum Status {
        ACTIVE, DELETED
    }

    @PreRemove
    private void preRemove() {
        this.status = Status.DELETED;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Contact contact = (Contact) o;
        return Objects.equals(id, contact.id) && firstName.equals(contact.firstName) && lastName.equals(contact.lastName) && email.equals(contact.email) && Objects.equals(phoneNumber, contact.phoneNumber) && company.equals(contact.company) && comment.equals(contact.comment) && status == contact.status && Objects.equals(createdDate, contact.createdDate) && Objects.equals(lastModifiedDate, contact.lastModifiedDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, email, phoneNumber, company, comment, status, createdDate, lastModifiedDate);
    }

    @Override
    public String toString() {
        return "Contact{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", company=" + company +
                ", comment='" + comment + '\'' +
                ", status=" + status +
                ", createdDate=" + createdDate +
                ", lastModifiedDate=" + lastModifiedDate +
                '}';
    }
}