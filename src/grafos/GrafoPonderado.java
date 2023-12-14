package grafos;

import java.util.*;

public class GrafoPonderado {

    private Map<Integer, List<Aresta>> listaAdjacencia;

    // Construtor
    public GrafoPonderado() {
        this.listaAdjacencia = new HashMap<>();
    }

    // Método para adicionar um vértice
    public void addVertice(int vertice) {
        listaAdjacencia.put(vertice, new ArrayList<>());
    }

    // Método de adicionar aresta
    public void addAresta(int origem, int destino, int peso) {
        listaAdjacencia.get(origem).add(new Aresta(destino, peso));
        listaAdjacencia.get(destino).add(new Aresta(origem, peso));
    }

    // Método para mostrar o Grafo
    public void printGrafo() {
        for (Map.Entry<Integer, List<Aresta>> entrada : listaAdjacencia.entrySet()) {
            System.out.print(entrada.getKey() + " -> ");
            List<Aresta> arestas = entrada.getValue();
            for (Aresta aresta : arestas) {
                System.out.print("(" + aresta.getDestino() + ", " + aresta.getPeso() + ") ");
            }
            System.out.println();
        }
    }

    // 1) a) Método para remover um vértice
    public void removeVertice(int vertice) {
        if (!listaAdjacencia.containsKey(vertice)) {
            throw new IllegalArgumentException("ERRO: Vértice não está presente no Grafo!");
        }
    
        listaAdjacencia.remove(vertice);
    
        for (List<Aresta> arestas : listaAdjacencia.values()) {
            arestas.removeIf(aresta -> aresta.getDestino() == vertice);
        }
    }

    // 1) b) Método para verificar se o Grafo é conexo
    public boolean isConexo() {
        Set<Integer> visitado = new HashSet<>();
        Iterator<Integer> i = listaAdjacencia.keySet().iterator();
        if (i.hasNext()) {
            dfs(i.next(), visitado);
        }
        return visitado.size() == listaAdjacencia.size();
    }
    // Método Depth-first search
    private void dfs(int vertice, Set<Integer> visitado) {
        if (!visitado.contains(vertice)) {
            visitado.add(vertice);
            for (Aresta aresta : listaAdjacencia.get(vertice)) {
                dfs(aresta.getDestino(), visitado);
            }
        }
    }

    // 1) c) Método para verificar se o Grafo é completo
    public boolean isCompleto() {
        for (int vertice1 : listaAdjacencia.keySet()) {
            for (int vertice2 : listaAdjacencia.keySet()) {
                if (vertice1 != vertice2 && !temArestaUnica(vertice1, vertice2)) {
                    return false;
                }
            }
        }
        return true;
    }
    // Método para verificar Arestas únicas
    private boolean temArestaUnica(int vertice1, int vertice2) {
        List<Aresta> arestas1 = listaAdjacencia.get(vertice1);
        List<Aresta> arestas2 = listaAdjacencia.get(vertice2);
    
        int cont = 0;
        for (Aresta aresta : arestas1) {
            if (aresta.getDestino() == vertice2 && arestas2.contains(aresta)) {
                cont++;
            }
        }
    
        return cont == 1;
    }

    // 1) d) Método com o algoritmo de Dijkstra
    public List<Integer> dijkstraCaminhoMaisCurto(int verticeInicial, int verticeFinal) {
        if (!listaAdjacencia.containsKey(verticeInicial) || !listaAdjacencia.containsKey(verticeFinal)) {
            throw new IllegalArgumentException("ERRO: Vértice inicial ou final não presente no Grafo!");
        }

        PriorityQueue<Nodo> filaAnterior = new PriorityQueue<>(Comparator.comparingInt(nodo -> nodo.distancia));
        Map<Integer, Integer> distancias = new HashMap<>();
        Map<Integer, Integer> anterior = new HashMap<>();

        for (int vertice : listaAdjacencia.keySet()) {
            distancias.put(vertice, vertice == verticeInicial ? 0 : Integer.MAX_VALUE);
            anterior.put(vertice, null);
            filaAnterior.add(new Nodo(vertice, distancias.get(vertice)));
        }

        // Algoritmo de Dijkstra
        while (!filaAnterior.isEmpty()) {
            int verticeAtual = filaAnterior.poll().vertice;

            for (Aresta aresta : listaAdjacencia.get(verticeAtual)) {
                int neighbor = aresta.getDestino();
                int newDistance = distancias.get(verticeAtual) + aresta.getPeso();

                if (newDistance < distancias.get(neighbor)) {
                    distancias.put(neighbor, newDistance);
                    anterior.put(neighbor, verticeAtual);
                    filaAnterior.add(new Nodo(neighbor, newDistance));
                }
            }
        }

        List<Integer> shortestPath = new ArrayList<>();
        int atual = verticeFinal;
        while (anterior.get(atual) != null) {
            shortestPath.add(atual);
            atual = anterior.get(atual);
        }
        Collections.reverse(shortestPath);

        return shortestPath;
    }

