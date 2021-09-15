package hu.futureofmedia.task.contactsapi.exception;

public class IdNotFoundException extends RuntimeException {

    private final Long id;
    private final Class<?> classType;

    public IdNotFoundException(Long id, Class<?> classType) {
        this.id = id;
        this.classType = classType;
    }

    public Long getId() {
        return id;
    }

    public Class<?> getClassType() {
        return classType;
    }
}
