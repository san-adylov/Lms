package com.app.lms.validation.email;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.List;
import lombok.RequiredArgsConstructor;
import com.app.lms.entities.User;
import com.app.lms.repositories.UserRepository;

@RequiredArgsConstructor
public class EmailValidator implements ConstraintValidator<EmailValid,String> {

  private final UserRepository userRepository;

  @Override
  public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {

    List<User> users = userRepository.findAll();
    boolean isFalse = true;
    for (User u:users) {
      if (u.getEmail().equals(email)){
        isFalse = false;
      }
    }

    return isFalse;
  }
}
