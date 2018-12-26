package ru.job4j.tictactoe;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

/**
 * Initialization and play part of tic-tac-toe game.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 22.04.2018
 */
public class TicTacToe extends Application {
    /**
     * Window header.
     */
    private static final String HEADER = "Крестики-нолики www.job4j.ru";
    /**
     * Field length and width (in cells).
     */
    private final int size = 3;
    /**
     * Playing table of cells.
     */
    private final Figure3T[][] cells = new Figure3T[size][size];
    /**
     * Logical operations performer.
     */
    private final Logic3T logic = new Logic3T(cells);

    /**
     * Returns empty cell (without mark).
     *
     * @param x    x coordinate of the upper-left corner of the cell.
     * @param y    y coordinate of the upper-left corner of the cell.
     * @param size size of cell.
     * @return new cell of defined place and size.
     */
    private Figure3T buildRectangle(int x, int y, int size) {
        Figure3T rect = new Figure3T();
        rect.setX(x * size);
        rect.setY(y * size);
        rect.setHeight(size);
        rect.setWidth(size);
        rect.setFill(Color.WHITE);
        rect.setStroke(Color.BLACK);
        return rect;
    }

    /**
     * Returns cell with "O" mark.
     *
     * @param x    x coordinate of the upper-left corner of the cell.
     * @param y    y coordinate of the upper-left corner of the cell.
     * @param size size of cell.
     * @return new cell of defined place and size and marked with "O".
     */
    private Group buildMarkO(double x, double y, int size) {
        Group group = new Group();
        int radius = size / 2;
        Circle circle = new Circle(x + radius, y + radius, radius - 10);
        circle.setStroke(Color.BLACK);
        circle.setFill(Color.WHITE);
        group.getChildren().add(circle);
        return group;
    }

    /**
     * Returns cell with "X" mark.
     *
     * @param x    x coordinate of the upper-left corner of the cell.
     * @param y    y coordinate of the upper-left corner of the cell.
     * @param size size of cell.
     * @return new cell of defined place and size and marked with "X".
     */
    private Group buildMarkX(double x, double y, int size) {
        Group group = new Group();
        group.getChildren().addAll(
                new Line(
                        x + 10, y + 10,
                        x + size - 10, y + size - 10
                ),
                new Line(
                        x + size - 10, y + 10,
                        x + 10, y + size - 10
                )
        );
        return group;
    }

    /**
     * Shows alert modal window with some message.
     *
     * @param message message to show.
     */
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(HEADER);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Checks if there still are empty cells and
     * the game can continue. If not, shows message.
     *
     * @return <tt>true</tt> if game can be continued, <tt>false</tt> if not.
     */
    private boolean canContinue() {
        boolean gap = this.logic.hasGap();
        if (!gap) {
            this.showAlert("Все поля заполнены! Начните новую Игру!");
        }
        return gap;
    }

    /**
     * Check if someone ("X" or "O") won the game. If found
     * winner, shows message.
     */
    private void checkWinner() {
        if (this.logic.isWinnerX()) {
            this.showAlert("Победили Крестики! Начните новую Игру!");
        } else if (this.logic.isWinnerO()) {
            this.showAlert("Победили Нолики! Начните новую Игру!");
        }
    }

    /**
     * Returns new handler on mouse button click: sets "X"
     * on left click, sets "O" on right click.
     *
     * @param panel grid of cells where mouse clicks are used.
     * @return new mouse clicks handler: sets "X"
     * on left click, sets "O" on right click.
     */
    private EventHandler<MouseEvent> buildMouseEvent(Group panel) {
        return event -> {
            Figure3T rect = (Figure3T) event.getTarget();
            if (this.canContinue()) {
                if (event.getButton() == MouseButton.PRIMARY) {
                    rect.setMark(true);
                    panel.getChildren().add(
                            this.buildMarkX(rect.getX(), rect.getY(), 50)
                    );
                } else {
                    rect.setMark(false);
                    panel.getChildren().add(
                            this.buildMarkO(rect.getX(), rect.getY(), 50)
                    );
                }
                this.checkWinner();
            }
        };
    }

    /**
     * Returns grid of cells working with
     * mouse buttons clicks.
     *
     * @return grid of active cells.
     */
    private Group buildGrid() {
        Group panel = new Group();
        for (int y = 0; y != this.size; y++) {
            for (int x = 0; x != this.size; x++) {
                Figure3T rect = this.buildRectangle(x, y, 50);
                this.cells[y][x] = rect;
                panel.getChildren().add(rect);
                rect.setOnMouseClicked(this.buildMouseEvent(panel));
            }
        }
        return panel;
    }

    /**
     * Initializes and runs the program.
     *
     * @param stage program window.
     */
    @Override
    public void start(Stage stage) {
        BorderPane border = new BorderPane();
        HBox control = new HBox();
        control.setPrefHeight(40);
        control.setSpacing(10.0);
        control.setAlignment(Pos.BASELINE_CENTER);
        Button start = new Button("Начать");
        start.setOnMouseClicked(
                event -> border.setCenter(this.buildGrid())
        );
        control.getChildren().addAll(start);
        border.setBottom(control);
        border.setCenter(this.buildGrid());
        stage.setScene(new Scene(border, 300, 300));
        stage.setTitle(HEADER);
        stage.setResizable(false);
        stage.show();
    }
}
