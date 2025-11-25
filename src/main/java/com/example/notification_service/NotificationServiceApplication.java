package com.example.notification_service;
import io.github.cdimascio.dotenv.Dotenv;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class NotificationServiceApplication {

	public static void main(String[] args) {
		
		Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();

		System.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD"));

		System.setProperty("AWS_REGION", dotenv.get("AWS_REGION"));
		System.setProperty("AWS_ACCESS_KEY", dotenv.get("AWS_ACCESS_KEY"));
		System.setProperty("AWS_SECRET_KEY", dotenv.get("AWS_SECRET_KEY"));

		System.setProperty("SES_FROM_EMAIL", dotenv.get("SES_FROM_EMAIL"));

		System.setProperty("SQS_USER_REGISTERED", dotenv.get("SQS_USER_REGISTERED"));
		System.setProperty("SQS_ORDER_PLACED", dotenv.get("SQS_ORDER_PLACED"));
		System.setProperty("SQS_ORDER_SHIPPED", dotenv.get("SQS_ORDER_SHIPPED"));
		System.setProperty("SQS_PAYMENT_COMPLETED", dotenv.get("SQS_PAYMENT_COMPLETED"));
		System.setProperty("SQS_PAYMENT_FAILED", dotenv.get("SQS_PAYMENT_FAILED"));

		SpringApplication.run(NotificationServiceApplication.class, args);
	}

}
