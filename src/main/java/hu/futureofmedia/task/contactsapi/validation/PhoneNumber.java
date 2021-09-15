package hu.futureofmedia.task.contactsapi.validation;

import com.google.i18n.phonenumbers.PhoneNumberUtil;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = PhoneValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PhoneNumber {

    String message() default "must suit the requirements of E.164 format";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
