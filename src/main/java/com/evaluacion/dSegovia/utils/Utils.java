package com.evaluacion.dSegovia.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;
@Component
public class Utils {

    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";

    public static boolean isValidEmail(String email) {
        return Pattern.compile(EMAIL_REGEX).matcher(email).matches();
    }

    public static String hashPassword(String password) {

        int strength = 12;
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(strength);
        return passwordEncoder.encode(password);
    }

    @Value("${password.regex}")
    private String passwordRegex;

    public  boolean isValidPassword(String password) {
        return Pattern.compile(passwordRegex).matcher(password).matches();
    }
}
