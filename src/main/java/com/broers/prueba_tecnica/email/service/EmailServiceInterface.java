package com.broers.prueba_tecnica.email.service;

import java.util.Map;

public interface EmailServiceInterface {
    void sendEmail(String to, String subject, String templateName, Map<String, Object> templateModel);

    void sendVerificationEmail(String to, String token);

    void sendPasswordResetEmail(String to, String token);
}