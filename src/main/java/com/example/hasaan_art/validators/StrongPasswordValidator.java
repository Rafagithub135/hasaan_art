//package com.example.hasaan_art.validators;
//
//import jakarta.validation.ConstraintValidator;
//import jakarta.validation.ConstraintValidatorContext;
//
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//public class StrongPasswordValidator implements ConstraintValidator<StrongPassword, String> {
//
//    @Override
//    public boolean isValid(String password, ConstraintValidatorContext context) {
//        // The password must be at least 8 characters long, must contain at least one uppercase letter,
//        // one lowercase letter, one number, one digit, and one special character
//
//        if (password == null) {
//            return false;
//        }
//
//        Pattern pattern = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$");
//        Matcher matcher = pattern.matcher(password);
//
//        return matcher.matches();
//    }
//}