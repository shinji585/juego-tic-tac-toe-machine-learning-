package com.example.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ControllerDecorator {
     private Controller controller;
    private boolean modoFacil = false;
    private Random random = new Random();
    private int[] mejoresMovimientos = {4, 0, 2, 6, 8, 1, 3, 5, 7}; // Orden de prioridad
    
    public ControllerDecorator(Controller controller) {
        this.controller = controller;
    }
    
    public void setModoFacil(boolean activado) {
        this.modoFacil = activado;
    }
    
    public int hacerMovimientoIA() {
        int[] tablero = controller.getTablero();
        
        // 1. Verificación de victoria/bloqueo (para ambos modos)
        int movimiento = buscarMovimientoRapido(tablero);
        if (movimiento != -1) {
            controller.hacerMovimientoIA(movimiento);
            return movimiento;
        }
        
        if (!modoFacil) {
            // MODO DIFÍCIL - Estrategia perfecta usando minimax
            movimiento = encontrarMejorMovimiento(tablero);
        } else {
            // MODO FÁCIL - Comportamiento más aleatorio y predecible
            movimiento = movimientoFacil(tablero);
        }
        
        if (movimiento != -1) {
            controller.hacerMovimientoIA(movimiento);
        }
        
        return movimiento;
    }
    
    private int encontrarMejorMovimiento(int[] tablero) {
        // Implementación del algoritmo minimax para modo difícil
        int mejorValor = Integer.MIN_VALUE;
        int mejorMovimiento = -1;
        
        for (int i = 0; i < 9; i++) {
            if (tablero[i] == 0) {
                tablero[i] = Controller.IA;
                int valorMovimiento = minimax(tablero, 0, false);
                tablero[i] = 0;
                
                if (valorMovimiento > mejorValor) {
                    mejorValor = valorMovimiento;
                    mejorMovimiento = i;
                }
            }
        }
        return mejorMovimiento;
    }
    
    private int minimax(int[] tablero, int profundidad, boolean esMaximizador) {
        int resultado = controller.verificarGanador();
        
        if (resultado == Controller.IA) return 10 - profundidad;
        if (resultado == Controller.HUMANO) return -10 + profundidad;
        if (resultado == -1) return 0; // Empate
        
        if (esMaximizador) {
            int mejorValor = Integer.MIN_VALUE;
            for (int i = 0; i < 9; i++) {
                if (tablero[i] == 0) {
                    tablero[i] = Controller.IA;
                    int valor = minimax(tablero, profundidad + 1, false);
                    tablero[i] = 0;
                    mejorValor = Math.max(mejorValor, valor);
                }
            }
            return mejorValor;
        } else {
            int mejorValor = Integer.MAX_VALUE;
            for (int i = 0; i < 9; i++) {
                if (tablero[i] == 0) {
                    tablero[i] = Controller.HUMANO;
                    int valor = minimax(tablero, profundidad + 1, true);
                    tablero[i] = 0;
                    mejorValor = Math.min(mejorValor, valor);
                }
            }
            return mejorValor;
        }
    }
    
    private int movimientoFacil(int[] tablero) {
        // 50% de probabilidad de elegir un movimiento estratégico
        if (random.nextDouble() < 0.5) {
            for (int pos : mejoresMovimientos) {
                if (tablero[pos] == 0) {
                    return pos;
                }
            }
        }
        
        // 50% de movimiento completamente aleatorio
        return movimientoAleatorio(tablero);
    }
    
    private int buscarMovimientoRapido(int[] tablero) {
        int[][] lineasCriticas = {
            {0,1,2}, {3,4,5}, {6,7,8}, {0,3,6}, 
            {1,4,7}, {2,5,8}, {0,4,8}, {2,4,6}
        };
        
        // Primero buscar ganar
        for (int[] linea : lineasCriticas) {
            int resultado = evaluarLinea(tablero, linea, Controller.IA);
            if (resultado != -1) return resultado;
        }
        
        // Luego bloquear
        for (int[] linea : lineasCriticas) {
            int resultado = evaluarLinea(tablero, linea, Controller.HUMANO);
            if (resultado != -1) return resultado;
        }
        
        return -1;
    }
    
    private int evaluarLinea(int[] tablero, int[] linea, int jugador) {
        int count = 0;
        int posVacia = -1;
        
        for (int pos : linea) {
            if (tablero[pos] == jugador) {
                count++;
            } else if (tablero[pos] == 0) {
                posVacia = pos;
            }
        }
        return (count == 2 && posVacia != -1) ? posVacia : -1;
    }
    
    private int movimientoAleatorio(int[] tablero) {
        List<Integer> disponibles = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            if (tablero[i] == 0) disponibles.add(i);
        }
        return disponibles.isEmpty() ? -1 : disponibles.get(random.nextInt(disponibles.size()));
    }
    
    // Métodos delegados
    public void reiniciarJuego() {
        controller.reiniciarJuego();
    }
    
    public boolean hacerMovimientoHumano(int posicion) {
        return controller.hacerMovimientoHumano(posicion);
    }
    
    public int verificarGanador() {
        return controller.verificarGanador();
    }
    
    public boolean esFinal() {
        return controller.esFinal();
    }
    
    public int[] getTablero() {
        return controller.getTablero();
    }
}