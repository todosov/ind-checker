package com.otodosov.ind.service.map;

import com.otodosov.ind.domain.BookingRequest;
import com.otodosov.ind.domain.CheckResponse;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = "spring",
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DataMapper {

  @Mapping(target = "booked", constant = "false")
  BookingRequest.BookableSlot responseDataToBookableSlot(CheckResponse.ResponseData data);

  @Mapping(target = "language", constant = "en")
  @Mapping(target = "productKey", constant = "DOC")
  BookingRequest.Appointment responseDataToAppointment(CheckResponse.ResponseData data);
}
