package hu.futureofmedia.task.contactsapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class ContactMinInfo {

    @Schema(example = "1")
    private Long id;

    @Schema(example = "John Doe")
    private String fullName;

    @Schema(example = "john.doe@gmail.com")
    private String email;

    @Schema(example = "+36201234567")
    private String phoneNumber;

    @Schema(example = "Company #1")
    private String companyName;

    public ContactMinInfo(Long id, String fullName, String companyName, String email, String phoneNumber) {
        this.id = id;
        this.fullName = fullName;
        this.companyName = companyName;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public Long getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
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
}
