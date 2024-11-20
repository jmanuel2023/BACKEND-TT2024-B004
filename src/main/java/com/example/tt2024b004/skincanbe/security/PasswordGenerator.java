package com.example.tt2024b004.skincanbe.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordGenerator {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        System.out.println("contra123 -> " + encoder.encode("contra123"));
        System.out.println("contra234 -> " + encoder.encode("contra234"));
        System.out.println("contra345 -> " + encoder.encode("contra345"));
        System.out.println("contra456  -> " + encoder.encode("contr456"));
    }
} 
