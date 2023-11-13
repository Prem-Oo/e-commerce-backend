package com.shopping.service;



import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.shopping.serviceModels.LineItem;
import com.shopping.serviceModels.Order;

@Service
public class ShoppingService {
	
	
	@Autowired
	private JavaMailSender mailSender;
	
	@Value("${spring.mail.username}")
	private String fromMail;
	
	
	public void sendMail(String toEmail,Order orderDetails) {
		System.out.println(fromMail);
		System.out.println(toEmail);
		System.out.println(orderDetails);
		
		StringBuilder emailText = new StringBuilder();
	    emailText.append("<html><body>");
	    emailText.append("<h1>Order Details</h1>");

	    double totalPrice = 0.0;

	    for (LineItem item : orderDetails.getLineItems()) {
	        emailText.append("<p><strong>Product Name:</strong> ").append(item.getProductName()).append("</p>");
	        emailText.append("<p><strong>Quantity:</strong> ").append(item.getQuantity()).append("</p>");
	        emailText.append("<p><strong>Price:</strong> &#8377;").append(item.getPrice()).append("</p>");
	        emailText.append("<br>");

	        totalPrice += item.getPrice() * item.getQuantity();
	    }

	    emailText.append("<p><strong>Total Price:</strong> &#8377;").append(totalPrice).append("</p>");
	    emailText.append("</body></html>");
	    
	    MimeMessage message = mailSender.createMimeMessage();

	    try {
	        MimeMessageHelper helper = new MimeMessageHelper(message, true);
	        helper.setFrom(fromMail);
	        helper.setSubject("Your Order Details");
	        helper.setTo(toEmail);
	        helper.setText(emailText.toString(), true); // Set the content type to HTML

	        mailSender.send(message);
	        System.out.println("sendMail() - successful");
	    } catch (MessagingException e) {
	    	System.out.println("problem occured while sending order-mail");
	    }
	}

}
