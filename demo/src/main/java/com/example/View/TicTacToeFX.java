package com.example.View;

import com.example.Controller.Controller;
import com.example.Controller.ControllerDecorator;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.net.URL;
import java.util.Arrays;

/**
 * The TicTacToeFX class represents a graphical user interface (GUI) for a Tic Tac Toe game
 * where a human player competes against an AI. It extends the JavaFX Application class
 * and provides a visually appealing and interactive game experience.
 *
 * <p>Features:
 * - A 3x3 grid for the Tic Tac Toe board.
 * - Human vs AI gameplay with an optional "Friendly Mode" for easier AI.
 * - Score tracking for human wins, AI wins, and ties.
 * - Customizable visuals with images for players and results.
 * - A reset button to restart the game or reset scores.
 *
 * <p>Key Components:
 * - Buttons for each cell in the grid.
 * - Labels to display scores for human, AI, and ties.
 * - Images for player symbols and winner announcements.
 * - A checkbox to toggle "Friendly Mode" for the AI.
 *
 * <p>Usage:
 * - Run the application to start the game.
 * - Click on a cell to make a move as the human player.
 * - The AI will automatically make its move after the human.
 * - The game announces the winner or a tie at the end of each round.
 * - Use the reset button to restart the game or reset scores.
 *
 * <p>Dependencies:
 * - JavaFX library for GUI components.
 * - External image URLs for player symbols and winner graphics.
 *
 * <p>Note:
 * - Ensure a stable internet connection to load external images.
 * - The game logic is handled by the Controller and ControllerDecorator classes.
 *
 * <p>Author: [shinji585 ]
 * @version 1.0
 */
public class TicTacToeFX extends Application {
    private Button[][] buttons = new Button[3][3];
    private ControllerDecorator controller;
    
    private int victoriasHumanas = 0;
    private int victoriasIA = 0;
    private int empates = 0;
    
    private Image imagenHumano;
    private Image imagenIA;
    private Image imagenGanador;
    private Image imagenX;
    private Image imagenO;
    
    private Label contadorHumano;
    private Label contadorIA;
    private Label contadorEmpates;

    @Override
    public void start(@SuppressWarnings("exports") Stage primaryStage) {
        controller = new ControllerDecorator(new Controller());
        cargarImagenes();
        BorderPane root = crearInterfazPrincipal();
        
        CheckBox cbModoFacil = new CheckBox("Modo Amigable");
        cbModoFacil.setTextFill(Color.WHITE);
        cbModoFacil.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");
        cbModoFacil.setOnAction(e -> {
            controller.setModoFacil(cbModoFacil.isSelected());
            reiniciarJuegoCompleto();
        });
        
        HBox panelControles = (HBox) ((VBox) root.getCenter()).getChildren().get(1);
        Button btnReiniciar = (Button) panelControles.getChildren().get(1);
        btnReiniciar.setText("Reiniciar Todo");
        btnReiniciar.setStyle("-fx-background-color: #9C27B0; -fx-text-fill: white; " +
                           "-fx-font-weight: bold; -fx-font-size: 14; -fx-padding: 10 20; " +
                           "-fx-background-radius: 20;");
        btnReiniciar.setOnAction(e -> reiniciarJuegoCompleto());
        
        panelControles.getChildren().add(1, cbModoFacil);
        configurarEscena(primaryStage, root);
    }

    @SuppressWarnings("deprecation")
    private void cargarImagenes() {
        try {
            imagenHumano = new Image(new URL("https://i.pinimg.com/736x/0f/76/1d/0f761da46823184724083f9d614bd5d9.jpg").openStream());
            imagenIA = new Image(new URL("https://i.pinimg.com/736x/f1/22/0e/f1220e9f7a009e24ea1e1774ec5632ea.jpg").openStream());
            imagenGanador = new Image(new URL("https://i.pinimg.com/736x/55/fe/05/55fe056a74e97db91e1f3b52237b6166.jpg").openStream());
            imagenX = new Image(new URL("https://cdn-icons-png.flaticon.com/512/3063/3063187.png").openStream());
            imagenO = new Image(new URL("https://cdn-icons-png.flaticon.com/512/3522/3522633.png").openStream());
        } catch (Exception e) {
            System.out.println("Error cargando imágenes: " + e.getMessage());
        }
    }

