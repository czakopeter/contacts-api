package hu.futureofmedia.task.contactsapi.exception;

public class ExceptionMessage {

    private final String field;
    private final String errorMessage;

    public ExceptionMessage(String field, String errorMessage) {
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
    public String toString() {
        return "ExceptionMessage{" +
                "field='" + field + '\'' +
                ", errorMessage='" + errorMessage + '\'' +
                '}';
    }
}
