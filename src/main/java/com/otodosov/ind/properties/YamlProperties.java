package com.otodosov.ind.properties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Component
@ConfigurationProperties(prefix = "app")
public class YamlProperties {
  private Ind ind;
  private Mail mail;
  private Customer customer;

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class Ind {
    private String slotsUrl;
    private String bookingUrl;
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class Mail {
    private String recipient;
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class Customer {
    private String phone;
    private String email;
    private String vNumber;
    private String firstName;
    private String lastName;
  }
}
