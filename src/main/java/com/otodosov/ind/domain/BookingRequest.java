package com.otodosov.ind.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingRequest {

  private BookableSlot bookableSlot;
  private Appointment appointment;

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class BookableSlot {
    private String key;
    private String date;
    private String startTime;
    private String endTime;
    private Integer parts;
    private Boolean booked;
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class Appointment {
    private String productKey;
    private String date;
    private String startTime;
    private String endTime;
    private String email;
    private String phone;
    private String language;
    private List<Customer> customers;
  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  @Builder
  public static class Customer {
    @JsonProperty("vNumber")
    private String vNumber;
    private String firstName;
    private String lastName;
  }
}
