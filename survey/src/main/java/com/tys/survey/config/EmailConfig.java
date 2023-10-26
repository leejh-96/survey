package com.tys.survey.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
@PropertySource("classpath:email.properties")
public class EmailConfig {

    @Value("${spring.mail.host}")
    private String host;
    @Value("${spring.mail.port}")
    private int port;
    @Value("${spring.mail.username}")
    private String userName;
    @Value("${spring.mail.password}")
    private String password;
    @Value("${spring.mail.properties.mail.transport.protocol}")
    private String protocol;
    @Value("${spring.mail.default.encoding}")
    private String encoding;
    @Value("${spring.mail.properties.mail.debug}")
    private String debug;
    @Value("${spring.mail.properties.mail.smtp.timeout}")
    private int timeOut;
    @Value("${spring.mail.properties.mail.smtp.starttls.enable}")
    private String starttlsEnable;
    @Value("${spring.mail.properties.mail.smtp.starttls.required}")
    private String starttlsRequired;
    @Value("${spring.mail.properties.mail.smtp.auth}")
    private String auth;

    @Bean
    public JavaMailSender javaMailSender(){
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost(host);
        javaMailSender.setPort(port);
        javaMailSender.setUsername(userName);
        javaMailSender.setPassword(password);
        javaMailSender.setProtocol(protocol);
        javaMailSender.setDefaultEncoding(encoding);
        javaMailSender.setJavaMailProperties(getMailProperties());
        return javaMailSender;
    }

    @Bean
    public Properties getMailProperties(){
        Properties prop = new Properties();
        prop.put("mail.debug",debug);
        prop.put("mail.smtp.timeout",timeOut);
        prop.put("mail.smtp.starttls.enable",starttlsEnable);
        prop.put("mail.smtp.starttls.required",starttlsRequired);
        prop.put("mail.smtp.auth",auth);
        return prop;
    }

}
