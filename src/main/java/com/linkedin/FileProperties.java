package com.linkedin;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class FileProperties {
  
  private Map<String, Map<Map, List<String>>> table;
  
}
