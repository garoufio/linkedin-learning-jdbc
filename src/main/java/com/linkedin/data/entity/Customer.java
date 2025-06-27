package com.linkedin.data.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class Customer {
  
  private UUID customerId;
  private String firstname;
  private String lastname;
  private String email;
  private String phone;
  private String address;
  
  //-------------------------------------------------------------------------------------------------------------------
  
  @Override
  public String toString() {
    return "Customer [" +
          "customer_id=" + customerId +
          ", first_name=" + firstname +
          ", last_name=" + lastname +
          ", email=" + email +
          ", phone=" + phone +
          ", address=" + address +
        "]";
  }
  
  //-------------------------------------------------------------------------------------------------------------------

}
