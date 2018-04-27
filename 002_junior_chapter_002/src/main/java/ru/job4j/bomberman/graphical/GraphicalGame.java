package ru.job4j.bomberman.graphical;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Pair;
import ru.job4j.bomberman.Board;
import ru.job4j.bomberman.Direction;
import ru.job4j.bomberman.Personage;
import ru.job4j.bomberman.WrongCoordinatesException;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GraphicalGame extends Application {
    private static final String HEADER = "Бомбермен www.job4j.ru";
    private final int width = 5;
    private final int height = 5;
    private final int nMonsters = 0;
    private final int nBlocks = 0;
    private final int cellSize = 100;
    private final GraphicalCell[][] cells = new GraphicalCell[width][height];

    @Override
    public void start(Stage stage) throws WrongCoordinatesException {
        Board board = new Board(this.width, this.height);
        Group group = new Group();
        this.createGrid(group);
        List<Pair<Integer, Integer>> blocks = this.createBlocks(board, group);
        GraphicalPersonage player = this.createPlayer(board, group);
        Thread[] monsters = this.createMonsters(board, group, this.nMonsters, blocks);
        this.setAndShowScene(stage, group, player);
        this.startThreads(monsters);
        this.setOnCloseAction(stage, monsters);
    }

    private List<Pair<Integer, Integer>> createBlocks(Board board, Group group) throws WrongCoordinatesException {
        List<Pair<Integer, Integer>> coords = this.createBlockCoordinates(this.nBlocks);
        this.lockBlocks(board, coords);
        this.createBlockRectangles(board, group, coords);
        return coords;
    }

    private List<Pair<Integer, Integer>> createBlockCoordinates(int number) {
        List<Pair<Integer, Integer>> result = new ArrayList<>(number);
        for (int i = 0; i < number; i++) {
            Pair<Integer, Integer> add;
            add = this.makeRandomCoordinatesNotOnBlock(result);
            result.add(add);
        }
        return result;
    }

    private Pair<Integer, Integer> makeRandomCoordinatesNotOnBlock(List<Pair<Integer, Integer>> blocks) {
        Random random = new Random();
        Pair<Integer, Integer> result;
        boolean onBlock = false;
        do {
            int x = random.nextInt(this.width);
            int y = random.nextInt(this.height);
            result = new Pair<>(x, y);
            for (Pair<Integer, Integer> block : blocks) {
                if (result.equals(block)) {
                    onBlock = true;
                    break;
                }
            }
        } while (onBlock);
        return result;
    }

    private void lockBlocks(Board board, List<Pair<Integer, Integer>> blocks) throws WrongCoordinatesException {
        for (Pair<Integer, Integer> block : blocks) {
            board.lock(block.getKey(), block.getValue());
        }
    }

    private void createBlockRectangles(Board board, Group group, List<Pair<Integer, Integer>> blocks) {
        GraphicalPersonage[] result = new GraphicalPersonage[blocks.size()];
        for (int i = 0; i < blocks.size(); i++) {
            int x = blocks.get(i).getKey();
            int y = blocks.get(i).getValue();
            Rectangle rectangle = new Rectangle(50, 100, 60, 60);
            rectangle.setFill(Color.BROWN);
            group.getChildren().add(rectangle);
            result[i] = new GraphicalPersonage(
                    board,
                    new Personage(board, i + this.nMonsters + 1, String.format("Block_%s", i), x, y),
                    rectangle, this.cells, this.cellSize
            );
            result[i].graphicTo(x, y);
        }
    }


    private GraphicalPersonage createPlayer(Board board, Group group) {
        Random random = new Random();
        Rectangle player = new Rectangle(50, 100, 40, 40);
        player.setFill(Color.BLACK);
        group.getChildren().add(player);
        int playerX = random.nextInt(this.width);
        int playerY = random.nextInt(this.height);
        GraphicalPersonage result = new GraphicalPersonage(
                board,
                new Personage(board, 0, "Player", playerX, playerY),
                player, this.cells, this.cellSize
        );
        result.graphicTo(playerX, playerY);
        return result;
    }

    private Thread[] createMonsters(Board board, Group group, int number, List<Pair<Integer, Integer>> blocks) {
        Rectangle[] rectangles = this.createMonsterRectangles(group, number);
        GraphicalPersonage[] gMonsters = this.createGraphicalMonsters(board, rectangles, blocks);
        return this.createGraphicalMonstersThreads(gMonsters);
    }

    private Rectangle[] createMonsterRectangles(Group group, int number) {
        Random random = new Random();
        Rectangle[] array = new Rectangle[number];
        for (int i = 0; i < array.length; i++) {
            array[i] = new Rectangle(50, 100, 20, 20);
            array[i].setFill(new Color(random.nextDouble(), random.nextDouble(), random.nextDouble(), 1));
            group.getChildren().add(array[i]);
        }
        return array;
    }

    private GraphicalPersonage[] createGraphicalMonsters(Board board, Rectangle[] rectangles, List<Pair<Integer, Integer>> blocks) {
        GraphicalPersonage[] gMonsters = new GraphicalPersonage[nMonsters];
        for (int i = 0; i < gMonsters.length; i++) {
            Pair<Integer, Integer> xy = this.makeRandomCoordinatesNotOnBlock(blocks);
            int randX = xy.getKey();
            int randY = xy.getValue();
            gMonsters[i] = new GraphicalPersonage(
                    board,
                    new Personage(board, i + 1, String.format("Monster_%s", (i + 1)), randX, randY),
                    rectangles[i],
                    this.cells, this.cellSize
            );
            gMonsters[i].graphicTo(randX, randY);
        }
        return gMonsters;
    }

    private Thread[] createGraphicalMonstersThreads(GraphicalPersonage[] gMonsters) {
        Thread[] result = new Thread[nMonsters];
        for (int i = 0; i < result.length; i++) {
            result[i] = new Thread(new RunMonster(gMonsters[i]));
        }
        return result;
    }

    private void createGrid(Group group) {
        BorderPane border = new BorderPane();
        border.setCenter(this.buildGrid());
        group.getChildren().add(border);
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

    private GraphicalCell buildCell(int x, int y, int size) {
        GraphicalCell cell = new GraphicalCell(x * size, y * size, size, size);
        cell.setFill(Color.WHITE);
        cell.setStroke(Color.BLACK);
        return cell;
    }

    //////////////////////////////////////////////////

    private void handleKeys(KeyCode key, GraphicalPersonage player) throws InterruptedException, WrongCoordinatesException {
        if (key == KeyCode.UP) {
            System.out.println("UP");
            player.tryMove(Direction.UP);
        } else if (key == KeyCode.RIGHT) {
            System.out.println("RIGHT");
            player.tryMove(Direction.RIGHT);
        } else if (key == KeyCode.DOWN) {
            System.out.println("DOWN");
            player.tryMove(Direction.DOWN);
        } else if (key == KeyCode.LEFT) {
            System.out.println("LEFT");
            player.tryMove(Direction.LEFT);
        }
    }

    //////////////////////////////////////////////////

    private void setAndShowScene(Stage stage, Group group, GraphicalPersonage player) {
        Scene scene = new Scene(group, this.width * this.cellSize, this.height * this.cellSize);

        /////////////////////////////////////////

        scene.setOnKeyPressed(event -> {
            try {
                this.handleKeys(event.getCode(), player);
                event.consume();
            } catch (InterruptedException | WrongCoordinatesException e) {
                System.out.println("OOPS!!!");
            }
        });

        /////////////////////////////////////////
        stage.setScene(scene);
        stage.setTitle(HEADER);
        stage.setResizable(false);
        stage.show();
    }

    private void startThreads(Thread[] monsters) {
        try {
            Thread.sleep(100);
            for (Thread m : monsters) {
                m.start();
                Thread.sleep(100);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void setOnCloseAction(Stage stage, Thread[] monsters) {
        stage.setOnCloseRequest(
                event -> {
                    for (Thread m : monsters) {
                        m.interrupt();
                    }
                }
        );
    }

}
