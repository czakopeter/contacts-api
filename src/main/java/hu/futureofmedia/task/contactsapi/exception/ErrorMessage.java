package hu.futureofmedia.task.contactsapi.exception;

import java.util.Objects;

public class ErrorMessage {

    private final String field;
    private final String errorMessage;

    public ErrorMessage(String field, String errorMessage) {
        this.field = field;
        this.errorMessage = errorMessage;
    }

    public String getField() {
        return field;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ErrorMessage that = (ErrorMessage) o;
        return Objects.equals(field, that.field) && Objects.equals(errorMessage, that.errorMessage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(field, errorMessage);
    }

    @Override
    public String toString() {
        return "ExceptionMessage{" +
                "field='" + field + '\'' +
                ", errorMessage='" + errorMessage + '\'' +
                '}';
    }
}
