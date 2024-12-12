package com.imagina.core_consumer.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
<<<<<<< HEAD

=======
//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
>>>>>>> a09dc005118e511cf2c2358a8575f9f9500c7bae

@Configuration
public class JsonConfig {

    @Bean
<<<<<<< HEAD
    public ObjectMapper objectMapper(){
        ObjectMapper objectMapper = new ObjectMapper();
=======
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        //objectMapper.registerModule(new JavaTimeModule());
        //objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
>>>>>>> a09dc005118e511cf2c2358a8575f9f9500c7bae
        return objectMapper;
    }
}
