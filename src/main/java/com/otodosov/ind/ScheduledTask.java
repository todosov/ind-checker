package com.otodosov.ind;

import com.otodosov.ind.service.CheckerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Log4j2
@RequiredArgsConstructor
public class ScheduledTask {

  private final CheckerService checkerService;

  @Scheduled(fixedDelay = 10000)
  public void checkSlots() {
    checkerService.check();
  }

}
