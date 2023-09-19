package com.ojt.StudentsBoot.service;

import com.ojt.StudentsBoot.model.Otp;
import com.ojt.StudentsBoot.model.User;
import com.ojt.StudentsBoot.repository.OtpRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final OtpRepository otpRepository;
    private final UserService userService;

    public void sendEmail(String to) {
        String generatedOtp = generateOtp();
        Otp otp = new Otp();
        otp.setOtp(generatedOtp);
        otp.setEmail(to);
        otpRepository.save(otp);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("OTP");
        message.setText("Your OTP is: " + generatedOtp);

        mailSender.send(message);
    }

    public String generateOtp() {
        // Generate a random number between 100000 (inclusive) and 999999 (exclusive)
        int otp = (int) (Math.random() * 900000) + 100000;
        return String.valueOf(otp);
    }
}
