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
		ConfigurableApplicationContext context = SpringApplication.run(SpringJdbcBootApplication.class, args);
		ScheduleItemService serviceBean = context.getBean(ScheduleItemService.class);
		
		ScheduleItem byDate = serviceBean.findByDate(LocalDateTime.of(2025, 03, 02));
		System.out.println(byDate);

	}
}
