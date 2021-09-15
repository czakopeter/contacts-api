package hu.futureofmedia.task.contactsapi.validation;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PhoneValidator implements ConstraintValidator<PhoneNumber, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(value == null || value.isEmpty()) {
            return true;
        }
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        try {
            String phoneNumberWithPlus = '+' + value;
            Phonenumber.PhoneNumber phoneNumber = phoneUtil.parse(phoneNumberWithPlus,null);
            return phoneUtil.isValidNumber(phoneNumber) &&
                    phoneNumberWithPlus.equals(phoneUtil.format(phoneNumber, PhoneNumberUtil.PhoneNumberFormat.E164));
        } catch (NumberParseException ignored) {}
        return false;
    }

}
