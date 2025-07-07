package com.linkedin.data.dao;

import com.linkedin.data.entity.Product;
import com.linkedin.data.util.DatabaseUtils;

import javax.xml.crypto.Data;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.logging.Logger;

public class SimpleProductDao {
  
  private static final Logger LOGGER = Logger.getLogger(SimpleProductDao.class.getName());
  private static final String CREATE = "select * from createproduct(?, ?, ?)";
  
  //-------------------------------------------------------------------------------------------------------------------
  
  public UUID createProduct(String name, BigDecimal price, String vendorName) {
    Connection connection = DatabaseUtils.getConnection();
    UUID uuid = null;
    
    try {
      connection.setAutoCommit(false);
      PreparedStatement statement = connection.prepareStatement(CREATE);
      statement.setString(1, name);
      statement.setBigDecimal(2, price);
      statement.setString(3, vendorName);
      ResultSet rs = statement.executeQuery();
      while (rs.next()) {
        uuid = (UUID)rs.getObject("createproduct");
      }
      connection.commit();
      rs.close();
      statement.close();
    } catch (SQLException ex) {
      try {
        connection.rollback();
      }
      catch (SQLException sqlex) {
        DatabaseUtils.handleSqlException("SimpleProductDao.createProduct.rollback", sqlex, LOGGER);
      }
      DatabaseUtils.handleSqlException("SimpleProductDao.createProduct", ex, LOGGER);
    }
    return uuid;
  }
  
  //-------------------------------------------------------------------------------------------------------------------
  
}
