package com.TicketXChange.TicketXChange.payment.service;

import com.TicketXChange.TicketXChange.payment.dto.RazorpayOrder;
import com.TicketXChange.TicketXChange.payment.dto.RazorpayPayment;
import com.razorpay.*;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RazorpayService {

    @Value("${razorpay.api.key}")
    private String apiKey;

    @Value("${razorpay.api.secret}")
    private String apiSecret;

    private RazorpayClient razorpayClient;

    @PostConstruct
    public void init() {
        try {
            if (apiKey == null || apiSecret == null) {
                throw new IllegalArgumentException("Razorpay API key and secret must be set.");
            }
            this.razorpayClient = new RazorpayClient(apiKey, apiSecret);
        } catch (RazorpayException e) {
            e.printStackTrace();
        }
    }

    public RazorpayOrder createOrder(String email, int amountInPaise, String currency) throws RazorpayException {
        JSONObject options = new JSONObject();
        options.put("amount", amountInPaise);
        options.put("currency", currency);
        options.put("receipt", "order_rcptid_" + System.currentTimeMillis());
        options.put("payment_capture", 1);

        Order order = razorpayClient.orders.create(options);
        RazorpayOrder razorpayOrder = new RazorpayOrder();
        razorpayOrder.setId(order.get("id"));
        razorpayOrder.setAmount(order.get("amount").toString());
        razorpayOrder.setCurrency(order.get("currency"));
        // Set other fields as needed
        return razorpayOrder;
    }

    public RazorpayOrder getOrder(String orderId) throws RazorpayException {
        Order order = razorpayClient.orders.fetch(orderId);
        RazorpayOrder razorpayOrder = new RazorpayOrder();
        razorpayOrder.setId(order.get("id"));
        razorpayOrder.setAmount(order.get("amount"));
        razorpayOrder.setCurrency(order.get("currency"));
        // Set other fields as needed
        return razorpayOrder;
    }

    public boolean isPaymentSuccessful(String orderId) throws RazorpayException {
        Order order = razorpayClient.orders.fetch(orderId);
        String paymentStatus = order.get("status");
        return "paid".equalsIgnoreCase(paymentStatus);
    }

    public RazorpayPayment fetchPayment(String paymentId) throws RazorpayException {
        Order payment = razorpayClient.orders.fetch(paymentId);
        RazorpayPayment razorpayPayment = new RazorpayPayment();
        razorpayPayment.setId(payment.get("id"));
        razorpayPayment.setAmount(payment.get("amount"));
        razorpayPayment.setStatus(payment.get("status"));
        // Set other fields as needed
        return razorpayPayment;
    }
}
