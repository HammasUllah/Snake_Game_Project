package com.example.snakegameproject;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.input.KeyCode;
import java.util.ArrayList;
import java.util.Random;


public class SnakeGame extends Application {

    private static final int WIDTH = 600;
    private static final int HEIGHT = 400;
    private static final int TILE_SIZE = 22;
    private static final int SPEED = 6;

    private ArrayList<Circle> snake = new ArrayList<>();
    private Circle food = new Circle(TILE_SIZE / 2, Color.RED);
    private String direction = "RIGHT";
    private boolean running = true;
    private int score = 0;

    private Text scoreText = new Text("Score: 0");
    private Text gameOverText = new Text("Game Over!!! "+"Your Score is \n: "+score +" Press R to Restart");

    private Pane pane = new Pane();

    @Override
    public void start(Stage stage) {
        pane.setPrefSize(WIDTH, HEIGHT);

        pane.setStyle("-fx-background-color: #2e2b28;");


        Circle head = new Circle(WIDTH / 2, HEIGHT / 2, TILE_SIZE / 2, Color.GREEN);
        snake.add(head);
        pane.getChildren().add(head);
        generateFood();

        scoreText.setFill(Color.WHITE);
        scoreText.setX(20);
        scoreText.setY(30);
        pane.getChildren().add(scoreText);

        gameOverText.setFill(Color.WHITE);
        gameOverText.setX(WIDTH / 2 -100);
        gameOverText.setY(HEIGHT /2 );
        gameOverText.setVisible(false);
        pane.getChildren().add(gameOverText);

        AnimationTimer timer = new AnimationTimer() {
            private long lastUpdateTime = 0;

            @Override
            public void handle(long now) {
                if (running && now - lastUpdateTime >= 1000000000 / SPEED) {
                    lastUpdateTime = now;
                    update();
                }
            }
        };

        timer.start();

        Scene scene = new Scene(pane);
        scene.setOnKeyPressed(e -> {
            KeyCode code = e.getCode();
            if (code == KeyCode.UP && !direction.equals("DOWN")) direction = "UP";
            else if (code == KeyCode.DOWN && !direction.equals("UP")) direction = "DOWN";
            else if (code == KeyCode.LEFT && !direction.equals("RIGHT")) direction = "LEFT";
            else if (code == KeyCode.RIGHT && !direction.equals("LEFT")) direction = "RIGHT";
            else if (code == KeyCode.R && !running) restartGame();
        });

        stage.setScene(scene);
        stage.setTitle("Snake Game");
        stage.show();
    }

    private void update() {
        Circle head = snake.get(0);
        head.setFill(Color.WHITE);

        Circle newHead = new Circle(TILE_SIZE / 2, Color.GREEN);
        switch (direction) {
            case "UP": newHead.setCenterX(head.getCenterX()); newHead.setCenterY(head.getCenterY() - TILE_SIZE); break;
            case "DOWN": newHead.setCenterX(head.getCenterX()); newHead.setCenterY(head.getCenterY() + TILE_SIZE); break;
            case "LEFT": newHead.setCenterX(head.getCenterX() - TILE_SIZE); newHead.setCenterY(head.getCenterY()); break;
            case "RIGHT": newHead.setCenterX(head.getCenterX() + TILE_SIZE); newHead.setCenterY(head.getCenterY()); break;
        }

        if (newHead.getCenterX() < 0) newHead.setCenterX(WIDTH - TILE_SIZE / 2);
        if (newHead.getCenterX() >= WIDTH) newHead.setCenterX(TILE_SIZE / 2);
        if (newHead.getCenterY() < 0) newHead.setCenterY(HEIGHT - TILE_SIZE / 2);
        if (newHead.getCenterY() >= HEIGHT) newHead.setCenterY(TILE_SIZE / 2);

        snake.add(0, newHead);
        pane.getChildren().add(newHead);

        if (Math.abs(newHead.getCenterX() - food.getCenterX()) < TILE_SIZE &&
                Math.abs(newHead.getCenterY() - food.getCenterY()) < TILE_SIZE) {
            score++;
            updateScore();
            generateFood();
        } else {
            Circle tail = snake.remove(snake.size() - 1);
            pane.getChildren().remove(tail);
        }

        if (isCollidingWithSelf()) {
            running = false;
            displayGameOver();
        }
    }
    private void displayGameOver() {
        gameOverText.setText("Game Over!!! "+"Your Score is: "+score +"\n\n         Press R to Restart");
        gameOverText.setVisible(true);
    }


    private boolean isCollidingWithSelf() {
        Circle head = snake.get(0);

        for (int i = 1; i < snake.size(); i++) {
            if (head.getCenterX() == snake.get(i).getCenterX() && head.getCenterY() == snake.get(i).getCenterY()) {
                return true;
            }
        }
        return false;
    }

    private void generateFood() {
        Random rand = new Random();
        food.setCenterX(rand.nextInt(WIDTH / TILE_SIZE) * TILE_SIZE + TILE_SIZE / 2);
        food.setCenterY(rand.nextInt(HEIGHT / TILE_SIZE) * TILE_SIZE + TILE_SIZE / 2);
        pane.getChildren().remove(food);
        pane.getChildren().add(food);
    }

    private void updateScore() {
        scoreText.setText("Score: " + score);
    }

    private void restartGame() {
        snake.clear();
        pane.getChildren().clear();
        score = 0;
        direction = "RIGHT";
        running = true;
        scoreText.setText("Score: " + score);
        gameOverText.setVisible(false);

        Circle head = new Circle(WIDTH / 2, HEIGHT / 2, TILE_SIZE / 2, Color.GREEN);
        snake.add(head);
        pane.getChildren().add(head);

        generateFood();
    }



    public static void main(String[] args) {
        launch();
    }
}
