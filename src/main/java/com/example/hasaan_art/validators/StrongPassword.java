//package com.example.hasaan_art.validators;
//
//import jakarta.validation.Constraint;
//import jakarta.validation.Payload;
//
//import java.lang.annotation.ElementType;
//import java.lang.annotation.Retention;
//import java.lang.annotation.RetentionPolicy;
//import java.lang.annotation.Target;
//
//@Target({ElementType.FIELD, ElementType.PARAMETER})
//@Retention(RetentionPolicy.RUNTIME)
//@Constraint(validatedBy = StrongPasswordValidator.class)
//public @interface StrongPassword {
//    String message() default "Password must be at least 8 characters and must contain 1 lower case, 1 upper case, 1 digit and 1 special character.";
//
//    Class<?>[] groups() default {};
//
//    Class<? extends Payload>[] payload() default {};
//}