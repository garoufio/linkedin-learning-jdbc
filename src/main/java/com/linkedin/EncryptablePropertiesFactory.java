package com.linkedin;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.jasypt.properties.EncryptableProperties;
import java.util.Properties;

@EnableEncryptableProperties
public class EncryptablePropertiesFactory {
  
  public static Properties create() {
    SimpleStringPBEConfig  config = new SimpleStringPBEConfig();
    config.setPassword(System.getenv("LOCAL_ENCRYPTOR_PASSWORD"));
    config.setAlgorithm("PBEWithMD5AndDES");
    config.setKeyObtentionIterations("1000");
    config.setProviderName("SunJCE");
    config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
    config.setIvGeneratorClassName("org.jasypt.iv.RandomIvGenerator");
    config.setStringOutputType("base64");
    
    StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
    encryptor.setConfig(config);
    
    Properties props = new EncryptableProperties(encryptor);
    return props;
  }

}
