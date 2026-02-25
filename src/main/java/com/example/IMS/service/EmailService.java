package com.example.IMS.service;

import com.example.IMS.dto.EmailRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${mail.from.email}")
    private String fromEmail;

    @Value("${mail.from.name}")
    private String fromName;

    /**
     * Send a simple plain text email
     */
    public void sendSimpleEmail(String to, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            
            mailSender.send(message);
            System.out.println("Email sent successfully to: " + to);
        } catch (Exception e) {
            System.err.println("Error sending email: " + e.getMessage());
            throw new RuntimeException("Failed to send email", e);
        }
    }

    /**
     * Send an HTML email
     */
    public void sendHtmlEmail(String to, String subject, String htmlBody) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            
            helper.setFrom(fromEmail, fromName);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody, true); // true indicates HTML
            
            mailSender.send(mimeMessage);
            System.out.println("HTML Email sent successfully to: " + to);
        } catch (MessagingException e) {
            System.err.println("Error sending HTML email: " + e.getMessage());
            throw new RuntimeException("Failed to send HTML email", e);
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            throw new RuntimeException("Failed to send HTML email", e);
        }
    }

    /**
     * Send email using EmailRequest DTO
     */
    public void sendEmail(EmailRequest emailRequest) {
        if (emailRequest.isHtml()) {
            sendHtmlEmail(emailRequest.getTo(), emailRequest.getSubject(), emailRequest.getBody());
        } else {
            sendSimpleEmail(emailRequest.getTo(), emailRequest.getSubject(), emailRequest.getBody());
        }
    }

    /**
     * Send welcome email to new users
     */
    public void sendWelcomeEmail(String to, String userName, String userType) {
        String subject = "Welcome to FlowTrack - " + userType + " Account Created";
        String body = String.format(
            "<html><body>" +
            "<h2>Welcome to FlowTrack!</h2>" +
            "<p>Dear %s,</p>" +
            "<p>Your %s account has been successfully created.</p>" +
            "<p>You can now log in and start using our inventory management system.</p>" +
            "<br>" +
            "<p>Best regards,<br>FlowTrack Team</p>" +
            "</body></html>",
            userName, userType
        );
        
        sendHtmlEmail(to, subject, body);
    }

    /**
     * Send payment confirmation email
     */
    public void sendPaymentConfirmationEmail(String to, String userName, double amount, String transactionId) {
        String subject = "Payment Confirmation - FlowTrack";
        String body = String.format(
            "<html><body>" +
            "<h2>Payment Confirmation</h2>" +
            "<p>Dear %s,</p>" +
            "<p>Your payment has been successfully processed.</p>" +
            "<p><strong>Transaction Details:</strong></p>" +
            "<ul>" +
            "<li>Amount: ₹%.2f</li>" +
            "<li>Transaction ID: %s</li>" +
            "<li>Date: %s</li>" +
            "</ul>" +
            "<p>Thank you for your payment!</p>" +
            "<br>" +
            "<p>Best regards,<br>FlowTrack Team</p>" +
            "</body></html>",
            userName, amount, transactionId, new java.util.Date().toString()
        );
        
        sendHtmlEmail(to, subject, body);
    }

    /**
     * Send OTP email for verification
     */
    public void sendOtpEmail(String to, String otp) {
        String subject = "Your FlowTrack OTP Code";
        String body = String.format(
            "<html><body>" +
            "<h2>Email Verification</h2>" +
            "<p>Your OTP code is: <strong style='font-size: 24px; color: #007bff;'>%s</strong></p>" +
            "<p>This OTP is valid for 10 minutes.</p>" +
            "<p>If you didn't request this, please ignore this email.</p>" +
            "<br>" +
            "<p>Best regards,<br>FlowTrack Team</p>" +
            "</body></html>",
            otp
        );
        
        sendHtmlEmail(to, subject, body);
    }
}
