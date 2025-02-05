# Operaciones Kafka Streams

## Tabla de Contenido
- [branch](#branch) *(Stateless)*
- [filter](#filter) *(Stateless)*
- [filterNot](#filternot) *(Stateless)*
- [mapValues](#mapvalues) *(Stateless)*
- [map](#map) *(Stateless)*
- [flatMap](#flatmap) *(Stateless)*
- [merge](#merge) *(Stateless)*
- [join](#join) *(Stateful)*
- [groupBy](#groupby) *(Stateful)*
- [groupByKey](#groupbykey) *(Stateful)*
- [aggregate](#aggregate) *(Stateful)*
- [cogroup](#cogroup) *(Stateful)*

<img src="img/kafkaStreamsOperations.png" alt="Operaciones kafka streams" />

## branch

- Divide un stream dependiendo de los predicados aplicados
- Evalúa los predicados en orden
- Un record solo sera matcheado una vez y si no coincide con ningún predicado es descartado
- Devuelve un array de stream
- Se trata de una operación intermedia
- Devuelve un KStream

```java
var arrayStream = stream.branch(
        (k, v) -> v > 100,
        (k, v) -> v > 20,
        (k, v) -> v > 10
)
```

Entrada
```
arrayStream[{A, 26}, {B, 120}, {C, 4}, {D, 15}]
```
Salida
```
arrayStream[0][{B, 120}]
arrayStream[1][{A, 26}]
arrayStream[2][{D, 15}]
```

---

## filter
- Filtra los registros en función de un predicado.
- Los registros que no cumplan con el predicado serán descartados.
- Se trata de una operación intermedia.
- Devuelve un KStream.

```java
var filteredStream = stream.filter((k, v) -> v > 100);
```

Entrada
```
[{A, 50}, {B, 120}, {C, 90}]
```

Salida
```
[{B, 120}]
```
---
## filterNot
- Filtra los registros que no cumplan con un predicado.
- Los registros que cumplan con el predicado serán descartados.
- Se trata de una operación intermedia.
- Devuelve un KStream.

```java
var filteredStream = stream.filterNot((k, v) -> v > 100);
```

Entrada
```
[{A, 50}, {B, 120}, {C, 90}]
```

Salida

```
[{A, 50}, {C, 90}]
```
---

## mapValues
- Transforma el valor de cada registro usando una función.
- No afecta a las claves del stream.
- Se trata de una operación intermedia.
- Devuelve un KStream.

```java
var mappedStream = stream.mapValues(v -> v.toUpperCase());
```

Entrada

```
[{A, "hello"}, {B, "world"}]
```

Salida

```
[{A, "HELLO"}, {B, "WORLD"}]
```


---

## map
- Transforma tanto la clave como el valor de cada registro usando una función.
- Útil para transformar claves y valores simultáneamente.
- Se trata de una operación intermedia.
- Devuelve un KStream.

```java
var mappedStream = stream.map((k, v) -> KeyValue.pair(k.toUpperCase(), v + 10));
```

Entrada

```
[{a, 5}, {b, 20}]
```

Salida

```
[{A, 15}, {B, 30}]
```


---

## flatMap
- Produce cero, uno o más registros para cada entrada.
- Cada registro se transforma en uno o varios registros usando una función.
- Se trata de una operación intermedia.
- Devuelve un KStream.

```java
var flatMappedStream = stream.flatMap((k, v) -> List.of(
KeyValue.pair(k, v + 1),
KeyValue.pair(k, v + 2)
).stream());
```

Entrada

```
[{A, 5}]
```

Salida

```
[{A, 6}, {A, 7}]
```

---

## merge
- Combina dos o más streams en un único stream.
- Los registros de todos los streams se envían al stream resultante.
- Se trata de una operación intermedia.
- Devuelve un KStream.

```java
var mergedStream = stream1.merge(stream2);
```

Entrada

```
Stream 1: [{A, 10}, {B, 20}]
Stream 2: [{C, 30}]
```

Salida

```
[{A, 10}, {B, 20}, {C, 30}]
```

---

## join

- Combina dos KStreams en función de una clave común.
- Requiere una ventana de tiempo para la operación (en caso de joins con streams).
- Se trata de una operación stateful.
- Devuelve un KStream.

```java
var joinedStream = stream1.join(
stream2,
(v1, v2) -> v1 + "-" + v2,
JoinWindows.of(Duration.ofSeconds(5))
);
```

Entrada

```
Stream 1: [{A, "foo"}, {B, "bar"}]
Stream 2: [{A, "baz"}]
```

Salida

```
[{A, "foo-baz"}]
```


---

## groupBy
- Agrupa los registros de un stream por una nueva clave generada a partir del valor original.
- Útil para redistribuir los datos antes de una agregación.
- Se trata de una operación intermedia.
- Devuelve un KGroupedStream.

```java
var groupedStream = stream.groupBy((key, value) -> KeyValue.pair(value, value));
```

Entrada
```
[
    {"C1": "Laptop"},
    {"C2": "Tablet"},
    {"C3": "Laptop"},
    {"C4": "Laptop"},
    {"C5": "Tablet"}
]
```

Salida (Ejemplo de redistribución de clave)

Si la nueva clave es el valor numérico modificado, por ejemplo, el módulo 2 del valor:

```
{
    "Laptop": ["Laptop", "Laptop", "Laptop"],
    "Tablet": ["Tablet", "Tablet"]
}
```

---

## groupByKey

- Agrupa los registros de un stream por sus claves.
- Útil para agregaciones.
- Se trata de una operación intermedia.
- Devuelve un KGroupedStream.

```java
var groupedStream = stream.groupByKey();
```

Entrada

```
[{A, 10}, {A, 20}, {B, 30}]
```

Salida

```
[{A -> [10, 20]}, {B -> [30]}]
```

---

## aggregate

- Aplica una operación de agregación sobre un KGroupedStream.
- Acumula valores en función de una clave.
- Se trata de una operación stateful.
- Devuelve un KTable.

```java
var aggregatedTable = groupedStream.aggregate(
() -> 0,
(key, value, agg) -> agg + value
);
```

Entrada

```
Grouped Stream: [{A -> [10, 20]}, {B -> [30]}]
```

Salida

```
[{A, 30}, {B, 30}]
```

---

## cogroup

- Combina múltiples `KGroupedStream` en un único proceso de agregación
- Útil para realizar agregaciones complejas entre múltiples streams agrupados
- Se trata de una operación stateful
- Devuevle un `KTable`

```java
var cogroupedTable = groupedStream1.cogroup((key, value, agg) -> agg + value)
    .cogroup(groupedStream2, (key, value, agg) -> agg * value)
    .cogroup(groupedStream3, (key, value, agg) -> agg - value)
    .aggregate(() -> 0);
```

Entrada

```
Grouped Stream 1: [{A -> [10]}, {B -> [20]}]
Grouped Stream 2: [{A -> [2]}, {B -> [3]}]
Grouped Stream 3: [{A -> [5]}, {B -> [10]}]
```

Salida

```
[{A, 15}, {B, 50}]
```

---

## windowedBy
- Aplica una ventana de tiempo a un `KGroupedStream` o `KStream`, permitiendo agrupar eventos dentro de una ventana temporal.
- Útil para cálculos basados en tiempo, como agregaciones y análisis de tendencias.
- Se trata de una operación **stateful**.
- Devuelve un `KGroupedStream` con ventanas de tiempo.

```java
var windowedStream = stream.groupByKey()
    .windowedBy(TimeWindows.ofSizeWithNoGrace(Duration.ofMinutes(10)));
```

### Tipos de Ventanas Soportadas
- TimeWindows: Ventanas de duración fija sin superposición.
- SlidingWindows: Ventanas superpuestas que permiten capturar eventos en diferentes intervalos.
- SessionWindows: Ventanas basadas en actividad, se cierran tras un periodo de inactividad.

**Ejemplo de Uso con Agregación**

```java
var aggregatedStream = stream.groupByKey()
.windowedBy(TimeWindows.ofSizeWithNoGrace(Duration.ofMinutes(10)))
.aggregate(
() -> 0, // Valor inicial
(key, value, agg) -> agg + value, // Lógica de agregación
Materialized.with(Serdes.String(), Serdes.Integer())
);
```
Entrada
```
[{A, 10, "10:00"}, {A, 15, "10:05"}, {A, 20, "10:12"}, {B, 30, "10:07"}]
```

Salida (Ejemplo con ventana de 10 minutos)
```
[{A, 25} (10:00 - 10:10)], [{A, 20} (10:10 - 10:20)], [{B, 30} (10:00 - 10:10)]
```

### ¿Cómo se estructuran los datos en Kafka Streams con windowedBy?

Entrada original (Mensajes sin ventana temporal):
```
[{A, 10, "10:00"}, {A, 15, "10:05"}, {A, 20, "10:12"}, {B, 30, "10:07"}]
```
Cada mensaje contiene:
- Clave (A, B): Representa el identificador del grupo.
- Valor (10, 15, etc.): El dato a procesar.
- Timestamp ("10:00", "10:05"): Indica cuándo ocurrió el evento.

- Kafka Streams agrupa los datos en intervalos de 10 minutos.
- Todos los eventos dentro del rango 10:00 - 10:10 se procesan juntos.
- Luego, se crea otra ventana 10:10 - 10:20, y así sucesivamente.
 
Salida generada por la agregación en ventanas:
```
[{A, 25} (10:00 - 10:10)], [{A, 20} (10:10 - 10:20)], [{B, 30} (10:00 - 10:10)]
```

- En la ventana 10:00 - 10:10, los valores 10 + 15 se suman, generando {A, 25}.
- En la ventana 10:10 - 10:20, solo hay un mensaje {A, 20}.
- Para la clave B, solo hay un mensaje en la ventana 10:00 - 10:10 ({B, 30}).

Cuando usas `windowedBy` en Kafka Streams, el mensaje final no solo contiene la clave y el valor, sino también la ventana de tiempo a la que pertenece el mensaje. Esto sucede porque Kafka Streams usa una estructura especial llamada Windowed<K>, donde K es la clave original y la ventana de tiempo es un atributo adicional.

```java
KTable<Windowed<String>, Long> estadisticas = builder.stream("operaciones-bolsa", Consumed.with(Serdes.String(), Serdes.Long()))
.groupByKey()
.windowedBy(TimeWindows.ofSizeWithNoGrace(Duration.ofMinutes(10)))
.count();
```

Salida esperada (estructura del mensaje final en Kafka):
```
[{(A, 10:00 - 10:10), 2}]
[{(A, 10:10 - 10:20), 1}]
[{(B, 10:00 - 10:10), 1}]
```

- (A, 10:00 - 10:10) → Clave A con ventana 10:00 - 10:10.
- 2 → Número de mensajes con clave A dentro de esa ventana.
- Kafka Streams utiliza la clave original (A, B, etc.) y le añade la información de ventana de tiempo.
Si imprimes la clave, verás algo como:
```java
System.out.println("Clave: " + windowedKey.key() + " | Ventana: " + windowedKey.window().start() + " - " + windowedKey.window().end());
```
```
Clave: A | Ventana: 10:00 - 10:10
Clave: A | Ventana: 10:10 - 10:20
Clave: B | Ventana: 10:00 - 10:10
```
---

## peek

- No modifica los datos: peek() permite visualizar los registros sin alterarlos.
- No filtra ni transforma los registros, a diferencia de map() o filter().
- Se usa generalmente para logging o debugging.
- Es una operación intermedia: No cambia el flujo de Kafka Streams.

```java
KStream<String, String> stream = builder.stream("t-input-topic");
stream.peek((key, value) -> System.out.println("Mensaje recibido -> Clave: " + key + ", Valor: " + value))
        .to("t-output-topic");
```

Entrada
```
{"clave": "AAPL", "valor": "200"}
```

La consola imprimirá:
```
Mensaje recibido -> Clave: AAPL, Valor: 200
```