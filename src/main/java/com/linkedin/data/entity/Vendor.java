package com.linkedin.data.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class Vendor {
  
  private UUID vendorId;
  private String name;
  private String contact;
  private String phone;
  private String email;
  private String address;
  
  //-------------------------------------------------------------------------------------------------------------------
  
  @Override
  public String toString() {
    return "Vendor [" +
        "vendor_id=" + vendorId +
        ", name=" + name +
        ", contact=" + contact +
        ", phone=" + phone +
        ", email=" + email +
        ", address=" + address +
        "]";
  }
  
  //-------------------------------------------------------------------------------------------------------------------
  
}
