package com.TicketXChange.TicketXChange.auth.controller;



import com.TicketXChange.TicketXChange.auth.dtos.*;
import com.TicketXChange.TicketXChange.auth.exception.UserNotFoundException;
import com.TicketXChange.TicketXChange.auth.model.User;
import com.TicketXChange.TicketXChange.auth.repository.UserRepository;
import com.TicketXChange.TicketXChange.auth.service.AuthenticationService;
import com.TicketXChange.TicketXChange.auth.service.EmailService;
import com.TicketXChange.TicketXChange.auth.service.JwtService;
import com.TicketXChange.TicketXChange.auth.helper.EmailTemplate;
import com.TicketXChange.TicketXChange.auth.service.OTPService;
import io.jsonwebtoken.Claims;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * The `PasswordController` class handles password-related operations such as initiating password resets and resetting passwords.
 *
 * This controller provides endpoints for initiating password resets via email and resetting passwords using a reset token.
 */
@RestController
@RequestMapping("/api/v1/auth/password")
@RequiredArgsConstructor
public class PasswordController {
    private final EmailService emailService;
    private final UserRepository repository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final OTPService otpService;
    private final AuthenticationService authenticationService;

    @Value("${application.url.front-end.get.forgot-password}")
    private  String forgotPasswordUrl;



