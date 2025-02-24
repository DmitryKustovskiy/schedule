package spring.configuration;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

//@Configuration
//@EnableTransactionManagement
//@PropertySource("classpath:application.yaml")
public class SpringConfig {
//
//    private final Environment env;
//    @Autowired
//    public SpringConfig(Environment env) {
//        this.env = env;
//    }

//    @Bean
//    public DataSource dataSource() {
//        DriverManagerDataSource dataSource = new DriverManagerDataSource();
//        dataSource.setDriverClassName(env.getRequiredProperty("spring.datasource.driver-class-name"));
//        dataSource.setUrl(env.getRequiredProperty("spring.datasource.url"));
//        dataSource.setUsername(env.getRequiredProperty("spring.datasource.username"));
//        dataSource.setPassword(env.getRequiredProperty("spring.datasource.password"));
//        return dataSource;
//    }

//    @Bean
//    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
//        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
//        emf.setDataSource(dataSource);
//        emf.setPackagesToScan("spring.model"); // Указываем пакеты с сущностями
//        emf.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
//
//        Properties properties = new Properties();
//        emf.setJpaProperties(properties);
//
//        return emf;
//    }
//
//    @Bean
//    @Primary
//    public EntityManager entityManager(EntityManagerFactory entityManagerFactory) {
//        return entityManagerFactory.createEntityManager();
//    }

//    @Bean
//    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
//        JpaTransactionManager transactionManager = new JpaTransactionManager();
//        transactionManager.setEntityManagerFactory(entityManagerFactory);
//        return transactionManager;
//    }
}
