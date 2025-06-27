package com.linkedin.data.dao;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface Dao<T, Id extends UUID> {
  
  List<T> getAll();
  
  T create(T entity);
  
  T update(T entity);
  
  void delete(Id id);
  
  Optional<T> getById(Id id);
  
}