    /**
     * Initiate a password reset process for the specified email address.
     *
     * @param request The request containing the user's email address.
     * @return A response entity indicating the status of the password reset initiation.
     * @throws MessagingException If an error occurs while sending the email.
     */
    @PostMapping("/forgot-password")
    public ResponseEntity<ForgotPasswordResponse> initiatePasswordReset(@RequestBody ForgotPasswordRequest request) throws MessagingException {
        try {
            String email = request.getEmail();

            if (email == null || email.isEmpty()) {
                return ResponseEntity.badRequest().body(ForgotPasswordResponse.builder()
                        .success(false)
                        .error("Invalid Email")
                        .message("Email is required and cannot be empty.")
                        .build());
            }

            Optional<User> userOptional = repository.findByEmail(email);

            if (!userOptional.isPresent()) {
                // If the email is not registered, return a not found response
                throw new UserNotFoundException("User Not Found");
            }

            User user = userOptional.get();

            int otp = otpService.generateOTP(email);

            EmailTemplate template = new EmailTemplate("templates/emails/SendOtp.html");
            Map<String, String> replacements = new HashMap<>();
            replacements.put("user", email);
            replacements.put("otpnum", String.valueOf(otp));
            String message = template.getTemplate(replacements);

            // Call the email service to send the OTP via email and handle its response
            SendTemplateEmailMessageResponse emailResponse = emailService.sendTemplateEmailMessage(email, "OTP - Email Verification", message);
//            var resetToken = jwtService.generateResetToken(user);
//            String resetLink = forgotPasswordUrl + "?token=" + resetToken;
//            EmailTemplate template = new EmailTemplate("templates/emails/SendPasswordResetLink.html");
//            Map<String, String> replacements = new HashMap<>();
//            replacements.put("user", email);
//            replacements.put("resetLink", resetLink);
//            String message = template.getTemplate(replacements);
//            System.out.println(message);
//
//            // Call the email service to send the password reset link via email and handle its response
//            SendTemplateEmailMessageResponse emailResponse = emailService.sendTemplateEmailMessage(email, "Reset Link - TicketXChange.TicketXChange Inc", message);

            if (emailResponse.isSuccess()) {
                // If email is sent successfully, return a success response
                return ResponseEntity.ok(ForgotPasswordResponse.builder()
                        .success(true)
                        .message("Please check your email. Email is sent successfully.")
                        .email(email)
                        .build());
            } else {
                // If there was an error sending the email, return an error response
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ForgotPasswordResponse.builder()
                        .success(false)
                        .error("Email Send Error")
                        .email(email)
                        .message("An error occurred while sending the email.")
                        .build());
            }
        }  catch (UserNotFoundException e) {
            return ResponseEntity.badRequest().body(ForgotPasswordResponse.builder()
                    .success(false)
                    .error(e.getMessage())
                    .message("User Not Found")
                    .build());
        }
        catch (Exception e) {
            // Handle other exceptions
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ForgotPasswordResponse.builder()
                    .success(false)
                    .error(e.getMessage())
                    .message("An internal server error occurred.")
                    .build());
        }
    }

    /**
     * Reset a user's password using a valid reset token.
     *
     * @param jwtToken The reset token sent to the user's email.
     * @param request  The request containing the new password.
     * @return A response entity indicating the status of the password reset.
     */
    @PostMapping("/reset-password")
    public ResponseEntity<ResetPasswordResponse> resetPassword(@RequestParam("token") String jwtToken, @RequestBody PasswordResetRequest request) {
        try {
            Claims claims = jwtService.extractAllClaims(jwtToken);
            String resetToken = (String) claims.get("resetToken");

            // Check if the reset token is valid (e.g., not expired)
            if (!jwtService.isTokenExpired(resetToken)) {
                // Extract the user's username from the JWT token
                String username = jwtService.extractUsername(jwtToken);

                // Retrieve the user by username from your database
                Optional<User> optionalUser = repository.findByEmail(username);

                if (!optionalUser.isPresent()) {
                    throw new UserNotFoundException("User Not Found");
                }
                User user = optionalUser.get();
                if (!verifyPassword(user.getPasswordHash(), request.getExistingPassword())) {
                    return ResponseEntity.ok(ResetPasswordResponse.builder()
                            .success(false)
                            .error("existing password doesn't match")
                            .build());
                }

                // Encrypt and set the new password
                if(!authenticationService.validatePassword(request.getNewPassword())){
                    return ResponseEntity.ok(ResetPasswordResponse.builder()
                            .success(false)
                            .error("Password should contain at least 8 characters, including at least one uppercase letter, one lowercase letter, one number, and one special character")
                            .build());
                }
                if(!request.getNewPassword().equals(request.getNewPasswordConfirm())){
                    return ResponseEntity.ok(ResetPasswordResponse.builder()
                            .success(false)
                            .error("Password and Confirm Password does not match")
                            .build());
                }

                String newPassword = passwordEncoder.encode(request.getNewPassword());
                if (verifyPassword(user.getPasswordHash(), request.getNewPasswordConfirm())) {
                    return ResponseEntity.ok(ResetPasswordResponse.builder()
                            .success(false)
                            .error("new password should not be same as old password")
                            .build());
                }
                user.setPasswordHash(newPassword);
                repository.save(user); // Update the user's password

                return ResponseEntity.ok(ResetPasswordResponse.builder()
                        .success(true)
                        .message("Password reset successfully")
                        .build());
            } else {
                return ResponseEntity.badRequest().body(ResetPasswordResponse.builder()
                        .success(false)
                        .error("Invalid or expired reset token")
                        .build());
            }
        } catch (UserNotFoundException e) {
            return ResponseEntity.badRequest().body(ResetPasswordResponse.builder()
                    .success(false)
                    .error("User not found")
                    .build());
        } catch (Exception e) {
            // Handle token parsing or other errors
            return ResponseEntity.badRequest().body(ResetPasswordResponse.builder()
                    .success(false)
                    .error("Invalid token")
                    .build());
        }
    }

    @PostMapping("/forgot-reset-password")
    public ResponseEntity<ResetPasswordResponse> resetPassword(@RequestBody ValidateForgotPasswordResetRequest request) {
        try {
            String email = request.getEmail();

            Optional<User> optionalUser = repository.findByEmail(email);

            if (!optionalUser.isPresent()) {
                throw new UserNotFoundException("User Not Found");
            }

            User user = optionalUser.get();

            if(!request.getNewPassword().equals(request.getNewPasswordConfirm())){
                return ResponseEntity.ok(ResetPasswordResponse.builder()
                        .success(false)
                        .error("Password and Confirm Password does not match")
                        .build());
            }
            // Encrypt and set the new password
            if(!authenticationService.validatePassword(request.getNewPassword())){
                return ResponseEntity.ok(ResetPasswordResponse.builder()
                        .success(false)
                        .error("Password should contain at least 8 characters, including at least one uppercase letter, one lowercase letter, one number, and one special character")
                        .build());
            }
            String newPassword = passwordEncoder.encode(request.getNewPassword());
            user.setPasswordHash(newPassword);
            repository.save(user); // Update the user's password

            return ResponseEntity.ok(ResetPasswordResponse.builder()
                    .success(true)
                    .message("Password reset successfully")
                    .build());
        } catch (UserNotFoundException e) {
            return ResponseEntity.badRequest().body(ResetPasswordResponse.builder()
                    .success(false)
                    .error("User not found")
                    .build());
        } catch (Exception e) {
            // Handle token parsing or other errors
            return ResponseEntity.badRequest().body(ResetPasswordResponse.builder()
                    .success(false)
                    .error("Invalid Request")
                    .build());
        }
    }

    public boolean verifyPassword(String encodedPassword, String rawPassword) {
        System.out.println("verify");

        if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
            return false;
        }
        return true;
    }
}


