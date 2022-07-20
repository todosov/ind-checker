package com.otodosov.ind.service;

import com.otodosov.ind.properties.YamlProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class EmailService {

  private final JavaMailSender emailSender;
  private final YamlProperties properties;

  @PostConstruct
  private void postConstruct() {
    sendEmail("Test email!");
  }

  public void sendEmail(String text) {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setFrom("alextod.zp@gmail.com");
    message.setTo(properties.getMail().getRecipient());
    message.setSubject("IND appointment");
    message.setText(text);
    emailSender.send(message);
  }
}