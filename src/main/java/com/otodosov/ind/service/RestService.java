package com.otodosov.ind.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.otodosov.ind.domain.BaseResponse;
import com.otodosov.ind.domain.BookingRequest;
import com.otodosov.ind.domain.RequestFailedException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Log4j2
public class RestService {

  private final RestTemplate restTemplate;
  private final ObjectMapper objectMapper = new ObjectMapper();

  public <T extends BaseResponse> T performGetRequest(String url, Class<T> responseType) {
    log.info("Performing GET request to {}", url);
    ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);

    return parseBody(responseEntity, responseType);
  }

  public <T extends BaseResponse> T performPostRequest(String url, BookingRequest body, Class<T> responseType) {
    log.info("Performing POST request to {}", url);
    ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, body, String.class);

    return parseBody(responseEntity, responseType);
  }

  @SneakyThrows
  private <T extends BaseResponse> T parseBody(ResponseEntity<String> responseEntity, Class<T> responseType) {
    if (!responseEntity.getStatusCode().is2xxSuccessful()) {
      log.error("Check request failed. Http status code: {}", responseEntity.getStatusCode());
      throw new RequestFailedException("check request failed");
    }

    String body = responseEntity.getBody();
    T response = objectMapper.readValue(body.substring(body.indexOf("{")), responseType);

    if (!response.getStatus().equals("OK")) {
      log.error("Check request failed. Status is {}", response.getStatus());
      throw new RequestFailedException("check request failed. status is not ok");
    }

    return response;
  }
}
