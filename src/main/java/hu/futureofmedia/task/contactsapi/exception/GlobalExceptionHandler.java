package hu.futureofmedia.task.contactsapi.exception;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;
import java.util.stream.Collectors;


@RestControllerAdvice
@Hidden
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public List<ErrorMessage> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException e) {
        return e.getFieldErrors().stream()
                .map(fieldError -> new ErrorMessage(fieldError.getField(), fieldError.getDefaultMessage()))
                .collect(Collectors.toList());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public List<ErrorMessage> handle(
            MethodArgumentTypeMismatchException e) {
        ErrorMessage message = new ErrorMessage(null, e.getMessage());
        return List.of(message);
    }

    @ExceptionHandler(IdNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public List<ErrorMessage> handleIdNotFoundException(
            IdNotFoundException e) {
        ErrorMessage message = new ErrorMessage(null, String.format("not found class %s entity with id %d", e.getClassType().getSimpleName(), e.getId()));
        return List.of(message);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public List<ErrorMessage> handleIllegalArgumentException(
            IllegalArgumentException e) {
        ErrorMessage message = new ErrorMessage(null, e.getMessage());
        return List.of(message);
    }
}
