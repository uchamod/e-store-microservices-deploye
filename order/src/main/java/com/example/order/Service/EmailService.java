package com.example.order.Service;


import com.example.order.DTO.EmailDTO;
import com.example.order.DTO.UserWrapper;
import com.example.order.Model.Order;
import com.example.order.Model.OrderProductModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;

import java.text.SimpleDateFormat;


@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender javaMailSender;
   // private final TemplateEngine templateEngine;

    @Value("${app.email.from}")
    private String fromEmail;

    //sending simple email
    @Async
    public void sendSimpleEmail(String to,String subject,String body){
        try{
            SimpleMailMessage message=new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);

            javaMailSender.send(message);
            log.info("Email sent successfully to: {}", to);
        }catch (Exception e){
            log.error("Failed to send email to: {}. Error: {}", to, e.getMessage());
        }
    }
    //text email templete
    @Async
    public void sendOrderNotificationToSeller(UserWrapper seller, Order order, UserWrapper customer,
                                              EmailDTO emailDTO, StringBuilder body, String subjectPrefix) {
        try {
           // String subject = "New Order Received - Order #" + order.getOrderId().toString().substring(0, 8);

           // StringBuilder body = new StringBuilder();
            body.setLength(0);
            body.append("Dear ").append(seller.getUserName()).append(",\n\n");
            body.append("You have received a new order!\n\n");
            body.append("Order Details:\n");
            body.append("===================\n");
            body.append("Order ID: ").append(order.getOrderId()).append("\n");
            body.append("Order Status: ").append(order.getOrderStatus()).append("\n");
            body.append("Order Date: ").append(new SimpleDateFormat("dd-MM-yyyy").format(order.getLastUpdate())).append("\n\n");

            body.append("Customer Information:\n");
            body.append("-------------------\n");
            body.append("Name: ").append(customer.getUserName()).append("\n");
            body.append("Email: ").append(customer.getUserEmail()).append("\n");
            body.append("Phone: ").append(customer.getUserPhone() != null ? customer.getUserPhone() : "N/A").append("\n");
            body.append("Address: ").append(customer.getUserAddress() != null ? customer.getUserAddress() : "N/A").append("\n\n");

            body.append("Your Products in This Order:\n");
            body.append("----------------------------\n");

            for(OrderProductModel orderProductModel : emailDTO.getOrderProductModelList()){
                body.append(String.format("Product ID: %s\n", orderProductModel.getProductId()));
                body.append(String.format("Quantity: %d\n", orderProductModel.getProductCount()));
                body.append(String.format("Unit Price: $%.2f\n", orderProductModel.getProductItemPrice()));
                body.append(String.format("Subtotal: $%.2f\n\n", orderProductModel.getProductCount()*orderProductModel.getProductItemPrice()));
            }


            body.append(String.format("Your Total: $%.2f\n\n", emailDTO.getTotalAmount()));
            body.append("Please prepare these items for shipping.\n\n");
            body.append("Thank you for your business!\n\n");
            body.append("Best regards,\n");
            body.append("Your Marketplace Team");

            sendSimpleEmail(seller.getUserEmail(), subjectPrefix + order.getOrderId(), body.toString());

        } catch (Exception e) {
            log.error("Failed to send order notification to seller: {}. Error: {}",
                    seller.getUserEmail(), e.getMessage());
        }
    }



}
