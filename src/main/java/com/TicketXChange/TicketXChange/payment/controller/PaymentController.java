package com.TicketXChange.TicketXChange.payment.controller;

import com.TicketXChange.TicketXChange.payment.dto.RazorpayOrder;
import com.TicketXChange.TicketXChange.payment.service.RazorpayService;
import com.razorpay.*;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@RestController
@RequestMapping("api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    @Autowired
    private RazorpayService razorpayService;

    @PostMapping("/initiate")
    public RazorpayOrder initiatePayment(@RequestParam String email, @RequestParam int amountInPaise, @RequestParam String currency) throws RazorpayException {
        System.out.println("initiatePayment");
        return razorpayService.createOrder(email, amountInPaise, currency);
    }


    @GetMapping("/payment/{orderId}")
    public String checkPaymentStatus(@PathVariable String orderId) throws RazorpayException {
        boolean isPaid = razorpayService.isPaymentSuccessful(orderId);
        return isPaid ? "Payment successful" : "Payment not successful";
    }

    @GetMapping("/order/{orderId}")
    public RazorpayOrder getOrder(@PathVariable String orderId) throws RazorpayException {
        return razorpayService.getOrder(orderId);
    }

    @PostMapping("/razorpay/webhook")
    public ResponseEntity<String> handleRazorpayWebhook(@RequestBody String payload, @RequestHeader("X-Razorpay-Signature") String signature) {
        // Your webhook secret key (set in Razorpay dashboard)
        String webhookSecret = "";

        // Verify the webhook payload
        if (!verifyWebhookSignature(payload, signature, webhookSecret)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid signature");
        }

        // Parse the payload to extract payment details and update your system
        // Example logic to handle webhook payload
        try {
            JSONObject json = new JSONObject(payload);
            JSONObject paymentEntity = json.getJSONObject("payload").getJSONObject("payment").getJSONObject("entity");

            String orderId = paymentEntity.getString("order_id");
            String paymentId = paymentEntity.getString("id");
            System.out.println(paymentId);
            String status = paymentEntity.getString("status");

            // Handle the status update accordingly
            if ("captured".equals(status)) {
                // Payment successful handling
                // Update your database, send confirmation emails, etc.
                return ResponseEntity.ok("Payment successful");
            } else if ("failed".equals(status)) {
                // Payment failed handling
                // Log or update database with failure information
                return ResponseEntity.ok("Payment failed");
            } else {
                // Handle other statuses if needed
                return ResponseEntity.ok("Unhandled status: " + status);
            }
        } catch (JSONException e) {
            return ResponseEntity.badRequest().body("Invalid webhook payload");
        }
    }

    private boolean verifyWebhookSignature(String payload, String signature, String secret) {
        try {
            Mac sha256Hmac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
            sha256Hmac.init(secretKey);
            byte[] signedBytes = sha256Hmac.doFinal(payload.getBytes());
            String computedSignature = Base64.getEncoder().encodeToString(signedBytes);
            return computedSignature.equals(signature);
        } catch (Exception e) {
            return false;
        }
    }
}
