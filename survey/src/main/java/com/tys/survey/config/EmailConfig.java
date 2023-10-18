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
        javaMailSender.setHost(this.host);
        javaMailSender.setPort(this.port);
        javaMailSender.setUsername(this.userName);
        javaMailSender.setPassword(this.password);
        javaMailSender.setProtocol(this.protocol);
        javaMailSender.setDefaultEncoding(this.encoding);
        javaMailSender.setJavaMailProperties(this.getMailProperties());
        return javaMailSender;
    }

    @Bean
    public Properties getMailProperties(){
        Properties prop = new Properties();
        prop.put("mail.debug",this.debug);
        prop.put("mail.smtp.timeout",this.timeOut);
        prop.put("mail.smtp.starttls.enable",this.starttlsEnable);
        prop.put("mail.smtp.starttls.required",this.starttlsRequired);
        prop.put("mail.smtp.auth",this.auth);
        return prop;
    }

}
