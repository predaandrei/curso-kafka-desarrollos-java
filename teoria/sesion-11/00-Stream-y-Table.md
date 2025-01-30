## KStream

- La clase KStream representa el flujo en Kafka Stream.
- Es una secuencia ordenada de mensajes, similar un topic de Kafka.
- No tiene límites, lo que significa que no tiene extremos. Los datos seguirán llegando al flujo mientras haya alguno.
- Todos los datos se insertan en un flujo, y ningún dato se actualiza ni se elimina. 

## KTable

- La tabla en Kafka Stream se representa en la clase KTable.
- Una tabla también es ilimitada, pero tiene un comportamiento diferente.
- Un KTable inserta o actualiza un valor, dependiendo de la clave.
- Una Ktable también puede borrar datos si el valor dado es null.
- La analogía es una tabla de base de datos, donde la clave Kafka es la clave primaria de la tabla.
- La analogía de la tabla sólo tiene otra columna, el valor Kafka.
- Sin embargo, si el valor es nulo, borrará el registro.


## Ejemplo de flujo de datos 

### Entrada

```
{"orderId": "1", "customerId": "C1", "amount": 100}
{"orderId": "2", "customerId": "C2", "amount": 200}
{"orderId": "3", "customerId": "C1", "amount": 150}
{"orderId": "4", "customerId": "C3", "amount": 300}
{"orderId": "1", "customerId": "C1", "amount": 250}  // Actualización de la orden 1
```

### KStream

```java
KStream<String, Order> ordersStream = builder.stream("orders", Consumed.with(Serdes.String(), orderSerde));
ordersStream.to("processed-orders", Produced.with(Serdes.String(), orderSerde));
```

Salida

```
{"orderId": "1", "customerId": "C1", "amount": 100}
{"orderId": "2", "customerId": "C2", "amount": 200}
{"orderId": "3", "customerId": "C1", "amount": 150}
{"orderId": "4", "customerId": "C3", "amount": 300}
{"orderId": "1", "customerId": "C1", "amount": 250}  // No reemplaza, es un evento nuevo
```

- Cada mensaje es un evento independiente.
- Las actualizaciones (misma clave, orderId=1) no sobrescriben valores anteriores.
- Ideal para eventos de secuencia, logs de eventos, detección de patrones en datos en movimiento.

### KTable

```java
KTable<String, Order> ordersTable = builder.table("orders", Consumed.with(Serdes.String(), orderSerde));
ordersTable.toStream().to("finalized-orders", Produced.with(Serdes.String(), orderSerde));
```

Salida

```
{"orderId": "2", "customerId": "C2", "amount": 200}
{"orderId": "3", "customerId": "C1", "amount": 150}
{"orderId": "4", "customerId": "C3", "amount": 300}
{"orderId": "1", "customerId": "C1", "amount": 250}  // Sobrescribe el mensaje anterior con orderId=1
```

- Cada clave (orderId) almacena solo el último valor recibido.
- La actualización de orderId=1 sobrescribe el mensaje anterior.
- Ideal para estados materializados como catálogos de productos, perfiles de usuario, datos agregados.


## Diferencias clave entre KStream y KTable

| Característica               | KStream                                      | KTable                                       |
|-----------------------------|----------------------------------------------|----------------------------------------------|
| **Tipo de datos**           | Flujo continuo de eventos                   | Vista materializada de los datos (tabla)    |
| **Manejo de claves duplicadas** | Cada mensaje es un evento independiente    | Solo la última clave es retenida (update)   |
| **Persistencia**            | No almacena estado, solo eventos            | Mantiene el último estado para cada clave   |
| **Procesamiento**           | Se usa para eventos en movimiento           | Se usa para representar el estado actual    |
| **Ejemplo en base de datos** | Similar a una tabla de logs                 | Similar a una tabla con claves únicas       |
| **Ejemplo de uso**          | Detección de fraude, monitoreo en tiempo real | Catálogos de productos, perfiles de usuario |

## Conclusión

| Uso                                        | KStream | KTable |
|--------------------------------------------|---------|--------|
| Procesamiento de eventos en tiempo real   | ✅      | ❌     |
| Estado actual y actualización de claves únicas | ❌  | ✅     |
| Registros históricos de eventos           | ✅      | ❌     |
| Reemplazo de valores duplicados           | ❌      | ✅     |

**`KStream` → Eventos independientes.**  
**`KTable` → Estado actualizado con la última versión de cada clave.**
