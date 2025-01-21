## Hello Kafka Stream

1. Vamos a coger los mensajes de `t-commodity-promotion` y los vamos a pasar a Uppercase
2. Vamos a publicar después el resultado en el topic `t-commodity-promotion-uppercase`

### Configurar el archivo application.yml

```yaml
logging:
  pattern:
    console: "[Kafka Stream] %clr(%d{HH:mm:ss.SSS}){faint} %clr(${LOG_LEVEL_PATTERN:%5p}) %clr(---){faint} %clr(%-40.40logger{39}){cyan} %clr(:){faint} %m%n${LOG_EXCEPTION_CONVERSION_WORD:%wEx}"
spring:
  application:
    name: kafka-stream-sample
  main:
    banner-mode: OFF
    log-startup-info: false
  kafka:
    listener:
      missing-topics-fatal: false
    consumer:
      group-id: kafka-stream-cg
      enable-auto-commit: true
      auto-offset-reset: earliest
      bootstrap-servers:
        - localhost:9092
      key-deserializer: org.apache.kafka.common.serialization.StringDeserializer
      value-deserializer: org.springframework.kafka.support.serializer.JsonDeserializer
      properties:
        spring:
          json:
            trusted:
              packages: com.course.kafka.broker.message
```

### Crear la clase `broker.stream.promotion.PromotionUppercaseStream.java`

- Anotar la clase con `@Configuration`

```java
@Configuration
public class PromotionUppercaseStream {

    @Bean
    KStream<String, String> kstreamPromotionUppercase(StreamsBuilder builder) {
        KStream<String, String> sourceStream = builder.stream("t-commodity-promotion",
                Consumed.with(Serdes.String(), Serdes.String()));

        KStream<String, String> uppercaseStream = sourceStream.mapValues(promotion -> promotion.toUpperCase());

        uppercaseStream.to("t-commodity-promotion-uppercase");

        // util para depuración pero mejor no usarlo en producción
        sourceStream.print(Printed.<String, String>toSysOut().withLabel("Original Stream"));
        uppercaseStream.print(Printed.<String, String>toSysOut().withLabel("Uppercase Stream"));

        return sourceStream;
    }
}
```

- Forma alternativa de crear un stream

```java
@Component
public class PromotionUppercaseStream {

    @Autowired
    void kstreamPromotionUppercase(StreamsBuilder builder) {
        KStream<String, String> sourceStream = builder.stream("t-commodity-promotion",
                Consumed.with(Serdes.String(), Serdes.String()));

        KStream<String, String> uppercaseStream = sourceStream.mapValues(promotion -> promotion.toUpperCase());

        uppercaseStream.to("t-commodity-promotion-uppercase");

        // util para depuración pero mejor no usarlo en producción
        sourceStream.print(Printed.<String, String>toSysOut().withLabel("Original Stream"));
        uppercaseStream.print(Printed.<String, String>toSysOut().withLabel("Uppercase Stream"));
    }
}
```

### Crear una clse de configuración `config.KafkaStreamConfig.java`

- Anotar la clase con `@Configuration` y `@EnableKafkaStreams`

```java
@Configuration
@EnableKafkaStreams
public class KafkaStreamConfig {

    @Bean(name = KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_CONFIG_BEAN_NAME)
    KafkaStreamsConfiguration kafkaStreamsConfiguration() {
        
        var props = new java.util.HashMap<String, Object>();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "kafka-stream-" + System.currentTimeMillis());
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());

        return new KafkaStreamsConfiguration(props);
    }
}
```

- También se puede configurar KafkaStream en el archivo `application.yml`

```yaml
spring:
  kafka:
    streams:
      application-id: kafka-stream-sample-${random.uuid}
      bootstrap-servers:
        - localhost:9092
      properties:
        default:
          key:
            serde: org.apache.kafka.common.serialization.Serdes$StringSerde
          value:
            serde: org.apache.kafka.common.serialization.Serdes$StringSerde
        commit:
          interval:
            ms: 3000
        processing:
          guarantee: exactly_once_v2
```

