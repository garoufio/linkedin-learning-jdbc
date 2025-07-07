package com.linkedin.data.dao;

import com.linkedin.data.entity.Vendor;
import com.linkedin.data.util.DatabaseUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;

public class VendorDao implements Dao<Vendor, UUID> {
  
  private static final Logger LOGGER = Logger.getLogger(VendorDao.class.getName());
  private static final String GET_ALL = "SELECT vendor_id, name, contact, phone, email, address " +
      "FROM wisdom.vendors";
  private static final String GET_BY_ID = "SELECT vendor_id, name, contact, phone, email, address " +
      "from wisdom.vendors where vendor_id = ?";
  private static final String DELETE = "DELETE FROM wisdom.vendors WHERE vendor_id = ?";
  private static final String CREATE =  "INSERT INTO wisdom.vendors " +
      "(vendor_id, name, contact, phone, email, address) VALUES (?, ?, ?, ?, ?, ?)";
  private static final String UPDATE = "UPDATE wisdom.vendors " +
      "SET name = ?, contact = ?, phone = ?, email = ?, address = ? " +
      "WHERE vendor_id = ?";
  
  //-------------------------------------------------------------------------------------------------------------------
  
  @Override
  public List<Vendor> getAll() {
    List<Vendor> vendors = null;
    Connection connection = DatabaseUtils.getConnection();
    
    try (Statement statement = connection.createStatement();) {
      ResultSet rs = statement.executeQuery(GET_ALL);
      vendors = processResultSet(rs);
      rs.close();
    } catch (SQLException ex) {
      DatabaseUtils.handleSqlException("VendorDao.getAll", ex, LOGGER);
    }
    return vendors == null ? new ArrayList<>() : vendors;
  }
  
  //-------------------------------------------------------------------------------------------------------------------
  
  @Override
  public Vendor create(Vendor entity) {
    UUID vendorId = UUID.randomUUID();
    Connection connection = DatabaseUtils.getConnection();
    
    try {
      connection.setAutoCommit(false);
      PreparedStatement statement = connection.prepareStatement(CREATE);
      statement.setObject(1, vendorId);
      statement.setString(2, entity.getName());
      statement.setString(3, entity.getContact());
      statement.setString(4, entity.getPhone());
      statement.setString(5, entity.getEmail());
      statement.setString(6, entity.getAddress());
      statement.execute();
      connection.commit();
      statement.close();
    } catch (SQLException ex) {
      try {
        connection.rollback();
      } catch (SQLException sqlex) {
        DatabaseUtils.handleSqlException("VendorDao.create.rollback", sqlex, LOGGER);
      }
      DatabaseUtils.handleSqlException("VendorDao.create", ex, LOGGER);
    }
    Optional<Vendor> vendor = this.getById(vendorId);
    return vendor.isPresent() ? vendor.get() : null;
  }
  
  //-------------------------------------------------------------------------------------------------------------------
  
  @Override
  public Vendor update(Vendor entity) {
    Connection connection = DatabaseUtils.getConnection();
    
    try {
      connection.setAutoCommit(false);
      PreparedStatement statement = connection.prepareStatement(UPDATE);
      statement.setString(1, entity.getName());
      statement.setString(2, entity.getContact());
      statement.setString(3, entity.getPhone());
      statement.setString(4, entity.getEmail());
      statement.setString(5, entity.getAddress());
      statement.setObject(6, entity.getVendorId());
      statement.execute();
      connection.commit();
      statement.close();
    } catch (SQLException ex) {
      try {
        connection.rollback();
      } catch (SQLException sqlex) {
        DatabaseUtils.handleSqlException("VendorDao.update.rollback", sqlex, LOGGER);
      }
      DatabaseUtils.handleSqlException("VendorDao.update", ex, LOGGER);
    }
    Optional<Vendor> vendor = getById(entity.getVendorId());
    return vendor.isPresent() ? vendor.get() : entity;
  }
  
  //-------------------------------------------------------------------------------------------------------------------
  
  @Override
  public void delete(UUID id) {
    Connection connection = DatabaseUtils.getConnection();
    
    try {
      connection.setAutoCommit(false);
      PreparedStatement statement = connection.prepareStatement(DELETE);
      statement.setObject(1, id);
      statement.execute();
      connection.commit();
      statement.close();
    } catch (SQLException ex) {
      try {
        connection.rollback();
      } catch (SQLException sqlex) {
        DatabaseUtils.handleSqlException("VendorDao.delete.rollback", sqlex, LOGGER);
      }
      DatabaseUtils.handleSqlException("VendorDao.delete", ex, LOGGER);
    }
  }
  
  //-------------------------------------------------------------------------------------------------------------------
  
  @Override
  public Optional<Vendor> getById(UUID id) {
    Connection connection = DatabaseUtils.getConnection();
    
    try (PreparedStatement statement = connection.prepareStatement(GET_BY_ID)) {
      statement.setObject(1, id);
      ResultSet rs = statement.executeQuery();
      if (rs.next()) {
        Vendor vendor = new Vendor();
        vendor.setVendorId(id);
        vendor.setName(rs.getString("name"));
        vendor.setContact(rs.getString("contact"));
        vendor.setEmail(rs.getString("email"));
        vendor.setPhone(rs.getString("phone"));
        vendor.setAddress(rs.getString("address"));
        return Optional.of(vendor);
      }
      rs.close();
    } catch (SQLException ex) {
      DatabaseUtils.handleSqlException("VendorDao.getById", ex, LOGGER);
    }
    return Optional.empty();
  }
  
  //-------------------------------------------------------------------------------------------------------------------
  
  private List<Vendor> processResultSet(ResultSet rs) throws SQLException {
    List<Vendor> vendors = new ArrayList<>();
    
    while (rs.next()) {
      Vendor vendor = new Vendor();
      vendor.setVendorId((UUID) rs.getObject("vendor_id"));
      vendor.setName(rs.getString("name"));
      vendor.setContact(rs.getString("contact"));
      vendor.setPhone(rs.getString("phone"));
      vendor.setEmail(rs.getString("email"));
      vendor.setAddress(rs.getString("address"));
      vendors.add(vendor);
    }
    return vendors;
  }
  
  //-------------------------------------------------------------------------------------------------------------------
  
}
