package edu.dei.examination.cmsexm.service;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import edu.dei.examination.cmsexm.model.PasswordResetToken;
import edu.dei.examination.cmsexm.model.User;
import edu.dei.examination.cmsexm.repository.PasswordResetTokenRepository;
import edu.dei.examination.cmsexm.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PasswordService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PasswordResetTokenRepository tokenRepo;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // =========================
    // 📧 SEND RESET LINK
    // =========================
    public void sendResetLink(String email) {

        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token = UUID.randomUUID().toString();

        PasswordResetToken prt = new PasswordResetToken();
        prt.setToken(token);
        prt.setUser(user);
        prt.setExpiryDate(LocalDateTime.now().plusMinutes(30));

        tokenRepo.save(prt);

        String link = "http://localhost:4200/auth/reset-password?token=" + token;

        sendEmail(email, link);
    }

    // =========================
    // 🔐 RESET PASSWORD
    // =========================
    public void resetPassword(String token, String newPassword) {

        PasswordResetToken prt = tokenRepo.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid token"));

        if (prt.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token expired");
        }

        User user = prt.getUser();

        user.setPassword(passwordEncoder.encode(newPassword));
       

        userRepo.save(user);

        // 🔥 delete token after use
        tokenRepo.delete(prt);
    }

    // =========================
    // 📧 EMAIL
    // =========================
    private void sendEmail(String to, String link) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(to);
        message.setSubject("Reset Your Password");

        message.setText(
                "Dear User,\n\n" +
                "Click the link below to reset your password:\n\n" +
                link + "\n\n" +
                "This link will expire in 30 minutes.\n\n" +
                "Regards,\nAdmin"
        );

        mailSender.send(message);
    }
}