package com.otodosov.ind.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingResponse extends BaseResponse {

  private ResponseData data;

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  @Builder
  public static class ResponseData {
    private String code;
    private String date;
    private String startTime;
    private String endTime;
  }
}
