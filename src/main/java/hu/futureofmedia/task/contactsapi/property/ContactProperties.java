package hu.futureofmedia.task.contactsapi.property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Min;

@Validated
@ConstructorBinding
@ConfigurationProperties(prefix = "contact")
public class ContactProperties {

    @Min(1)
    private final int pageSize;

    public ContactProperties(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageSize() {
        return pageSize;
    }

}
