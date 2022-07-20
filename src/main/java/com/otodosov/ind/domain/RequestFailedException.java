package com.otodosov.ind.domain;

public class RequestFailedException extends RuntimeException {

  public RequestFailedException(String message) {
    super(message);
  }
}
