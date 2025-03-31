package com.drinkeg.drinkeg.domain.member.service;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendVerificationEmail(String toEmail, String link) {
        String subject = "[Drinkeg] 이메일 인증을 완료해주세요!";

        sendMail(toEmail, subject);
    }

    private void sendMail(String to, String subject) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();

            message.setTo(to);
            message.setSubject(subject);
            mailSender.send(message);

        } catch (MailException e) {
            throw new RuntimeException("이메일 전송에 실패했습니다", e);
        }
    }

}
