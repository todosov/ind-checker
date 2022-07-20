package com.otodosov.ind.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CheckResponse extends BaseResponse {

  private List<ResponseData> data;

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class ResponseData {
    private String key;
    private String date;
    private String startTime;
    private String endTime;
    private Integer parts;
  }
}
