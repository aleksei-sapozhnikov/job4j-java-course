package ru.job4j.bomberman.graphical;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import ru.job4j.bomberman.Board;
import ru.job4j.bomberman.Personage;

import java.util.Random;

public class Graphics extends Application {
    private static final String HEADER = "Бомбермен www.job4j.ru";
    private final int width = 6;
    private final int height = 5;
    private final int cellSize = 100;
    private final GraphicalCell[][] cells = new GraphicalCell[width][height];

    private GraphicalCell buildCell(int x, int y, int size) {
        GraphicalCell cell = new GraphicalCell(x * size, y * size, size, size);
        cell.setFill(Color.WHITE);
        cell.setStroke(Color.BLACK);
        return cell;
    }

    private Group buildGrid() {
        Group panel = new Group();
        for (int x = 0; x < this.width; x++) {
            for (int y = 0; y < this.height; y++) {
                GraphicalCell cell = this.buildCell(x, y, this.cellSize);
                this.cells[x][y] = cell;
                panel.getChildren().add(cell);
            }
        }
        return panel;
    }

    @Override
    public void start(Stage stage) {
        Board board = new Board(this.width, this.height);
        int nMonsters = 10;
        Random random = new Random();
        // set grid
        Group group = new Group();
        BorderPane border = new BorderPane();
        border.setCenter(this.buildGrid());
        group.getChildren().add(border);
        //add player
        Rectangle player = new Rectangle(50, 100, 40, 40);
        player.setFill(Color.BLACK);
        group.getChildren().add(player);
        Thread runPlayer = new Thread(new RunGraphicalPersonage(new GraphicalPersonage(
                new Personage(board, 0, "Player", random.nextInt(this.width), random.nextInt(this.height)), player, this.cells, this.cellSize
        )));
//         rectangles for monsters
        Rectangle[] monsters = new Rectangle[nMonsters];
        for (int i = 0; i < monsters.length; i++) {
            monsters[i] = new Rectangle(50, 100, 25, 25);
            monsters[i].setFill(new Color(random.nextDouble(), random.nextDouble(), random.nextDouble(), 1));
            group.getChildren().add(monsters[i]);
        }
        // add monsters threads
        Thread[] runsMonsters = new Thread[nMonsters];
        for (int i = 0; i < runsMonsters.length; i++) {
            runsMonsters[i] = new Thread(new RunGraphicalPersonage(new GraphicalPersonage(
                    new Personage(board, i + 1, String.format("Monster_%s", (i + 1)), random.nextInt(this.width), random.nextInt(this.height)),
                    monsters[i], this.cells, this.cellSize
            )));
        }
        // set scene
        stage.setScene(new Scene(group, 600, 600));
        stage.setTitle(HEADER);
        stage.setResizable(false);
        // start everyone
        try {
            runPlayer.start();
            Thread.sleep(100);
            for (Thread monst : runsMonsters) {
                monst.start();
                Thread.sleep(100);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // show everything
        stage.show();
        // on close
        stage.setOnCloseRequest(
                event -> {
                    runPlayer.interrupt();
                    for (Thread monst : runsMonsters) {
                        monst.interrupt();
                    }
                }
        );
    }

}
