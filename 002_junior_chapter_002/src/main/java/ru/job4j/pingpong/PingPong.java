package ru.job4j.pingpong;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

/**
 * Simple ping-pong movement of rectangle using JavaFX.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 23.04.2018
 */
public class PingPong extends Application {
    /**
     * Application title.
     */
    private static final String TITLE = "Пинг-понг www.job4j.ru";

    /**
     * Initializes and runs application.
     *
     * @param stage application window.
     */
    @Override
    public void start(Stage stage) {
        int limitX = 300;
        int limitY = 300;
        Group group = new Group();
        Rectangle rect = new Rectangle(50, 100, 25, 25);
        group.getChildren().add(rect);
        Thread movingRectangle = new Thread(new RectangleMove(rect, limitX, limitY));
        movingRectangle.start();
        stage.setScene(new Scene(group, limitX, limitY));
        stage.setTitle(TITLE);
        stage.setResizable(false);
        stage.show();
        stage.setOnCloseRequest(
                event -> movingRectangle.interrupt()
        );
    }


}