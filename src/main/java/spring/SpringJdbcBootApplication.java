package spring;

import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import spring.model.Student;
import spring.service.StudentService;

@SpringBootApplication
public class SpringJdbcBootApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(SpringJdbcBootApplication.class, args);
    }

}
