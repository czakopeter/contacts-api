package hu.futureofmedia.task.contactsapi.dto;

import hu.futureofmedia.task.contactsapi.validation.PhoneNumber;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.*;
import java.util.Objects;

public class ContactCreateUpdateCommand {

    @Schema(example = "John")
    @NotBlank
    private String firstName;

    @Schema(example = "Doe")
    @NotBlank
    private String lastName;

    @Schema(example = "john.doe@gmail.com")
    @NotEmpty
    @Email
    private String email;

    @Schema(example = "+36201234567")
    @Size(min = 1, message = "size must be greater than or equal to {min}")
    @PhoneNumber
    private String phoneNumber;

    @Schema(example = "1")
    @NotNull
    @Positive
    private Long companyId;

    @Schema(example = "Some comment for contact")
    @NotNull
    private String comment;

    public ContactCreateUpdateCommand(String firstName, String lastName, String email, String phoneNumber, Long companyId, String comment) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.companyId = companyId;
        this.comment = comment;
    }

    public String getFirstName() {
        return firstName;
    }

    public ContactCreateUpdateCommand setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public ContactCreateUpdateCommand setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public ContactCreateUpdateCommand setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public ContactCreateUpdateCommand setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public ContactCreateUpdateCommand setCompanyId(Long companyId) {
        this.companyId = companyId;
        return this;
    }

    public String getComment() {
        return comment;
    }

    public ContactCreateUpdateCommand setComment(String comment) {
        this.comment = comment;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ContactCreateUpdateCommand that = (ContactCreateUpdateCommand) o;
        return firstName.equals(that.firstName) && lastName.equals(that.lastName) && email.equals(that.email) && Objects.equals(phoneNumber, that.phoneNumber) && Objects.equals(companyId, that.companyId) && Objects.equals(comment, that.comment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, email, phoneNumber, companyId, comment);
    }

    @Override
    public String toString() {
        return "ContactCreateCommand{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phoneNumber + '\'' +
                ", companyId=" + companyId +
                ", comment='" + comment + '\'' +
                '}';
    }
}
