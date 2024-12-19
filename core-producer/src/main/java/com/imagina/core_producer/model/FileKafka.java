package com.imagina.core_producer.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileKafka {

    private String name;
    private long size;
    private String extension;

}