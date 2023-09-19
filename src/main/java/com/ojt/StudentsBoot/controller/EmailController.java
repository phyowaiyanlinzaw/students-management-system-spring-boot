package com.ojt.StudentsBoot.controller;

import com.ojt.StudentsBoot.model.Email;
import com.ojt.StudentsBoot.service.EmailService;
import com.ojt.StudentsBoot.service.UserService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
public class EmailController {

     private final EmailService emailService;
     private final UserService userService;


    // Sending a simple Email
    @PostMapping("/request-otp")
    public ResponseEntity<String> sendOtpEmail(@RequestParam("email") String email) {
        if (!userService.existsByEmail(email)) {
            System.out.println("Email not found");
            return ResponseEntity.badRequest().body("Email not found");
        }

        try {
            System.out.println("Sending email to: " + email);
            emailService.sendEmail(email);
            return ResponseEntity.ok("Email sent successfully");
        } catch (Exception e) {
            // Handle exceptions related to email sending
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error sending email: " + e.getMessage());
        }
    }



}
