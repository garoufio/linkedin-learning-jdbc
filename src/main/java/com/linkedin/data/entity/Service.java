package com.linkedin.data.entity;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class Service {

  private UUID serviceId;
  private String name;
  private BigDecimal price;
  
  //-------------------------------------------------------------------------------------------------------------------
  
  @Override
  public String toString() {
    return "Service [service_id=" + serviceId + ", name=" + name + ", price=" + price + "]";
  }
  
  //-------------------------------------------------------------------------------------------------------------------_
  
}
