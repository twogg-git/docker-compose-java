package emailboot;

import emailboot.util.EmailProperties;
import emailboot.util.SMTPProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableWebMvc
@EnableAsync
@EnableTransactionManagement
@ComponentScan(basePackages = {
        "emailboot.logger.service",
        "emailboot.rest",
        "emailboot.util.exception"})
@PropertySource(value = {"classpath:application.properties"})
public class ApiConfig {

    @Autowired
    private Environment env;

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(env.getProperty("database.driver"));
        dataSource.setUrl(env.getProperty("database.jdbcConn"));
        dataSource.setUsername(env.getProperty("database.username"));
        dataSource.setPassword(env.getProperty("database.password"));
        return dataSource;
    }

    @Bean
    public EntityManagerFactory entityManagerFactory() {
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setGenerateDdl(true);
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setJpaVendorAdapter(vendorAdapter);
        factory.setPackagesToScan("emailboot.logger.entities");
        factory.setDataSource(dataSource());
        factory.setJpaPropertyMap(jpaProperties());
        factory.afterPropertiesSet();
        return factory.getObject();
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        JpaTransactionManager txManager = new JpaTransactionManager();
        txManager.setEntityManagerFactory(entityManagerFactory());
        return txManager;
    }

    private Map<String, ?> jpaProperties() {
        Map<String, String> jpaPropertiesMap = new HashMap<String, String>();
        jpaPropertiesMap.put("hibernate.dialect", env.getProperty("hibernate.dialect"));
        jpaPropertiesMap.put("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));
        jpaPropertiesMap.put("hibernate.show_sql", env.getProperty("hibernate.show_sql"));
        jpaPropertiesMap.put("hibernate.format_sql", env.getProperty("hibernate.format_sql"));
        jpaPropertiesMap.put("hibernate.max_fetch_depth", env.getProperty("entities.max_fetch_depth"));
        return jpaPropertiesMap;
    }

    @Bean
    public EmailProperties emailProperties() {
        EmailProperties emailProperties = new EmailProperties();
        emailProperties.setMailFromAddress(env.getProperty("mail.from.address"));
        emailProperties.setMaxAttachmentsSize(env.getProperty("mail.max.file.size"));
        emailProperties.setVelocityTemplate(env.getProperty("velocity.template"));
        emailProperties.setVelocityTemplatePath(env.getProperty("velocity.template.path"));
        emailProperties.setHeaderImgPath(env.getProperty("velocity.logo.header"));
        return emailProperties;
    }

    @Bean
    public SMTPProperties smtpProperties() {
        SMTPProperties smtpProperties = new SMTPProperties();
        smtpProperties.setHost(env.getProperty("mail.host"));
        smtpProperties.setPort(env.getProperty("mail.host.port"));
        smtpProperties.setUsername(env.getProperty("mail.host.username"));
        smtpProperties.setPassword(env.getProperty("mail.host.password"));
        smtpProperties.setProtocol(env.getProperty("mail.transport.protocol"));
        smtpProperties.setSmtpAuth(env.getProperty("mail.smtp.auth"));
        smtpProperties.setEnableTls(env.getProperty("mail.smtp.starttls.enable"));
        return smtpProperties;
    }

}
