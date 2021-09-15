package hu.futureofmedia.task.contactsapi.dto;

import hu.futureofmedia.task.contactsapi.validation.PhoneNumber;

import javax.validation.constraints.NotBlank;

public class ContactUpdateCommand {

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    private String email;

    @PhoneNumber
    private String phoneNumber;

    private Long companyId;

    private String comment;

    public ContactUpdateCommand() {}

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

    public Long getCompanyId() {
        return companyId;
    }

    public String getComment() {
        return comment;
    }
}
