package com.broers.prueba_tecnica.email.service;

import com.broers.prueba_tecnica.utils.constants.EndpointsConstants;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.nio.charset.StandardCharsets;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailService implements EmailServiceInterface {

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${app.url}")
    private String appUrl;

    @Async
    public void sendEmail(String to, String subject, String templateName, Map<String, Object> templateModel) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());

            Context context = new Context();
            context.setVariables(templateModel);
            String html = templateEngine.process(templateName, context);

            helper.setTo(to);
            helper.setFrom(fromEmail);
            helper.setSubject(subject);
            helper.setText(html, true);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Error sending email to " + to, e);
        }
    }

    public void sendVerificationEmail(String to, String token) {
        String verificationUrl = appUrl + "/api/v1/school/verify-email?token=" + token;
        Map<String, Object> templateModel = Map.of(
                "name", to.split("@")[0],
                "verificationUrl", verificationUrl
        );
        sendEmail(to, "Verify your email address", "email-verification", templateModel);
    }

    public void sendPasswordResetEmail(String to, String token) {
        String resetUrl = appUrl + EndpointsConstants.ENDPOINT_RESET_FORM + "?token=" + token;
        Map<String, Object> templateModel = Map.of(
                "name", to.split("@")[0],
                "resetUrl", resetUrl
        );
        sendEmail(to, "Reset your password", "password-reset", templateModel);
    }
}
