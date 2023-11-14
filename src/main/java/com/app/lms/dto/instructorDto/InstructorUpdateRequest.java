package com.app.lms.dto.instructorDto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import com.app.lms.validation.phoneNumber.PhoneNumberValidation;

public record InstructorUpdateRequest(@NotNull
                                      @NotBlank
                                      String firstName,
                                      @NotNull
                                      @NotBlank
                                      String lastName,
                                      @NotNull
                                      @NotBlank
                                      @PhoneNumberValidation
                                      String phoneNumber,
                                      @NotNull
                                      @NotBlank
                                      @Email
                                      String email,
                                      @NotNull
                                      @NotBlank
                                      String specialization) {
}
