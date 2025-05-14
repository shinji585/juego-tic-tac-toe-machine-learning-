package com.example.Controller;

public class Controller {
    protected int[] tablero;
    public static final int HUMANO = 1;
    public static final int IA = 2;
    
    public Controller() {
        reiniciarJuego();
    }
    
    public void reiniciarJuego() {
        tablero = new int[9];
    }
    
    public boolean hacerMovimientoHumano(int posicion) {
        if (posicion < 0 || posicion > 8 || tablero[posicion] != 0) {
            return false;
        }
        tablero[posicion] = HUMANO;
        return true;
    }
    
    public boolean hacerMovimientoIA(int posicion) {
        if (posicion < 0 || posicion > 8 || tablero[posicion] != 0) {
            return false;
        }
        tablero[posicion] = IA;
        return true;
    }
    
    public int verificarGanador() {
        int[][] lineasGanadoras = {
            {0, 1, 2}, {3, 4, 5}, {6, 7, 8}, // filas
            {0, 3, 6}, {1, 4, 7}, {2, 5, 8}, // columnas
            {0, 4, 8}, {2, 4, 6}             // diagonales
        };
        
        for (int[] linea : lineasGanadoras) {
            if (tablero[linea[0]] != 0 && 
                tablero[linea[0]] == tablero[linea[1]] && 
                tablero[linea[0]] == tablero[linea[2]]) {
                return tablero[linea[0]];
            }
        }
        
        for (int pos : tablero) {
            if (pos == 0) return 0; // Juego continúa
        }
        
        return -1; // Empate
    }
    
    public boolean esFinal() {
        return verificarGanador() != 0;
    }
    
    public int[] getTablero() {
        return tablero.clone();
    }
}