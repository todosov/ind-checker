package com.otodosov.ind.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.otodosov.ind.domain.BookingRequest;
import com.otodosov.ind.domain.BookingResponse;
import com.otodosov.ind.domain.CheckResponse;
import com.otodosov.ind.domain.RequestFailedException;
import com.otodosov.ind.properties.YamlProperties;
import com.otodosov.ind.service.map.DataMapper;
import com.otodosov.ind.service.map.DataMapperImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

  private DataMapper dataMapper;
  private YamlProperties properties;
  @Mock
  private RestService restService;
  @Mock
  private EmailService emailService;

  private BookingService bookingService;

  private ObjectMapper objectMapper;

  @BeforeEach
  void setUp() {
    objectMapper = new ObjectMapper();
    dataMapper = new DataMapperImpl();
    properties = YamlProperties.builder()
        .ind(YamlProperties.Ind.builder()
            .bookingUrl("booking_url")
            .build())
        .customer(YamlProperties.Customer.builder()
            .email("test@email.com")
            .firstName("Voornaam")
            .lastName("Achternaam")
            .phone("0837452998")
            .vNumber("123456789")
            .build())
        .build();

    bookingService = new BookingService(dataMapper, properties, restService, emailService);
  }

  @Test
  void shouldSendEmailIfFailed() throws Exception {
    String expectedBodyJson = new String(
        getClass()
            .getResourceAsStream(
                "/bookingResponse.json")
            .readAllBytes());
    BookingRequest expectedBody = objectMapper.readValue(expectedBodyJson, BookingRequest.class);

    BookingResponse expectedResponse = BookingResponse.builder()
        .data(BookingResponse.ResponseData.builder()
            .code("test_code")
            .date("2022-08-14")
            .startTime("09:21")
            .endTime("09:22")
            .build())
        .build();

    when(restService.performPostRequest(notNull(), notNull(), notNull()))
        .thenThrow(new RequestFailedException(""));

    bookingService.bookAppointment(CheckResponse.ResponseData.builder()
        .date("2022-10-21")
        .startTime("11:06")
        .endTime("11:13")
        .key("f2690e282c330f3800c2808446495c51")
        .parts(1)
        .build());

    verify(restService).performPostRequest(eq("booking_url"), eq(expectedBody), eq(BookingResponse.class));
    verify(emailService, never()).sendEmail("Appointment has been made! Appointment code: test_code, date: 2022-08-14, startTime: 09:21, endTime: 09:22");
    verify(emailService).sendEmail("Error occurred during booking. Booking failed!");
  }

  @Test
  void shouldBookAppointment() throws Exception {
    String expectedBodyJson = new String(
        getClass()
            .getResourceAsStream(
                "/bookingResponse.json")
            .readAllBytes());
    BookingRequest expectedBody = objectMapper.readValue(expectedBodyJson, BookingRequest.class);

    BookingResponse expectedResponse = BookingResponse.builder()
        .data(BookingResponse.ResponseData.builder()
            .code("test_code")
            .date("2022-08-14")
            .startTime("09:21")
            .endTime("09:22")
            .build())
        .build();

    when(restService.performPostRequest(notNull(), notNull(), notNull()))
        .thenReturn(expectedResponse);

    bookingService.bookAppointment(CheckResponse.ResponseData.builder()
        .date("2022-10-21")
        .startTime("11:06")
        .endTime("11:13")
        .key("f2690e282c330f3800c2808446495c51")
        .parts(1)
        .build());

    verify(restService).performPostRequest(eq("booking_url"), eq(expectedBody), eq(BookingResponse.class));
    verify(emailService).sendEmail("Appointment has been made! Appointment code: test_code, date: 2022-08-14, startTime: 09:21, endTime: 09:22");
  }
}