### Probar la aplicacion

- Levanta la aplicación `kafka-stream-order`
- Levanta la aplicación `kafka-stream-sample`
- Crea varias promociones usando postman

Los mensajes son publicados a un nuevo topic que puede ser escuchado como cualquier otro.

### Escuchar los mensajes del nuevo topic

- Crea la clase `broker.consumer.PromotionUppercaseListener.java` en la aplicación `kafka-stream-storage` 

```java
import com.imagina.kafka.broker.message.PromotionMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PromotionUppercaseListener {

    @KafkaListener(topics = "t-commodity-promotion-uppercase")
    public void listenPromotion(PromotionMessage message) {
        log.info("Processing uppercase promotion: {}", message);
    }

}
```

Si ejecutamos la aplicación `kafka-stream-storage` nos daremos cuenta de que los mensajes consumidos tienen un valor nulo

`Processing uppercase promotion: PromotionMessage{promotionCode='null'}`

Esto es así porque estamos usando String Serde y por lo tanto estamos transformando en Uppercase tanto el nombre del campo
como el valor, por lo tanto al deserializarlo como un objeto PromotionMessage no se reconoce el campo por estar en uppercase.

## Promotion Uppercase Stream con String Serde

### Crea una clase de configuración para Json

```java
@Configuration
public class JsonConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return objectMapper;
    }
}
```

### Crea una nueva clase `broker.stream.promotion.PromotionUppercaseJsonStream.java`

```java
@Component
@Slf4j
public class PromotionUppercaseJsonStream {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    void kstreamPromotionUppercase(StreamsBuilder builder) {
        var sourceStream = builder.stream("t-commodity-promotion", Consumed.with(Serdes.String(), Serdes.String()));
        var uppercaseStream = sourceStream.mapValues(this::uppercasePromotionCode);

        uppercaseStream.to("t-commodity-promotion-uppercase");

        sourceStream.print(Printed.<String, String>toSysOut().withLabel("JSON Original Stream"));
        uppercaseStream.print(Printed.<String, String>toSysOut().withLabel("JSON Uppercase Stream"));
    }

    private String uppercasePromotionCode(String jsonString) {
        try {
            var promotion = objectMapper.readValue(jsonString, PromotionMessage.class);
            promotion.setPromotionCode(promotion.getPromotionCode().toUpperCase());
            return objectMapper.writeValueAsString(promotion);
        } catch (Exception e) {
            log.warn("Unable to process JSON", e);
            return "";
        }
    }

}
```

Si ahora haces la prueba anterior, verás que los mensajes y son apropiadamente consumidos por el `PromotionConsumer`
del proyecto `kafka-stream-storage`

`Processing uppercase promotion: PromotionMessage{promotionCode='SMALL900'}`

## Promotion Uppercase Stream con JSON Serde

### Crea la clase `broker.stream.promotion.PromotionUppercaseSpringJsonStream.java`

```java
@Component
public class PromotionUppercaseSpringJsonStream {

    @Autowired
    void kstreamPromotionUppercase(StreamsBuilder builder) {
        var jsonSerde = new JsonSerde<>(PromotionMessage.class);
        var sourceStream = builder.stream("t-commodity-promotion", Consumed.with(Serdes.String(), jsonSerde));
        var uppercaseStream = sourceStream.mapValues(this::uppercasePromotionCode);

        uppercaseStream.to("t-commodity-promotion-uppercase", Produced.with(Serdes.String(), jsonSerde));

        sourceStream.print(Printed.<String, PromotionMessage>toSysOut().withLabel("JSON Serde Original Stream"));
        uppercaseStream.print(Printed.<String, PromotionMessage>toSysOut().withLabel("JSON Serde Uppercase Stream"));
    }

    private PromotionMessage uppercasePromotionCode(PromotionMessage message) {
        return new PromotionMessage(message.getPromotionCode().toUpperCase());
    }
}
```