    // 1) e) Método para verificar se o Grafo é Euleriano, Semi-Euleriano ou Não-Euleriano
    public String checkEuleriano() {
        int contGrauImpar = 0;
    
        for (int vertice : listaAdjacencia.keySet()) {
            List<Aresta> arestas = listaAdjacencia.get(vertice);
            int grau = arestas.size();
    
            if (grau % 2 != 0) {
                contGrauImpar++;
            }
        }
    
        if (contGrauImpar == 0) {
            return "Euleriano";
        } else if (contGrauImpar == 2) {
            return "Semi-Euleriano";
        } else {
            return "Não-Euleriano";
        }
    }

    // 1) f) Método para verificar se o Grafo é Hamiltoniano, Semi-Hamiltoniano ou Não-Hamiltoniano
    public String checkHamiltoniano() {
        int numVertices = listaAdjacencia.size();
        if (numVertices < 3) {
            return "Não-Hamiltoniano"; // Grafos com menos de 3 vertices não são Hamiltonianos
        }
        for (int vertice : listaAdjacencia.keySet()) {
            if (isCaminhoHamiltonianoComecandoDe(vertice, new HashSet<>(), vertice, numVertices - 1)) {
                return "Hamiltoniano";
            }
        }
        for (int vertice : listaAdjacencia.keySet()) {
            if (isCaminhoSemiHamiltonianoComecandoDe(vertice, new HashSet<>(), vertice)) {
                return "Semi-Hamiltoniano";
            }
        }
        return "Não-Hamiltoniano";
    }
    // Método para verificar se um caminho começando de certo vértice é ou não Hamiltoniano
    private boolean isCaminhoHamiltonianoComecandoDe(int verticeInicial, Set<Integer> visitado, int verticeAtual, int verticesRestantes) {
        visitado.add(verticeAtual);
        if (verticesRestantes == 0) {
            visitado.remove(verticeAtual);
            return true;
        }
        for (Aresta aresta : listaAdjacencia.get(verticeAtual)) {
            int proximoVertice = aresta.getDestino();
            if (!visitado.contains(proximoVertice)) {
                if (isCaminhoHamiltonianoComecandoDe(verticeInicial, visitado, proximoVertice, verticesRestantes - 1)) {
                    return true;
                }
            }
        }
        visitado.remove(verticeAtual);
        return false;
    }
    // Método para verificar se um caminho começando de certo vértice é ou não Semi-Hamiltoniano
    private boolean isCaminhoSemiHamiltonianoComecandoDe(int verticeInicial, Set<Integer> visitado, int verticeAtual) {
        visitado.add(verticeAtual);
        for (Aresta aresta : listaAdjacencia.get(verticeAtual)) {
            int proximoVertice = aresta.getDestino();
            if (proximoVertice == verticeInicial && visitado.size() == listaAdjacencia.size()) {
                visitado.remove(verticeAtual);
                return true;
            }
            if (!visitado.contains(proximoVertice)) {
                if (isCaminhoSemiHamiltonianoComecandoDe(verticeInicial, visitado, proximoVertice)) {
                    return true;
                }
            }
        }
        visitado.remove(verticeAtual);
        return false;
    }

    // Classe Aresta
    private static class Aresta {
        private int destino;
        private int peso;

        public Aresta(int destino, int peso) {
            this.destino = destino;
            this.peso = peso;
        }

        public int getDestino() {
            return destino;
        }

        public int getPeso() {
            return peso;
        }
    }

    // Classe Nodo
    private static class Nodo {
        private int vertice;
        private int distancia;

        public Nodo(int vertice, int distancia) {
            this.vertice = vertice;
            this.distancia = distancia;
        }
    }
}
