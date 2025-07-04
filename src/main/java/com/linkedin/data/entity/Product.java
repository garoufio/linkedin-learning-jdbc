package com.linkedin.data.entity;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class Product {
  
  private UUID productId;
  private String name;
  private BigDecimal price;
  private UUID vendorId;
  
  //-------------------------------------------------------------------------------------------------------------------
  
  @Override
  public String toString() {
    return "Product [product_id=" + productId + ", name=" + name + ", price=" + price + ", vendor_id=" + vendorId + "]";
  }
  
  //-------------------------------------------------------------------------------------------------------------------
  
}
