import java.util.*;

public class APE4_Grafos {

    // ═══════════════════════════════════════
    // Nodo
    // ═══════════════════════════════════════
    static class Nodo {
        String id;
        String nombre;

        public Nodo(String id, String nombre) {
            this.id = id;
            this.nombre = nombre;
        }
    }

    // ═══════════════════════════════════════
    // Arista
    // ═══════════════════════════════════════
    static class Arista {
        String destino;
        int peso;

        public Arista(String destino, int peso) {
            this.destino = destino;
            this.peso = peso;
        }
    }

    // ═══════════════════════════════════════
    // Grafo
    // ═══════════════════════════════════════
    static class Grafo {

        Map<String, Nodo> nodos = new HashMap<>();
        Map<String, List<Arista>> adyacencia = new HashMap<>();

        // ═══════════════════════════════════
        // TODO 1
        // Agregar nodo al grafo
        // ═══════════════════════════════════
        public void agregarNodo(String id, String nombre) {

            // COMPLETE AQUÍ
            nodos.put(id, new Nodo(id, nombre));
            adyacencia.putIfAbsent(id, new ArrayList<>());


        }

        // ═══════════════════════════════════
        // TODO 2
        // Agregar arista no dirigida
        // ═══════════════════════════════════
        public void agregarArista(String origen, String destino, int peso) {

            // COMPLETE AQUÍ
            // Como es no dirigida, agregamos en ambas direcciones
             adyacencia.get(origen).add(new Arista(destino, peso));
             adyacencia.get(destino).add(new Arista(origen, peso));


        }

        // ═══════════════════════════════════
        // TODO 3 — BFS
        // Ruta con menos paradas
        // ═══════════════════════════════════
        public List<String> bfs(String inicio, String fin) {

            // Cola para recorrer niveles
            Queue<List<String>> cola = new LinkedList<>();

            // Nodos visitados
            Set<String> visitados = new HashSet<>();

            // Camino inicial
            List<String> caminoInicial = new ArrayList<>();

            // TODO:
            // Agregar nodo inicio al camino inicial

                caminoInicial.add(inicio);
            // TODO:
            // Agregar caminoInicial a la cola
                cola.add(caminoInicial);

            // TODO:
            // Marcar inicio como visitado
               visitados.add(inicio);


            while (!cola.isEmpty()) {

                // TODO:
                // Obtener el primer camino de la cola
                List<String> camino = cola.poll();
              


                // Nodo actual
                String actual=camino.get(camino.size() - 1);

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

            return null;
        }

        // ═══════════════════════════════════
        // TODO 4 — Dijkstra
        // Ruta con menor distancia
        // ═══════════════════════════════════
        public List<String> dijkstra(String inicio, String fin) {
    Map<String, Integer> distancias = new HashMap<>();
    Map<String, String> anteriores = new HashMap<>();
    PriorityQueue<String> cola = new PriorityQueue<>(Comparator.comparingInt(distancias::get));

    for (String nodo : nodos.keySet()) {
        distancias.put(nodo, Integer.MAX_VALUE);
    }
    
    distancias.put(inicio, 0);
    cola.add(inicio);

    while (!cola.isEmpty()) {
        String actual = cola.poll();
        if (actual.equals(fin)) break;

        for (Arista arista : adyacencia.getOrDefault(actual, new ArrayList<>())) {
            int nuevaDistancia = distancias.get(actual) + arista.peso;
            if (nuevaDistancia < distancias.get(arista.destino)) {
                distancias.put(arista.destino, nuevaDistancia);
                anteriores.put(arista.destino, actual);
                cola.add(arista.destino);
            }
        }
    }

    List<String> camino = new ArrayList<>();
    for (String at = fin; at != null; at = anteriores.get(at)) {
        camino.add(0, at);
    }
    return camino.get(0).equals(inicio) ? camino : null;
}
        // ═══════════════════════════════════
        // Mostrar resultado
        // ═══════════════════════════════════
        public void mostrarRuta(List<String> ruta) {

            if (ruta == null) {
                System.out.println("No existe ruta");
                return;
            }

            for (int i = 0; i < ruta.size(); i++) {

                String idNodo = ruta.get(i);

                Nodo nodo = nodos.get(idNodo);

                System.out.print(
                    nodo.nombre + " (" + nodo.id + ")"
                );

                if (i < ruta.size() - 1) {
                    System.out.print(" -> ");
                }
            }

            System.out.println();
        }
    }

    // ═══════════════════════════════════════
    // MAIN
    // ═══════════════════════════════════════
    public static void main(String[] args) {

        Grafo grafo = new Grafo();

        // NODOS
        grafo.agregarNodo("uta", "Universidad");
        grafo.agregarNodo("fisei", "FISEI");
        grafo.agregarNodo("idiomas", "Idiomas");
        grafo.agregarNodo("biblioteca", "Biblioteca");
        grafo.agregarNodo("estadio", "Estadio");
        grafo.agregarNodo("comedor", "Comedor");

        // ARISTAS
        grafo.agregarArista("uta", "fisei", 50);
        grafo.agregarArista("fisei", "idiomas", 40);
        grafo.agregarArista("idiomas", "biblioteca", 30);
        grafo.agregarArista("biblioteca", "estadio", 70);

        // Ruta con menos paradas
        // pero más distancia
        grafo.agregarArista("uta", "comedor", 20);
        grafo.agregarArista("comedor", "estadio", 200);

        // ═══════════════════════════════════
        // PRUEBAS
        // ═══════════════════════════════════

        System.out.println("===== BFS =====");

        List<String> rutaBFS =
                grafo.bfs("uta", "estadio");

        grafo.mostrarRuta(rutaBFS);

        System.out.println("\n===== DIJKSTRA =====");

        List<String> rutaDijkstra =
                grafo.dijkstra("uta", "estadio");

        grafo.mostrarRuta(rutaDijkstra);
    }
}