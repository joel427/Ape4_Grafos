# APE4 - Grafos: Búsqueda de Rutas Óptimas

##  Descripción de la Guía

Esta es una práctica académica (**APE4**) que implementa algoritmos de búsqueda en grafos para encontrar rutas óptimas entre nodos. El objetivo principal es comprender y aplicar dos estrategias diferentes de búsqueda: **BFS (Breadth-First Search)** y **Dijkstra**, comparando cómo cada uno optimiza diferentes criterios.

---

##  Objetivo

Implementar un sistema de navegación de grafos que pueda encontrar:
1. **Ruta con menor número de paradas** (usando BFS)
2. **Ruta con menor distancia total** (usando Dijkstra)

Este concepto es aplicable a problemas reales como:
- Sistemas de navegación GPS
- Redes de transporte público
- Redes de comunicación
- Planificación de rutas en logística

---

##  Componentes Implementados

### 1. **Clase Nodo**
Representa cada punto en el grafo con:
- `id`: Identificador único del nodo
- `nombre`: Nombre descriptivo del ubicación

```java
static class Nodo {
    String id;
    String nombre;
}
```

### 2. **Clase Arista**
Representa la conexión entre dos nodos con:
- `destino`: ID del nodo destino
- `peso`: Distancia o costo de la conexión

```java
static class Arista {
    String destino;
    int peso;
}
```

### 3. **Clase Grafo**
Estructura principal que contiene:
- `nodos`: Mapa de nodos disponibles
- `adyacencia`: Mapa de listas de adyacencia (representación del grafo)

---

##  Algoritmos Implementados

### **TODO 1 - Agregar Nodo**
```java
public void agregarNodo(String id, String nombre)
```
- Crea un nuevo nodo con identificador y nombre
- Lo añade al mapa de nodos
- Inicializa una lista vacía de adyacencia para ese nodo

**Solución implementada:**
```java
nodos.put(id, new Nodo(id, nombre));
adyacencia.putIfAbsent(id, new ArrayList<>());
```

### **TODO 2 - Agregar Arista (No Dirigida)**
```java
public void agregarArista(String origen, String destino, int peso)
```
- Crea una conexión bidireccional entre dos nodos
- Se agrega la arista tanto en origen → destino como en destino → origen
- Cada arista tiene un peso (distancia)

**Solución implementada:**
```java
// Como es no dirigida, agregamos en ambas direcciones
adyacencia.get(origen).add(new Arista(destino, peso));
adyacencia.get(destino).add(new Arista(origen, peso));
```

### **TODO 3 - BFS (Breadth-First Search)**
```java
public List<String> bfs(String inicio, String fin)
```

**Propósito:** Encontrar la ruta con el **menor número de paradas**.

**Estrategia:**
- Usa una cola (FIFO) para explorar nodos por niveles
- Marca nodos visitados para evitar ciclos
- Mantiene el camino completo desde el origen

**Complejidad:** O(V + E) donde V = vértices, E = aristas

**Solución implementada:**
```java
// Cola para recorrer niveles
Queue<List<String>> cola = new LinkedList<>();

// Nodos visitados
Set<String> visitados = new HashSet<>();

// Camino inicial
List<String> caminoInicial = new ArrayList<>();

// Agregar nodo inicio al camino inicial
caminoInicial.add(inicio);

// Agregar caminoInicial a la cola
cola.add(caminoInicial);

// Marcar inicio como visitado
visitados.add(inicio);

while (!cola.isEmpty()) {
    // Obtener el primer camino de la cola
    List<String> camino = cola.poll();
    
    // Nodo actual
    String actual = camino.get(camino.size() - 1);
    
    // Si llegamos al destino
    if (actual.equals(fin)) {
        return camino;
    }
    
    // Recorrer vecinos
    for (Arista arista : adyacencia.get(actual)) {
        if (!visitados.contains(arista.destino)) {
            visitados.add(arista.destino);
            List<String> nuevoCamino = new ArrayList<>(camino);
            nuevoCamino.add(arista.destino);
            cola.add(nuevoCamino);
        }
    }
}
```

**Ventaja:** Garantiza encontrar el camino más corto en términos de número de pasos.

### **TODO 4 - Dijkstra**
```java
public List<String> dijkstra(String inicio, String fin)
```

**Propósito:** Encontrar la ruta con la **menor distancia total**.

**Estrategia:**
- Usa una cola de prioridad ordenada por distancia
- Mantiene registro de distancias mínimas a cada nodo
- Rastrea el nodo anterior para reconstruir el camino
- Actualiza distancias si encuentra un camino más corto

**Complejidad:** O((V + E) log V) con cola de prioridad

**Solución implementada:**
```java
Map<String, Integer> distancias = new HashMap<>();
Map<String, String> anteriores = new HashMap<>();
PriorityQueue<String> cola = new PriorityQueue<>(Comparator.comparingInt(distancias::get));

// Inicializar distancias a infinito
for (String nodo : nodos.keySet()) {
    distancias.put(nodo, Integer.MAX_VALUE);
}

// Distancia inicial es 0
distancias.put(inicio, 0);
cola.add(inicio);

while (!cola.isEmpty()) {
    String actual = cola.poll();
    if (actual.equals(fin)) break;
    
    // Explorar vecinos
    for (Arista arista : adyacencia.getOrDefault(actual, new ArrayList<>())) {
        int nuevaDistancia = distancias.get(actual) + arista.peso;
        if (nuevaDistancia < distancias.get(arista.destino)) {
            // Actualizar distancia
            distancias.put(arista.destino, nuevaDistancia);
            anteriores.put(arista.destino, actual);
            cola.add(arista.destino);
        }
    }
}

// Reconstruir camino
List<String> camino = new ArrayList<>();
for (String at = fin; at != null; at = anteriores.get(at)) {
    camino.add(0, at);
}
```

