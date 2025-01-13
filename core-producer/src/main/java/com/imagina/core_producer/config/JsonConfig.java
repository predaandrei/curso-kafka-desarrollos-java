package com.imagina.core_producer.config;

import com.fasterxml.jackson.databind.ObjectMapper;
<<<<<<< HEAD
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.fasterxml.jackson.databind.SerializationFeature;
=======
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
>>>>>>> b78ec5fd80814d2ffe832fa028c69ebc9da60728

@Configuration
public class JsonConfig {

    @Bean
    public ObjectMapper objectMapper(){
        ObjectMapper objectMapper = new ObjectMapper();
<<<<<<< HEAD
=======
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
>>>>>>> b78ec5fd80814d2ffe832fa028c69ebc9da60728
        return objectMapper;
    }
}
