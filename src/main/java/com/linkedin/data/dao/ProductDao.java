package com.linkedin.data.dao;

import com.linkedin.data.entity.Product;
import com.linkedin.data.entity.Service;
import com.linkedin.data.util.DatabaseUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;

public class ProductDao implements Dao<Product, UUID> {
  
  private static final Logger LOGGER = Logger.getLogger(ProductDao.class.getName());
  private static final String GET_ALL = "SELECT product_id, name, price, vendor_id FROM wisdom.products";
  private static final String GET_BY_ID = "SELECT product_id, name, price, vendor_id FROM wisdom.products WHERE product_id = ?";
  private static final String CREATE = "INSERT INTO wisdom.products (product_id, name, price, vendor_id) VALUES (?, ?, ?, ?)";
  private static final String UPDATE = "UPDATE wisdom.products SET name = ?, price = ?, vendor_id = ? WHERE product_id = ?";
  private static final String DELETE = "DELETE FROM wisdom.products WHERE product_id = ?";
  
  
  //-------------------------------------------------------------------------------------------------------------------
  
  @Override
  public List<Product> getAll() {
    List<Product> products = null;
    Connection connection = DatabaseUtils.getConnection();
    
    try (Statement statement = connection.createStatement()) {
      ResultSet rs = statement.executeQuery(GET_ALL);
      products = processResultSet(rs);
    } catch (SQLException ex) {
      DatabaseUtils.handleSqlException("ProductDao.getAll", ex, LOGGER);
    }
    return products == null ? new ArrayList<>() : products;
  }
  
  //-------------------------------------------------------------------------------------------------------------------
  
  @Override
  public Product create(Product entity) {
    UUID productId = UUID.randomUUID();
    Connection connection = DatabaseUtils.getConnection();
    
    try {
      connection.setAutoCommit(false);
      PreparedStatement statement = connection.prepareStatement(CREATE);
      statement.setObject(1, productId);
      statement.setString(2, entity.getName());
      statement.setBigDecimal(3, entity.getPrice());
      statement.setObject(4, entity.getVendorId());
      statement.execute();
      connection.commit();
      statement.close();
    }  catch (SQLException ex) {
      try {
        connection.rollback();
      } catch (SQLException sqlex) {
        DatabaseUtils.handleSqlException("ProductDao.create.rollback", sqlex, LOGGER);
      }
      DatabaseUtils.handleSqlException("ProductDao.create", ex, LOGGER);
    }
    
    Optional<Product> product = this.getById(productId);
    return product.isPresent() ? product.get() : null;
  }
  
  //-------------------------------------------------------------------------------------------------------------------
  
  @Override
  public Product update(Product entity) {
    Connection connection = DatabaseUtils.getConnection();
    
    try {
      connection.setAutoCommit(false);
      PreparedStatement statement = connection.prepareStatement(UPDATE);
      statement.setString(1, entity.getName());
      statement.setBigDecimal(2, entity.getPrice());
      statement.setObject(3, entity.getVendorId());
      statement.setObject(4, entity.getProductId());
      statement.execute();
      connection.commit();
      statement.close();
    } catch (SQLException ex) {
      try {
        connection.rollback();
      } catch (SQLException sqlex) {
        DatabaseUtils.handleSqlException("ProductDao.update.rollback", sqlex, LOGGER);
      }
      DatabaseUtils.handleSqlException("productDao.update", ex, LOGGER);
    }
    Optional<Product> product = getById(entity.getProductId());
    return product.isPresent() ? product.get() : entity;
  }
  
  //-------------------------------------------------------------------------------------------------------------------
  
  @Override
  public void delete(UUID id) {
    Connection connection = DatabaseUtils.getConnection();
    
    try {
      connection.setAutoCommit(false);
      PreparedStatement statement  = connection.prepareStatement(DELETE);
      statement.setObject(1, id);
      statement.execute();
      connection.commit();
      statement.close();
    } catch (SQLException ex) {
      try {
        connection.rollback();
      } catch (SQLException sqlex) {
        DatabaseUtils.handleSqlException("ProductDao.delete.rollback", ex, LOGGER);
      }
      DatabaseUtils.handleSqlException("ProductDao.delete", ex, LOGGER);
    }
  }
  
  //-------------------------------------------------------------------------------------------------------------------
  
  @Override
  public Optional<Product> getById(UUID id) {
    Connection connection = DatabaseUtils.getConnection();
    
    try (PreparedStatement statement = connection.prepareStatement(GET_BY_ID)) {
      statement.setObject(1, id);
      ResultSet rs = statement.executeQuery();
      if (rs.next()) {
        Product product = new Product();
        product.setProductId(id);
        product.setName(rs.getString("name"));
        product.setPrice(rs.getBigDecimal("price"));
        product.setVendorId((UUID) rs.getObject("vendor_id"));
        return Optional.of(product);
      }
    } catch (SQLException ex) {
      DatabaseUtils.handleSqlException("ProductDao.getById", ex, LOGGER);
    }
    return Optional.empty();
  }
  
  //-------------------------------------------------------------------------------------------------------------------
  
  private List<Product> processResultSet(ResultSet rs) throws SQLException {
    List<Product> products = new ArrayList<>();
    
    while (rs.next()) {
      Product product = new Product();
      product.setProductId((UUID) rs.getObject("product_id"));
      product.setName(rs.getString("name"));
      product.setPrice(rs.getBigDecimal("price"));
      product.setVendorId((UUID) rs.getObject("vendor_id"));
      products.add(product);
    }
    return products;
  }
  
  //-------------------------------------------------------------------------------------------------------------------
}