    private BorderPane crearInterfazPrincipal() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #1a2a6c, #b21f1f, #fdbb2d);");
        
        root.setTop(crearPanelJugadores());
        root.setCenter(crearPanelCentral());
        
        return root;
    }

    private HBox crearPanelJugadores() {
        HBox panel = new HBox(30);
        panel.setAlignment(Pos.CENTER);
        panel.setPadding(new Insets(0, 0, 20, 0));
        
        VBox panelHumano = crearPanelJugador(imagenHumano, "0");
        contadorHumano = (Label) panelHumano.getChildren().get(1);
        
        VBox panelIA = crearPanelJugador(imagenIA, "0");
        contadorIA = (Label) panelIA.getChildren().get(1);
        
        panel.getChildren().addAll(panelHumano, panelIA);
        return panel;
    }

    private VBox crearPanelJugador(Image imagen, String contadorInicial) {
        VBox panel = new VBox(10);
        panel.setAlignment(Pos.CENTER);
        
        ImageView imageView = new ImageView(imagen);
        imageView.setFitWidth(80);
        imageView.setFitHeight(80);
        
        Label contador = new Label(contadorInicial);
        contador.setFont(new Font(20));
        contador.setTextFill(Color.WHITE);
        
        panel.getChildren().addAll(imageView, contador);
        return panel;
    }

    private VBox crearPanelCentral() {
        VBox centro = new VBox(20);
        centro.setAlignment(Pos.CENTER);
        
        centro.getChildren().addAll(
            crearTablero(),
            crearPanelControles()
        );
        
        return centro;
    }

    private GridPane crearTablero() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(15);
        grid.setVgap(15);
        
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Button btn = crearBotonCasilla(i, j);
                buttons[i][j] = btn;
                grid.add(btn, j, i);
            }
        }
        return grid;
    }

    private Button crearBotonCasilla(int fila, int col) {
        Button btn = new Button();
        btn.setMinSize(100, 100);
        btn.setStyle("-fx-background-radius: 15; -fx-background-color: rgba(255,255,255,0.2); " +
                    "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 10, 0, 0, 2);");
        
        final int pos = fila * 3 + col;
        btn.setOnAction(e -> manejarClickCasilla(pos));
        
        return btn;
    }

    private HBox crearPanelControles() {
        HBox panel = new HBox(20);
        panel.setAlignment(Pos.CENTER);
        panel.setPadding(new Insets(20, 0, 0, 0));
        
        VBox panelEmpates = crearPanelEmpates();
        Button btnReiniciar = crearBotonReinicio();
        
        panel.getChildren().addAll(panelEmpates, btnReiniciar);
        return panel;
    }

    private VBox crearPanelEmpates() {
        VBox panel = new VBox(10);
        panel.setAlignment(Pos.CENTER);
        
        Label lblEmpates = new Label("Empates");
        lblEmpates.setFont(new Font(14));
        lblEmpates.setTextFill(Color.WHITE);
        
        contadorEmpates = new Label("0");
        contadorEmpates.setFont(new Font(20));
        contadorEmpates.setTextFill(Color.WHITE);
        
        panel.getChildren().addAll(lblEmpates, contadorEmpates);
        return panel;
    }

    private Button crearBotonReinicio() {
        Button btn = new Button("Nuevo Juego");
        btn.setStyle("-fx-background-color: #9C27B0; -fx-text-fill: white; " +
                    "-fx-font-weight: bold; -fx-font-size: 14; -fx-padding: 10 20; " +
                    "-fx-background-radius: 20;");
        btn.setOnAction(e -> reiniciarJuegoCompleto());
        return btn;
    }

    private void configurarEscena(Stage stage, BorderPane root) {
        Scene scene = new Scene(root, 450, 650);
        stage.setScene(scene);
        stage.setTitle("Tic Tac Toe - Humano vs IA");
        stage.setResizable(false);
        stage.show();
    }
