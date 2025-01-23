# Ejercicio: Análisis y Procesamiento de Pedidos por Categorías

## Requisitos del Ejercicio
### Tópicos Kafka Involucrados

Entrada
- `t-commodity-orders`: Contiene mensajes de pedidos con la estructura OrderMessage.

Salida
- `t-orders-plastic`: Pedidos cuya categoría de artículos es "Plastic".
- `t-orders-steel`: Pedidos cuya categoría de artículos es "Steel".
- `t-orders-others`: Pedidos que no caen en las categorías anteriores.
- `t-orders-discounted`: Pedidos que califican para un descuento basado en cantidad y precio.
- `t-orders-all`: Combinación de todas las órdenes.

### Lógica del Flujo

- Clasificar los pedidos en tres categorías: "Plastic", "Steel" y "Others", basándose en el nombre del artículo (itemName).
- Calcular el total del pedido (price * quantity) y enviarlo al tópico t-orders-discounted si supera los $500 y la cantidad es mayor a 10.
- Cambiar la clave de los mensajes en t-orders-discounted para que sea una combinación de la ubicación y el número de orden (orderLocation-orderNumber).

### Funcionalidad Avanzada

- Implementar un merge de los flujos resultantes para generar un único stream que combine todos los pedidos procesados. 
- Este stream debe escribirse en un nuevo tópico `t-orders-all`.

### Especificaciones Técnicas

Transformaciones y Operaciones

- Usa `branch` para dividir el stream en las tres categorías.
- Aplica `mapValues` para calcular el total del pedido.
- Usa `filter` para seleccionar pedidos que califican para el descuento.
- Usa `selectKey` para cambiar la clave en los mensajes de t-orders-discounted.
- Usa `merge` para combinar los flujos de salida.

Métodos auxiliares

- `isPlastic`, `isSteel` y `isOtherCategory` para determinar la categoría del artículo.
- `calculateTotal` para calcular el total del pedido.
- `isDiscountEligible` para validar si un pedido califica para el descuento.
- `generateKey` para formar la clave combinada.

- Publica mensajes en el tópico t-orders con datos de prueba variados.
- Verifica los mensajes procesados en los tópicos de salida (t-orders-plastic, t-orders-steel, etc.).
- Comprueba el resultado combinado en t-orders-all.

Extra
- Completar la funcionalidad de bono usando merge correctamente sumará puntos adicionales. 
- Asegúrate de documentar cada paso con comentarios claros en el código.