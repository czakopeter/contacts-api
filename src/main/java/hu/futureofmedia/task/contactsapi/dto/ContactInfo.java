package hu.futureofmedia.task.contactsapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.Objects;

public class ContactInfo {

    @Schema(example = "1")
    private Long id;

    @Schema(example = "John")
    private String firstName;

    @Schema(example = "Doe")
    private String lastName;

    @Schema(example = "john.doe@gmail.com")
    private String email;

    @Schema(example = "36201234567")
    private String phoneNumber;

    @Schema(example = "Company #1")
    private String companyName;

    @Schema(example = "Some comment for contact")
    private String comment;
    private LocalDateTime createdDate;
    private LocalDateTime lastUpdatedDate;

    public ContactInfo() {}

    public ContactInfo(Long id, String firstName, String lastName, String email, String phoneNumber, String companyName, String comment, LocalDateTime createdDate, LocalDateTime lastUpdatedDate) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.companyName = companyName;
        this.comment = comment;
        this.createdDate = createdDate;
        this.lastUpdatedDate = lastUpdatedDate;
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

    public String getCompanyName() {
        return companyName;
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

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public LocalDateTime getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ContactInfo that = (ContactInfo) o;
        return id.equals(that.id) && firstName.equals(that.firstName) && lastName.equals(that.lastName) && email.equals(that.email) && Objects.equals(phoneNumber, that.phoneNumber) && companyName.equals(that.companyName) && comment.equals(that.comment) && createdDate.equals(that.createdDate) && lastUpdatedDate.equals(that.lastUpdatedDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, email, phoneNumber, companyName, comment, createdDate, lastUpdatedDate);
    }

    @Override
    public String toString() {
        return "ContactInfo{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", companyName='" + companyName + '\'' +
                ", comment='" + comment + '\'' +
                ", createdDate=" + createdDate +
                ", lastUpdatedDate=" + lastUpdatedDate +
                '}';
    }
}
