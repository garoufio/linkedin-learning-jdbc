package com.linkedin.data.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

public class DatabaseUtils {

  private static final String URL = "jdbc:postgresql://localhost:5432/postgres";
  private static final String USERNAME = "postgres";
  private static final String PASSWORD = "p@ssw0rd";
  private static final Logger LOGGER = Logger.getLogger(DatabaseUtils.class.getName());
  private static final String EXCEPTION_FORMAT = "exception in %s, message: %s, code: %s";
  private static Connection connection;
  
  //-------------------------------------------------------------------------------------------------------------------
  
  public static Connection getConnection() {
    if (connection == null) {
      synchronized (DatabaseUtils.class) {
        if (connection == null) {
          try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
          } catch (SQLException ex) {
            handleSqlException("DatabaseUtils.getConnection", ex, LOGGER);
          }
        }
      }
    }
    return connection;
  }
  
  //-------------------------------------------------------------------------------------------------------------------
  
  public static void closeConnection() {
    if (connection != null) {
      try {
        connection.close();
      } catch (SQLException ex) {
        handleSqlException("DatabaseUtils.closeConnection", ex, LOGGER);
      }
    }
  }
  
  //-------------------------------------------------------------------------------------------------------------------
  
  public static void handleSqlException(String method, SQLException ex, Logger logger) {
    logger.warning(String.format(EXCEPTION_FORMAT, method, ex.getMessage(), ex.getErrorCode()));
    throw new RuntimeException(ex);
  }
  
  //-------------------------------------------------------------------------------------------------------------------

}
