package com.linkedin.data.entity;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class SimpleProduct {
  
  private UUID productId;
  private String name;
  private BigDecimal price;
  private UUID vendorId;
  private String vendorName;
  private String contact;
  private String phoneNumber;
  private String email;
  private String address;
  
  //-------------------------------------------------------------------------------------------------------------------
  
  @Override
  public String toString() {
    return "SimpleProduct [productId=" + productId + ", name=" + name + ", price=" + price + ", vendorId=" + vendorId +
        ", vendorName=" + vendorName + ", contact=" + contact + ", phoneNumber=" + phoneNumber +
        ", email=" + email + ", address=" + address + "]";
  }
  
  //-------------------------------------------------------------------------------------------------------------------
  
}