**Ventaja:** Encuentra el camino óptimo considerando los pesos de las aristas (distancias reales).

---

## 🏫 Caso de Uso: Sistema de Navegación Universitaria

El programa implementa un ejemplo práctico: encontrar rutas óptimas dentro del campus de la Universidad Técnica de Ambato.

### **Nodos (Ubicaciones):**
- `uta`: Universidad Técnica de Ambato
- `fisei`: Facultad de Ingeniería en Sistemas
- `idiomas`: Centro de Idiomas
- `biblioteca`: Biblioteca Central
- `estadio`: Estadio Universitario
- `comedor`: Comedor Estudiantil

### **Aristas (Conexiones con distancias en metros):**
```
Universidad (uta)
    ├─ FISEI (fisei)         : 50m
    └─ Comedor (comedor)     : 20m

FISEI (fisei)
    └─ Idiomas (idiomas)     : 40m

Idiomas (idiomas)
    └─ Biblioteca (biblioteca) : 30m

Biblioteca (biblioteca)
    └─ Estadio (estadio)     : 70m

Comedor (comedor)
    └─ Estadio (estadio)     : 200m
```

---

##  Ejemplo de Ejecución

### **Búsqueda de ruta: Universidad → Estadio**

#### **BFS (Ruta con menos paradas):**
```
Universidad (uta) -> Comedor (comedor) -> Estadio (estadio)
```
- **Paradas:** 3 nodos
- **Distancia total:** 220m (20m + 200m)
- **Ventaja:** Menos paradas, pero mayor distancia

#### **Dijkstra (Ruta con menor distancia):**
```
Universidad (uta) -> FISEI (fisei) -> Idiomas (idiomas) -> Biblioteca (biblioteca) -> Estadio (estadio)
```
- **Paradas:** 5 nodos
- **Distancia total:** 190m (50m + 40m + 30m + 70m)
- **Ventaja:** Menor distancia total

---

##  Comparación BFS vs Dijkstra

| Característica | BFS | Dijkstra |
|---|---|---|
| **Criterio** | Menor número de pasos | Menor costo/distancia |
| **Tipo de grafo** | Funciona en grafos no ponderados | Requiere pesos positivos |
| **Estructura datos** | Cola (FIFO) | Cola de prioridad |
| **Complejidad** | O(V + E) | O((V + E) log V) |
| **Uso** | Camino más corto por pasos | Ruta óptima por costo |

---

##  Casos de Uso Reales

1. **Navegación GPS:** Dijkstra para encontrar la ruta más corta en distancia
2. **Redes sociales:** BFS para encontrar el grado de separación entre usuarios
3. **Logística:** Dijkstra para optimizar rutas de entrega
4. **Telecomunicaciones:** Dijkstra para enrutamiento de paquetes con menor latencia
5. **Mapas de videojuegos:** BFS/Dijkstra para pathfinding de personajes

---

##  Cómo Compilar y Ejecutar

### **Compilar:**
```bash
javac APE4_Grafos.java
```

### **Ejecutar:**
```bash
java APE4_Grafos
```

### **Salida esperada:**
```
===== BFS =====
Universidad (uta) -> Comedor (comedor) -> Estadio (estadio)

===== DIJKSTRA =====
Universidad (uta) -> FISEI (fisei) -> Idiomas (idiomas) -> Biblioteca (biblioteca) -> Estadio (estadio)
```

---

##  Conceptos Clave

### **Grafo**
Estructura de datos que representa relaciones entre objetos mediante nodos y aristas.

### **Nodo**
Vértice del grafo que representa una entidad u ubicación.

### **Arista**
Conexión entre dos nodos, puede tener un peso asociado (costo, distancia, etc.).

### **Grafo No Dirigido**
Los bordes no tienen dirección; la conexión funciona en ambas direcciones.

### **Grafo Ponderado**
Cada arista tiene un peso que representa costo, distancia, tiempo, etc.

### **Cola (Queue)**
Estructura FIFO (First In, First Out) donde el primer elemento ingresado es el primero en salir.

### **Cola de Prioridad**
Estructura donde los elementos se ordenan por prioridad (en Dijkstra, por distancia mínima).

---

##  Aprendizajes

Al completar esta práctica, habrás:

 Entendido la estructura de un grafo y sus operaciones básicas  
 Implementado BFS para búsqueda por niveles  
 Implementado Dijkstra para optimización de caminos  
 Comparado diferentes estrategias de búsqueda  
 Aplicado algoritmos a problemas del mundo real  
 Comprendido la importancia de la elección del algoritmo según el criterio  

---

##  Autor

**Práctica realizada por:** Estudiante de la Universidad Técnica de Ambato  
**Asignatura:** Estructuras de Datos y Algoritmos  
**Práctica:** APE4 - Grafos  
**Fecha:** Junio 2026

---

##  Referencias

- [BFS - Wikipedia](https://en.wikipedia.org/wiki/Breadth-first_search)
- [Dijkstra's Algorithm - Wikipedia](https://en.wikipedia.org/wiki/Dijkstra%27s_algorithm)
- [Graph Theory Basics](https://www.geeksforgeeks.org/graph-data-structure-and-algorithms/)