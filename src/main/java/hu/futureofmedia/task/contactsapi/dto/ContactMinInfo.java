package hu.futureofmedia.task.contactsapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Objects;

public class ContactMinInfo {

    @Schema(example = "1")
    private Long id;

    @Schema(example = "John Doe")
    private String fullName;

    @Schema(example = "john.doe@gmail.com")
    private String email;

    @Schema(example = "36201234567")
    private String phoneNumber;

    @Schema(example = "Company #1")
    private String companyName;

    public ContactMinInfo() {}

    public ContactMinInfo(Long id, String fullName, String email, String phoneNumber, String companyName) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.companyName = companyName;
    }

    public Long getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getCompanyName() {
        return companyName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ContactMinInfo that = (ContactMinInfo) o;
        return id.equals(that.id) && fullName.equals(that.fullName) && email.equals(that.email) && Objects.equals(phoneNumber, that.phoneNumber) && companyName.equals(that.companyName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fullName, email, phoneNumber, companyName);
    }

    @Override
    public String toString() {
        return "ContactMinInfo{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", companyName='" + companyName + '\'' +
                '}';
    }
}
