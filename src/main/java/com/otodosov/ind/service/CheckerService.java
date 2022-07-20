package com.otodosov.ind.service;

import com.otodosov.ind.domain.CheckResponse;
import com.otodosov.ind.properties.YamlProperties;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.function.Predicate;

@Service
@RequiredArgsConstructor
public class CheckerService {

  private final YamlProperties properties;
  private final EmailService emailService;
  private final BookingService bookingService;
  private final RestService restService;

  private final String EMAIL_TEXT_TEMPLATE = "There is free slot! Date: %s, startTime: %s, endTime: %s";

  @SneakyThrows
  public void check() {
    CheckResponse checkResponse = restService.performGetRequest(properties.getInd().getSlotsUrl(), CheckResponse.class);

    checkResponse.getData()
        .stream()
        .min(Comparator.comparing(CheckResponse.ResponseData::getDate))
        .filter(filterFunction())
        .ifPresent(responseData -> {
          emailService.sendEmail(
              String.format(EMAIL_TEXT_TEMPLATE, responseData.getDate(), responseData.getStartTime(), responseData.getEndTime()));
          bookingService.bookAppointment(responseData);
          shutDown();
        });
  }

  /**
   * search slots till the end of August (9th month in response)
   */
  private Predicate<CheckResponse.ResponseData> filterFunction() {
    return responseData -> {
      int month = Integer.parseInt(responseData.getDate().substring(5, 7));
      return month < 10;
    };
  }

  private void shutDown() {
    restService.performPostRequest("http://localhost:8008/actuator/shutdown", null, null);
  }
}
