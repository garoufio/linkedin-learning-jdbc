package com.linkedin;

import java.awt.print.Book;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Stream;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.yaml.snakeyaml.Yaml;

public class AppProperties {
  
  private static final String DEFAULT_PROPERTIES_FILE = "application.yaml";
  
  //-------------------------------------------------------------------------------------------------------------------
  
  public Properties loadProperties(String filename) {
    if (filename == null) {
      filename = DEFAULT_PROPERTIES_FILE;
    }
    
    try (InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(filename)) {
      Yaml yaml = new Yaml();
      Map<String, Object> map = (Map<String, Object>) yaml.load(inputStream);
      map.forEach((k,v) -> {
        //System.out.printf("key: %s - value: %s\n", k, v);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.convertValue(v, JsonNode.class);
        
        objectMapper.reader().forType(new TypeReference<List<String>>() {});
        List<Map<String, String>> list = objectMapper.convertValue(jsonNode, new TypeReference<List<Map<String, String>>>() {});
        
        System.out.printf("Key: %s\n", k);
        jsonNode.forEach(System.out::println);
      });
      

    } catch (IOException ex) {
      ex.printStackTrace();
    }
    
    //return yaml.load(inputStream);
    return null;
  }
  
  //-------------------------------------------------------------------------------------------------------------------
  
}
