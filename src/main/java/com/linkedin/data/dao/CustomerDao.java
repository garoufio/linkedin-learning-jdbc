package com.linkedin.data.dao;

import com.linkedin.data.entity.Customer;
import com.linkedin.data.util.DatabaseUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;

public class CustomerDao implements Dao<Customer, UUID> {
  
  private static final Logger LOGGER = Logger.getLogger(CustomerDao.class.getName());
  private static final String GET_ALL = "SELECT customer_id, first_name, last_name, email, phone, address " +
      "FROM wisdom.customers";
  private static final String GET_BY_ID = "SELECT customer_id, first_name, last_name, email, phone, address " +
      "from wisdom.customers where customer_id = ?";
  private static final String DELETE = "DELETE FROM wisdom.customers WHERE customer_id = ?";
  private static final String CREATE =  "INSERT INTO wisdom.customers " +
      "(customer_id, first_name, last_name, email, phone, address) VALUES (?, ?, ?, ?, ?, ?)";
  private static final String UPDATE = "UPDATE wisdom.customers " +
        "SET first_name = ?, last_name = ?, email = ?, phone = ?, address = ? " +
      "WHERE customer_id = ?";
  
  //-------------------------------------------------------------------------------------------------------------------
  
  @Override
  public List<Customer> getAll() {
    List<Customer> customers = null;
    Connection connection = DatabaseUtils.getConnection();
    
    try (Statement statement = connection.createStatement();) {
      ResultSet resultSet = statement.executeQuery(GET_ALL);
      customers = processResultSet(resultSet);
    } catch (SQLException ex) {
      DatabaseUtils.handleSqlException("CustomerDao.getAll", ex, LOGGER);
    }
    return customers == null ? new ArrayList<>() : customers;
  }
  
  //-------------------------------------------------------------------------------------------------------------------
  
  @Override
  public Customer create(Customer entity) {
    UUID uuid = UUID.randomUUID();
    Connection connection = DatabaseUtils.getConnection();
    
    try {
      connection.setAutoCommit(false);
      PreparedStatement statement = connection.prepareStatement(CREATE);
      statement.setObject(1, uuid);
      statement.setString(2, entity.getFirstname());
      statement.setString(3, entity.getLastname());
      statement.setString(4, entity.getEmail());
      statement.setString(5, entity.getPhone());
      statement.setString(6, entity.getAddress());
      statement.execute();
      connection.commit();
      statement.close();
    } catch (SQLException ex) {
      try {
        connection.rollback();
      } catch (SQLException sqlex) {
        DatabaseUtils.handleSqlException("CustomerDao.create.rollback", ex, LOGGER);
      }
      DatabaseUtils.handleSqlException("CustomerDao.create", ex, LOGGER);
    }
    Optional<Customer> customer = getById(uuid);
    return customer.orElse(null);
  }
  
  //-------------------------------------------------------------------------------------------------------------------
  
  @Override
  public Customer update(Customer entity) {
    Connection connection = DatabaseUtils.getConnection();
    
    try {
      connection.setAutoCommit(false);
      PreparedStatement statement = connection.prepareStatement(UPDATE);
      statement.setObject(1, entity.getFirstname());
      statement.setObject(2, entity.getLastname());
      statement.setObject(3, entity.getEmail());
      statement.setObject(4, entity.getPhone());
      statement.setObject(5, entity.getAddress());
      statement.setObject(6, entity.getCustomerId());
      statement.execute();
      connection.commit();
      statement.close();
    } catch (SQLException ex) {
      try {
        connection.rollback();
      }
      catch (SQLException sqlex) {
        DatabaseUtils.handleSqlException("CustomerDao.update.rollback", sqlex, LOGGER);
      }
      DatabaseUtils.handleSqlException("CustomerDao.update", ex, LOGGER);
    }
    Optional<Customer> customerOptional = getById(entity.getCustomerId());
    return customerOptional.isPresent() ? customerOptional.get() : entity;
  }
  
  //-------------------------------------------------------------------------------------------------------------------
  
  @Override
  public void delete(UUID uuid) {
    Connection connection = DatabaseUtils.getConnection();
    
    try  {
      connection.setAutoCommit(false);
      PreparedStatement statement = connection.prepareStatement(DELETE);
      statement.setObject(1, uuid);
      statement.execute();
      connection.commit();
      statement.close();
    } catch (SQLException ex) {
      try {
        connection.rollback();
      } catch (SQLException sqlex) {
        DatabaseUtils.handleSqlException("CustomerDao.delete.rollback", sqlex, LOGGER);
      }
      DatabaseUtils.handleSqlException("CustomerDao.delete", ex, LOGGER);
    }
  }
  
  //-------------------------------------------------------------------------------------------------------------------
  
  @Override
  public Optional<Customer> getById(UUID uuid) {
    Connection connection = DatabaseUtils.getConnection();
    
    try (PreparedStatement statement = connection.prepareStatement(GET_BY_ID)) {
      statement.setObject(1, uuid);
      ResultSet resultSet = statement.executeQuery();
      if (resultSet.next()) {
        Customer customer = new Customer();
        customer.setCustomerId(uuid);
        customer.setFirstname(resultSet.getString("first_name"));
        customer.setLastname(resultSet.getString("last_name"));
        customer.setEmail(resultSet.getString("email"));
        customer.setPhone(resultSet.getString("phone"));
        customer.setAddress(resultSet.getString("address"));
        return Optional.of(customer);
      }
    } catch (SQLException ex) {
      DatabaseUtils.handleSqlException("CustomerDao.getById", ex, LOGGER);
    }
    return Optional.empty();
  }
  
  //-------------------------------------------------------------------------------------------------------------------
  
  private List<Customer> processResultSet(ResultSet rs) throws SQLException {
    List<Customer> customers = new ArrayList<>();
    
    while (rs.next()) {
      Customer customer = new Customer();
      customer.setCustomerId((UUID) rs.getObject("customer_id"));
      customer.setFirstname(rs.getString("first_name"));
      customer.setLastname(rs.getString("last_name"));
      customer.setEmail(rs.getString("email"));
      customer.setPhone(rs.getString("phone"));
      customer.setAddress(rs.getString("address"));
      customers.add(customer);
    }
    return customers;
  }
  
  //-------------------------------------------------------------------------------------------------------------------
  
}
