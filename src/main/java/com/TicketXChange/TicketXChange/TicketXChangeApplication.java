package com.TicketXChange.TicketXChange;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaRepositories(basePackages = {"com.TicketXChange.TicketXChange.auth.repository","com.TicketXChange.TicketXChange.payment", "com.TicketXChange.TicketXChange.ticket.repository"})
@ComponentScan(basePackages = {"com.TicketXChange.TicketXChange.auth","com.TicketXChange.TicketXChange.payment", "com.TicketXChange.TicketXChange.messagingstompwebsocket", "com.TicketXChange.TicketXChange.ticket", "com.TicketXChange.TicketXChange.chatgptverify", "com.TicketXChange.TicketXChange.home"})
@EnableScheduling
public class TicketXChangeApplication {

	public static void main(String[] args) {
		SpringApplication.run(TicketXChangeApplication.class, args);
	}

}
