package com.ojt.StudentsBoot.controller;

import com.ojt.StudentsBoot.model.Email;
import com.ojt.StudentsBoot.service.EmailService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class EmailController {

     private final EmailService emailService;


    // Sending a simple Email
    @GetMapping("/sendMail")
    public ResponseEntity<String>
    sendMail()
    {
        emailService.sendEmail(
                "pudgeismyfavourate@gmail.com",
                "Hello",
                "This is a test email"
        );
        return ResponseEntity.ok("Email sent successfully");
    }


}
