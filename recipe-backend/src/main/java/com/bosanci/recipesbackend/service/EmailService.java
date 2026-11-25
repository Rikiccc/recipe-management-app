package com.bosanci.recipesbackend.service;

import com.bosanci.recipesbackend.config.AppProperties;
import com.bosanci.recipesbackend.model.User;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    private final ObjectProvider<JavaMailSender> mailSender;
    private final AppProperties properties;

    public void sendActivation(User user, String token) {
        String link = frontend("/auth/activate?token=" + token);
        send(user, "Aktivacija naloga",
                "Zdravo " + user.getUsername() + ",\n\n"
                        + "Hvala što ste se registrovali. Da biste aktivirali nalog, otvorite link:\n\n"
                        + link + "\n\n"
                        + "Link važi 24 sata. Ako se niste registrovali, ignorišite ovu poruku.");
    }

    public void sendPasswordReset(User user, String token) {
        String link = frontend("/auth/reset-password?token=" + token);
        send(user, "Resetovanje lozinke",
                "Zdravo " + user.getUsername() + ",\n\n"
                        + "Primili smo zahtev za resetovanje lozinke. Novu lozinku možete postaviti ovde:\n\n"
                        + link + "\n\n"
                        + "Link važi 1 sat. Ako niste tražili reset, ignorišite ovu poruku, lozinka ostaje ista.");
    }

    private void send(User user, String subject, String text) {
        JavaMailSender sender = mailSender.getIfAvailable();
        if (sender == null) {
            log.warn("SMTP nije podešen (spring.mail.host). Poruka '{}' za {} NIJE poslata. Sadržaj:\n{}",
                    subject, user.getEmail(), text);
            return;
        }
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(properties.mail().from());
            message.setTo(user.getEmail());
            message.setSubject(subject);
            message.setText(text);
            sender.send(message);
        } catch (MailException e) {
            log.error("Slanje poruke '{}' na {} nije uspelo", subject, user.getEmail(), e);
        }
    }

    private String frontend(String path) {
        String base = properties.frontendUrl();
        return (base.endsWith("/") ? base.substring(0, base.length() - 1) : base) + path;
    }
}