package com.otodosov.ind.service;

import com.otodosov.ind.domain.BookingRequest;
import com.otodosov.ind.domain.BookingResponse;
import com.otodosov.ind.domain.CheckResponse;
import com.otodosov.ind.properties.YamlProperties;
import com.otodosov.ind.service.map.DataMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {

  private final DataMapper dataMapper;
  private final YamlProperties properties;
  private final RestService restService;
  private final EmailService emailService;

  private final String EMAIL_TEXT_TEMPLATE = "Appointment has been made! Appointment code: %s, date: %s, startTime: %s, endTime: %s";

  public void bookAppointment(CheckResponse.ResponseData responseData) {
    BookingRequest body = buildRequest(responseData);
    BookingResponse bookingResponse = restService.performPostRequest(
        properties.getInd().getBookingUrl(),
        body,
        BookingResponse.class);

    emailService.sendEmail(
        String.format(
            EMAIL_TEXT_TEMPLATE,
            bookingResponse.getData().getCode(),
            bookingResponse.getData().getDate(),
            bookingResponse.getData().getStartTime(),
            bookingResponse.getData().getEndTime()));
  }

  private BookingRequest buildRequest(CheckResponse.ResponseData responseData) {
    BookingRequest.BookableSlot bookableSlot = dataMapper.responseDataToBookableSlot(responseData);
    BookingRequest.Appointment appointment = dataMapper.responseDataToAppointment(responseData);
    appointment.setEmail(properties.getCustomer().getEmail());
    appointment.setPhone(properties.getCustomer().getPhone());
    appointment.setCustomers(
        List.of(
            BookingRequest.Customer.builder()
                .vNumber(properties.getCustomer().getVNumber())
                .firstName(properties.getCustomer().getFirstName())
                .lastName(properties.getCustomer().getLastName())
                .build()
        )
    );

    return BookingRequest.builder()
        .appointment(appointment)
        .bookableSlot(bookableSlot)
        .build();
  }
}
