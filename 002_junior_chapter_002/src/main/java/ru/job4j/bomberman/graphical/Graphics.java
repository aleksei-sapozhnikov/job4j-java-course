package ru.job4j.bomberman.graphical;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import ru.job4j.bomberman.Board;
import ru.job4j.bomberman.Character;

public class Graphics extends Application {
    private static final String HEADER = "Бомбермен www.job4j.ru";
    private final int width = 6;
    private final int height = 5;
    private final int cellSize = 100;
    private final GraphicCell[][] cells = new GraphicCell[width][height];

    private GraphicCell buildCell(int x, int y, int size) {
        GraphicCell cell = new GraphicCell(x * size, y * size, size, size);
        cell.setFill(Color.WHITE);
        cell.setStroke(Color.BLACK);
        return cell;
    }

    private Group buildGrid() {
        Group panel = new Group();
        for (int x = 0; x < this.width; x++) {
            for (int y = 0; y < this.height; y++) {
                GraphicCell cell = this.buildCell(x, y, this.cellSize);
                this.cells[x][y] = cell;
                panel.getChildren().add(cell);
            }
        }
        return panel;
    }

    @Override
    public void start(Stage stage) {
        BorderPane border = new BorderPane();
        border.setCenter(this.buildGrid());

        Rectangle rect = new Rectangle(50, 100, 25, 25);

        Group group = new Group();
        group.getChildren().add(border);
        group.getChildren().add(rect);

        Board board = new Board(this.width, this.height);
        Thread mover = new Thread(new Mover(new Character(board, 1, "Player", 0, 0), rect, this.cells, this.cellSize));

        stage.setScene(new Scene(group, 600, 600));
        stage.setTitle(HEADER);
        stage.setResizable(false);
        stage.show();

        mover.start();

        stage.setOnCloseRequest(
                event -> mover.interrupt()
        );
    }

}
