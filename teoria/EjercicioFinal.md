# **Implementación de un Sistema de Procesamiento de Datos de Bolsa con Kafka y Kafka Streams**

## **Descripción General**
En este ejercicio, se desarrollará un **sistema distribuido de procesamiento de datos de bolsa** utilizando **Apache Kafka y Kafka Streams**.

- Se desarrollará una **API REST** en Java que reciba **operaciones de bolsa** y las publique en un **topic de Kafka**.
- Se creará un **proceso de Kafka Streams** que consuma los datos del **topic** y realice **distintas transformaciones**.
- Se enviarán los datos procesados a **diferentes topics**, incluyendo:
    - `notificaciones-bolsa`: Para alertas si el precio supera un umbral.
    - `dashboard-bolsa`: Para datos filtrados utilizados en una UI.
    - `estadisticas-bolsa`: Para agrupar datos en ventanas temporales.

---

## **Parte 1: API REST para Enviar Mensajes a Kafka**

### **Objetivo:**
Implementar un **servicio REST en Spring Boot** que permita enviar **operaciones de bolsa** a Kafka.

### **Requerimientos**:
- La API deberá exponer un endpoint `POST /acciones` que reciba una operación en JSON con los siguientes campos:

```json
{
    "accion": "AAPL",
    "precio": 175.23,
    "cantidad": 50,
    "tipo": "COMPRA",
    "timestamp": "2025-02-06T10:15:30Z"
}
```

- Publicar la operación en un topic de Kafka llamado **"operaciones-bolsa"**.



---

## **Parte 2: Kafka Streams para Procesamiento de Datos de Bolsa**

### **Objetivo:**
Crear una aplicación **Kafka Streams** en Java que procese los datos de bolsa y los dirija a diferentes topics.

**Transformación 1: Filtrado de Operaciones para Notificaciones**
- Consumir mensajes del topic operaciones-bolsa.
- Filtrar aquellos mensajes en los que el precio de la acción sea mayor a $180. 
- Enviar los mensajes filtrados al topic notificaciones-bolsa.

**Transformación 2: Enviar Datos Filtrados al Dashboard**
- Consumir mensajes del topic operaciones-bolsa.
- Transformar los datos para que contengan solo la información relevante para el dashboard (por ejemplo, acción, precio y cantidad).
- Enviar los datos procesados al topic dashboard-bolsa.

**Transformación 3: Cálculo de Estadísticas en Ventanas Temporales**
- Consumir mensajes del topic operaciones-bolsa.
- Agrupar los datos por clave (acción bursátil).
- Aplicar una ventana de tiempo de 15 minutos para calcular métricas como el número de transacciones, el precio promedio, el precio mínimo y el precio máximo.
- Enviar los datos agregados al topic estadisticas-bolsa.
- Puedes utilizar el método actualizar de la siguiente clase como agregador

```java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstadisticaBolsa {
    private long transacciones;
    private double promedio;
    private double min;
    private double max;

    public EstadisticaBolsa actualizar(AccionMessage accion) {
        this.transacciones++;
        this.promedio = (this.promedio * (this.transacciones - 1) + accion.getPrecio()) / this.transacciones;
        this.min = Math.min(this.min, accion.getPrecio());
        this.max = Math.max(this.max, accion.getPrecio());
        return this;
    }
}
```
---

## **Parte 3: Despliegue y Pruebas**

### **Prueba de Envío de Mensajes**
- Enviar mensajes con Postman al endpoint:
```http
POST http://localhost:8080/acciones
Content-Type: application/json
Body:
{
    "accion": "AAPL",
    "precio": 190.5,
    "cantidad": 20,
    "tipo": "COMPRA",
    "timestamp": "2025-02-06T10:15:30Z"
}
```
- Verificar que los mensajes se publican correctamente en Kafka.

### **Prueba de Kafka Streams**
- Consumir mensajes de los topics generados:
```sh
bin/kafka-console-consumer.sh --topic notificaciones-bolsa --bootstrap-server localhost:9092 --from-beginning
bin/kafka-console-consumer.sh --topic dashboard-bolsa --bootstrap-server localhost:9092 --from-beginning
bin/kafka-console-consumer.sh --topic estadisticas-bolsa --bootstrap-server localhost:9092 --from-beginning
```