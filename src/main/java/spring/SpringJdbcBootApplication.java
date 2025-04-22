package spring;

import java.time.LocalDateTime;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import spring.model.ScheduleItem;
import spring.service.ScheduleItemService;

@SpringBootApplication
public class SpringJdbcBootApplication {
	public static void main(String[] args) {
		SpringApplication.run(SpringJdbcBootApplication.class, args);

	}
}
