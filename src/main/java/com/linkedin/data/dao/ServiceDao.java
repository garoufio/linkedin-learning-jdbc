package com.linkedin.data.dao;

import com.linkedin.data.entity.Service;
import com.linkedin.data.util.DatabaseUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;

public class ServiceDao implements Dao<Service, UUID> {
  
  private static final Logger LOGGER = Logger.getLogger(ServiceDao.class.getName());
  private static final String GET_ALL = "SELECT service_id, name, price FROM wisdom.services";
  private static final String GET_BY_ID = "SELECT service_id, name, price FROM wisdom.services WHERE service_id = ?";
  private static final String CREATE = "INSERT INTO wisdom.services (service_id, name, price) VALUES (?, ?, ?)";
  private static final String UPDATE = "UPDATE wisdom.services SET name = ?, price = ? WHERE service_id = ?";
  private static final String DELETE = "DELETE FROM wisdom.services WHERE service_id = ?";
  
  //-------------------------------------------------------------------------------------------------------------------
  
  @Override
  public List<Service> getAll() {
    List<Service> services = null;
    Connection connection = DatabaseUtils.getConnection();
    
    try (Statement statement = connection.createStatement()) {
      ResultSet rs = statement.executeQuery(GET_ALL);
      services = this.processResultSet(rs);
    } catch (SQLException ex) {
      DatabaseUtils.handleSqlException("ServiceDao.getAll", ex, LOGGER);
    }
    return services == null ? new ArrayList<>() : services;
  }
  
  //-------------------------------------------------------------------------------------------------------------------
  
  @Override
  public Service create(Service entity) {
    UUID serviceId = UUID.randomUUID();
    Connection connection = DatabaseUtils.getConnection();
    
    try {
      connection.setAutoCommit(false);
      PreparedStatement statement = connection.prepareStatement(CREATE);
      statement.setObject(1, serviceId);
      statement.setString(2, entity.getName());
      statement.setBigDecimal(3, entity.getPrice());
      statement.execute();
      connection.commit();
      statement.close();
    } catch (SQLException ex) {
      try {
        connection.rollback();
      } catch (SQLException sqlex) {
        DatabaseUtils.handleSqlException("ServiceDao.create.rollback", sqlex, LOGGER);
      }
      DatabaseUtils.handleSqlException("ServiceDao.create", ex, LOGGER);
    }
    Optional<Service> service = this.getById(serviceId);
    return service.isPresent() ? service.get() : null;
  }
  
  //-------------------------------------------------------------------------------------------------------------------
  
  @Override
  public Service update(Service entity) {
    Connection connection = DatabaseUtils.getConnection();
    
    try {
      connection.setAutoCommit(false);
      PreparedStatement statement = connection.prepareStatement(UPDATE);
      statement.setString(1, entity.getName());
      statement.setBigDecimal(2, entity.getPrice());
      statement.setObject(3, entity.getServiceId());
      statement.execute();
      connection.commit();
      statement.close();
    } catch (SQLException ex) {
      try {
        connection.rollback();
      } catch (SQLException sqlex) {
        DatabaseUtils.handleSqlException("ServiceDao.update.rollback", sqlex, LOGGER);
      }
      DatabaseUtils.handleSqlException("ServiceDao.update", ex, LOGGER);
    }
    Optional<Service> service = getById(entity.getServiceId());
    return service.isPresent() ? service.get() : entity;
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
        DatabaseUtils.handleSqlException("ServiceDao.delete.rollback", sqlex, LOGGER);
      }
      DatabaseUtils.handleSqlException("ServiceDao.delete", ex, LOGGER);
    }
  }
  
  //-------------------------------------------------------------------------------------------------------------------
  
  @Override
  public Optional<Service> getById(UUID id) {
    Connection connection = DatabaseUtils.getConnection();
    
    try (PreparedStatement statement = connection.prepareStatement(GET_BY_ID)) {
      statement.setObject(1, id);
      ResultSet rs = statement.executeQuery();
      if (rs.next()) {
        Service service = new Service();
        service.setServiceId(id);
        service.setName(rs.getString("name"));
        service.setPrice(rs.getBigDecimal("price"));
        return Optional.of(service);
      }
    } catch (SQLException ex) {
      DatabaseUtils.handleSqlException("ServiceDao.getById", ex, LOGGER);
    }
    return Optional.empty();
  }
  
  //-------------------------------------------------------------------------------------------------------------------
  
  private List<Service> processResultSet(ResultSet rs) throws SQLException {
    List<Service> services = new ArrayList<>();
    
    while (rs.next()) {
      Service service = new Service();
      service.setServiceId((UUID) rs.getObject("service_id"));
      service.setName(rs.getString("name"));
      service.setPrice(rs.getBigDecimal("price"));
      services.add(service);
    }
    return services;
  }
  
  //-------------------------------------------------------------------------------------------------------------------
  
}
