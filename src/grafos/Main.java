package grafos;

import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        // Grafo ponderado
        GrafoPonderado grafoPonderado = new GrafoPonderado();

        grafoPonderado.addVertice(1);
        grafoPonderado.addVertice(2);
        grafoPonderado.addVertice(3);
        grafoPonderado.addVertice(4);
        grafoPonderado.addVertice(5);
        grafoPonderado.addVertice(6);
        grafoPonderado.addAresta(1, 2, 5);
        grafoPonderado.addAresta(2, 3, 7);
        grafoPonderado.addAresta(1, 3, 3);
        grafoPonderado.addAresta(3, 5, 1);
        grafoPonderado.addAresta(3, 4, 10);
        grafoPonderado.addAresta(2, 4, 2);
        grafoPonderado.addAresta(4, 5, 2);
        grafoPonderado.addAresta(5, 6, 20);

        grafoPonderado.printGrafo();
        System.out.println();
        grafoPonderado.removeVertice(6);
        grafoPonderado.printGrafo();

        System.out.println();
        System.out.println("Conexo?: " + grafoPonderado.isConexo());
        System.out.println("Completo?: " + grafoPonderado.isCompleto());
        System.out.println("Euleriano?: " + grafoPonderado.checkEuleriano());
        System.out.println("Hamiltoniano?: " + grafoPonderado.checkHamiltoniano());

        int verticeInicial = 1;
        int verticeFinal = 4;
        List<Integer> caminhoMaisCurto = grafoPonderado.dijkstraCaminhoMaisCurto(verticeInicial, verticeFinal);
        System.out.println("\nCaminho mais curto de " + verticeInicial + " para " + verticeFinal + ": " + caminhoMaisCurto);
    }
}
