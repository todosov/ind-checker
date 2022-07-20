package com.otodosov.ind.service;

import com.otodosov.ind.domain.CheckResponse;
import com.otodosov.ind.properties.YamlProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CheckerServiceTest {

  private YamlProperties properties;
  @Mock private EmailService emailService;
  @Mock private BookingService bookingService;
  @Mock private RestService restService;

  private CheckerService checkerService;

  @BeforeEach
  void setUp() {
    properties = YamlProperties.builder()
        .ind(YamlProperties.Ind.builder()
            .slotsUrl("slot_url")
            .build())
        .mail(YamlProperties.Mail.builder()
            .recipient("test_email")
            .build())
        .build();
    checkerService = new CheckerService(properties, emailService, bookingService, restService);
  }

  @Test
  void shouldDoNothingIfNoData() {
    when(restService.performGetRequest("slot_url", CheckResponse.class))
        .thenReturn(CheckResponse.builder()
            .data(List.of())
            .build());

    checkerService.check();

    verify(restService).performGetRequest("slot_url", CheckResponse.class);
    verify(emailService, never()).sendEmail(anyString());
    verify(bookingService, never()).bookAppointment(any());
  }

  @Test
  void shouldDoNothingIfSlotIsLaterThenAugust() {
    when(restService.performGetRequest("slot_url", CheckResponse.class))
        .thenReturn(CheckResponse.builder()
            .data(List.of(
                CheckResponse.ResponseData.builder()
                    .date("2022-10-04")
                    .build(),
                CheckResponse.ResponseData.builder()
                    .date("2022-11-14")
                    .build()
            ))
            .build());

    checkerService.check();

    verify(restService).performGetRequest("slot_url", CheckResponse.class);
    verify(emailService, never()).sendEmail(anyString());
    verify(bookingService, never()).bookAppointment(any());
  }

  @Test
  void shouldSendEmailAndBookAppointmentForNearestSlotAndShutDownApp() {
    CheckResponse.ResponseData expected = CheckResponse.ResponseData.builder()
        .date("2022-08-14")
        .startTime("09:21")
        .endTime("09:22")
        .build();
    when(restService.performGetRequest("slot_url", CheckResponse.class))
        .thenReturn(CheckResponse.builder()
            .data(List.of(
                CheckResponse.ResponseData.builder()
                    .date("2022-09-04")
                    .build(),
                expected
            ))
            .build());

    checkerService.check();

    verify(restService).performGetRequest("slot_url", CheckResponse.class);
    verify(emailService).sendEmail("There is free slot! Date: 2022-08-14, startTime: 09:21, endTime: 09:22");
    verify(bookingService).bookAppointment(expected);
    verify(restService).performPostRequest("http://localhost:8008/actuator/shutdown", null, null);
  }
}