private void manejarClickCasilla(int pos) {
    // Movimiento humano
    if (!controller.hacerMovimientoHumano(pos)) {
        return;
    }
    
    actualizarInterfaz();
    
    if (controller.esFinal()) {
        verificarFinJuego();
        return;
    }
    
    // Movimiento IA
    int movimientoIA = controller.hacerMovimientoIA();
    if (movimientoIA != -1) {
        actualizarInterfaz();
        
        if (controller.esFinal()) {
            verificarFinJuego();
        }
    }
}

    private void actualizarInterfaz() {
        int[] tablero = controller.getTablero();
        
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int pos = i * 3 + j;
                Button btn = buttons[i][j];
                int valor = tablero[pos];
                
                if (valor == Controller.HUMANO) {
                    configurarBotonComoX(btn);
                } else if (valor == Controller.IA) {
                    configurarBotonComoO(btn);
                } else {
                    configurarBotonVacio(btn);
                }
                
                btn.setDisable(valor != 0);
            }
        }
    }

    private void configurarBotonComoX(Button btn) {
        if (imagenX != null) {
            ImageView iv = new ImageView(imagenX);
            iv.setFitWidth(60);
            iv.setFitHeight(60);
            btn.setGraphic(iv);
        } else {
            btn.setText("X");
            btn.setStyle("-fx-font-size: 36; -fx-font-weight: bold; -fx-text-fill: #4CAF50;");
        }
    }

    private void configurarBotonComoO(Button btn) {
        if (imagenO != null) {
            ImageView iv = new ImageView(imagenO);
            iv.setFitWidth(60);
            iv.setFitHeight(60);
            btn.setGraphic(iv);
        } else {
            btn.setText("O");
            btn.setStyle("-fx-font-size: 36; -fx-font-weight: bold; -fx-text-fill: #F44336;");
        }
    }

    private void configurarBotonVacio(Button btn) {
        btn.setGraphic(null);
        btn.setText("");
        btn.setStyle("-fx-background-color: rgba(255,255,255,0.2);");
    }

    private void verificarFinJuego() {
        int ganador = controller.verificarGanador();
        
        if (ganador != 0) {
            actualizarContadores(ganador);
            mostrarResultado(ganador);
            controller.reiniciarJuego();
            actualizarInterfaz();
        }
    }

    private void actualizarContadores(int ganador) {
        if (ganador == Controller.HUMANO) {
            victoriasHumanas++;
            contadorHumano.setText(String.valueOf(victoriasHumanas));
        } else if (ganador == Controller.IA) {
            victoriasIA++;
            contadorIA.setText(String.valueOf(victoriasIA));
        } else {
            empates++;
            contadorEmpates.setText(String.valueOf(empates));
        }
    }

    private void mostrarResultado(int ganador) {
        String mensaje;
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Fin del juego");
        alert.setHeaderText(null);
        
        if (ganador == Controller.HUMANO) {
            mensaje = "¡Ganaste!";
            alert.setContentText(mensaje);
        } else if (ganador == Controller.IA) {
            mensaje = "¡La IA ganó!";
            if (imagenGanador != null) {
                ImageView ivGanador = new ImageView(imagenGanador);
                ivGanador.setFitWidth(200);
                ivGanador.setFitHeight(200);
                alert.setGraphic(ivGanador);
            }
            alert.setContentText(mensaje);
        } else {
            mensaje = "¡Empate!";
            alert.setContentText(mensaje);
        }
        
        alert.showAndWait();
    }

    private void reiniciarJuegoCompleto() {
        controller.reiniciarJuego();
        victoriasHumanas = 0;
        victoriasIA = 0;
        empates = 0;
        contadorHumano.setText("0");
        contadorIA.setText("0");
        contadorEmpates.setText("0");
        actualizarInterfaz();
    }

    public static void main(String[] args) {
        launch(args);
    }
}