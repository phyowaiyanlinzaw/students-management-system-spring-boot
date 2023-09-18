package com.ojt.StudentsBoot.controller;

import com.ojt.StudentsBoot.model.Email;
import com.ojt.StudentsBoot.service.EmailService;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@NoArgsConstructor
public class EmailController {

    @Autowired
     private EmailService emailService;


    // Sending a simple Email
    @PostMapping("/sendMail")
    public String
    sendMail(Email details)
    {
        details.setSubject("LEE");
        details.setMsgBody("LEE BODY");
        details.setRecipient("pudgeismyfavourate@gmail.com");
        return emailService.sendSimpleMail(details);
    }

    // Sending email with attachment
    @PostMapping("/sendMailWithAttachment")
    public String sendMailWithAttachment(
            @RequestBody Email details)
    {
        details.setRecipient("pudgeismyfavourate@gmail.com");
        return emailService.sendMailWithAttachment(details);
    }
}
