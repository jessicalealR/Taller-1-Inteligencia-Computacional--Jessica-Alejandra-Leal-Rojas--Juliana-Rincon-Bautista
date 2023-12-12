package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Rutas2 {

    static class Estado {
        int calle;
        int carrera;
        int trafico;

        Estado(int calle, int carrera, int trafico) {
            this.calle = calle;
            this.carrera = carrera;
            this.trafico = trafico;
        }
    }

    static class Ruta {
        List<Estado> estados;

        Ruta() {
            this.estados = new ArrayList<>();
        }

        void agregarEstado(Estado estado) {
            this.estados.add(estado);
        }
    }

    public static Ruta planificarRuta(Estado estadoInicial, Estado estadoDestino, int profundidadMaxima) {
        Ruta mejorRuta = new Ruta();
        System.out.println("Iniciando planificación de rutas...");
        alphaBeta(estadoInicial, estadoDestino, Integer.MIN_VALUE, Integer.MAX_VALUE, true, 0, profundidadMaxima, mejorRuta);
        return mejorRuta;
    }

    private static int alphaBeta(Estado estado, Estado estadoDestino, int alfa, int beta, boolean maximizador,
                                 int profundidad, int profundidadMaxima, Ruta mejorRuta) {
        System.out.println("Profundidad: " + profundidad + ", Evaluando estado: Calle " + estado.calle +
                ", Carrera " + estado.carrera + ", Trafico " + estado.trafico);

        if (profundidad == profundidadMaxima || esNodoTerminal(estado, estadoDestino)) {
            int valor = evaluarRuta(estado, estadoDestino, mejorRuta);
            System.out.println("Valor del estado terminal: " + valor);
            return valor;
        }

        int valor;
        if (maximizador) {
            valor = Integer.MIN_VALUE;
            for (Estado sucesor : generarSucesores(estado)) {
                valor = Math.max(valor, alphaBeta(sucesor, estadoDestino, alfa, beta, false, profundidad + 1, profundidadMaxima, mejorRuta));
                alfa = Math.max(alfa, valor);
                System.out.println("Alfa: " + alfa + ", Beta: " + beta);
                if (beta <= alfa) {
                    System.out.println("Poda Beta: Se omite la exploración de otros nodos en esta rama.");
                    break;
                }
            }
        } else {
            valor = Integer.MAX_VALUE;
            for (Estado sucesor : generarSucesores(estado)) {
                valor = Math.min(valor, alphaBeta(sucesor, estadoDestino, alfa, beta, true, profundidad + 1, profundidadMaxima, mejorRuta));
                beta = Math.min(beta, valor);
                System.out.println("Alfa: " + alfa + ", Beta: " + beta);
                if (beta <= alfa) {
                    System.out.println("Poda Alfa: Se omite la exploración de otros nodos en esta rama.");
                    break;
                }
            }
        }

        return valor;
    }

    private static boolean esNodoTerminal(Estado estado, Estado estadoDestino) {
        boolean esTerminal = estado.calle == estadoDestino.calle && estado.carrera == estadoDestino.carrera;
        if (esTerminal) {
            System.out.println("Nodo Terminal: Se ha alcanzado el destino en Calle " +
                    estado.calle + ", Carrera " + estado.carrera);
        }
        return esTerminal;
    }

    private static List<Estado> generarSucesores(Estado estado) {
        List<Estado> sucesores = new ArrayList<>();
        if (estado.calle < 10) {
            sucesores.add(new Estado(estado.calle + 1, estado.carrera, calcularTrafico()));
        }
        if (estado.carrera < 5) {
            sucesores.add(new Estado(estado.calle, estado.carrera + 1, calcularTrafico()));
        }
        return sucesores;
    }

    private static int evaluarRuta(Estado estado, Estado estadoDestino, Ruta ruta) {
        int distanciaAlDestino = Math.abs(estado.calle - estadoDestino.calle) + Math.abs(estado.carrera - estadoDestino.carrera);
        int valor = distanciaAlDestino - estado.trafico;

        System.out.println("Evaluando estado: Calle " + estado.calle + ", Carrera " + estado.carrera +
                ", Trafico " + estado.trafico + ", Distancia al destino " + distanciaAlDestino +
                ", Valor Heurístico: " + valor);

        ruta.agregarEstado(estado);
        return valor;
    }

    private static int calcularTrafico() {
        return (int) (Math.random() * 10);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Obtener el punto de inicio aleatorio dentro de los límites 10x5
        Estado estadoInicial = obtenerPuntoAleatorio(10, 5);
        System.out.println("Posición inicial aleatoria: Calle " + estadoInicial.calle + ", Carrera " +
                estadoInicial.carrera);

        // Obtener el punto de destino aleatorio dentro de los límites 10x5
        Estado estadoDestino = obtenerPuntoAleatorio(10, 5);
        System.out.println("Posición destino aleatoria: Calle " + estadoDestino.calle + ", Carrera " +
                estadoDestino.carrera);

        int profundidadMaxima = 7;

        System.out.println("\nIniciando la planificación de rutas...");

        Ruta rutaOptima = planificarRuta(estadoInicial, estadoDestino, profundidadMaxima);

        System.out.println("\nRuta óptima:");
        for (Estado estado : rutaOptima.estados) {
            System.out.println("Calle " + estado.calle + ", Carrera " + estado.carrera +
                    ", Trafico " + estado.trafico);
        }

        Estado estadoFinal = rutaOptima.estados.get(rutaOptima.estados.size() - 1);
        System.out.println("Posición final: Calle " + estadoFinal.calle + ", Carrera " +
                estadoFinal.carrera);

        scanner.close();
    }

    private static Estado obtenerPuntoAleatorio(int limiteCalle, int limiteCarrera) {
        return new Estado((int) (Math.random() * limiteCalle) + 1, (int) (Math.random() * limiteCarrera) + 1, calcularTrafico());
    }
}
