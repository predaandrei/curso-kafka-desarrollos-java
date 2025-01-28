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