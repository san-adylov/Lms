package com.app.lms.validation.phoneNumber;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PhoneNumberValidator implements ConstraintValidator<PhoneNumberValidation,String> {

  @Override
  public boolean isValid(String phoneNumber, ConstraintValidatorContext constraintValidatorContext) {
    return phoneNumber.startsWith("+996") && phoneNumber.length()==13;
  }
}
