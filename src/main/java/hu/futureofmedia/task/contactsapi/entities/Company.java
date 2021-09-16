package hu.futureofmedia.task.contactsapi.entities;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Company {
    @Id
    private Long id;
    private String name;

    public Company() {}

    public Company(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
