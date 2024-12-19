package com.example.snakegameproject;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class LoginMenu extends Application {


    private List<User> users = new ArrayList<>();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        loadUsers();
//        Image image = new Image("C:\\Users\\Home\\Pictures\\presentation\\downlaod.png");
//        StackPane stackPane = new StackPane();
//       // stackPane.getChildren().add(image);
//        primaryStage.getIcons().add(image);
        primaryStage.setResizable(false);
        showLoginScreen(primaryStage);
    }

    private void loadUsers() {
        try (BufferedReader reader = new BufferedReader(new FileReader("user.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");
                if (parts.length == 2) {
                    users.add(new User(parts[0], parts[1]));
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading user data: " + e.getMessage());
        }
    }

    private void saveUser(String username, String password) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("user.txt", true))) {
            writer.write(username + ":" + password);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Error saving user data: " + e.getMessage());
        }
    }

    private boolean userExists(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    private boolean validateUser(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }

    private void showLoginScreen(Stage stage) {
        GridPane grid = createStyledGrid();

        Label title = createStyledLabel("Login to Snake Game", 20, Color.WHITE);
        Label usernameLabel = createStyledLabel("Username:", 16, Color.WHITE);
        Label passwordLabel = createStyledLabel("Password:", 16, Color.WHITE);

        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter username");
        usernameField.setStyle("-fx-background-color: #b49159; -fx-text-fill: white;");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter password");
        passwordField.setStyle("-fx-background-color: #b49159; -fx-text-fill: white;");

        Button loginButton = createStyledButton("Login", Color.GREEN);
        Button signUpButton = createStyledButton("Sign Up", Color.DODGERBLUE);
        Label messageLabel = createStyledLabel("", 14, Color.RED);

        loginButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();

            if (validateUser(username, password)) {
                messageLabel.setText("Login successful!");
                SnakeGame game = new SnakeGame();
                game.start(stage);
                System.out.println("Logged in successfully!");
            } else {
                messageLabel.setText("Invalid username. Please sign up.");
            }
        });

        signUpButton.setOnAction(e -> showSignUpScreen(stage));

        VBox vbox = new VBox(10, title, grid, loginButton, signUpButton, messageLabel);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(20));

        grid.add(usernameLabel, 0, 0);
        grid.add(usernameField, 1, 0);
        grid.add(passwordLabel, 0, 1);
        grid.add(passwordField, 1, 1);

        Scene scene = createStyledScene(vbox, 400, 400);
        stage.setScene(scene);
        stage.setTitle("Snake Game Login");
        stage.show();
    }

    private void showSignUpScreen(Stage stage) {
        GridPane grid = createStyledGrid();

        Label title = createStyledLabel("Sign Up", 20, Color.WHITE);
        Label usernameLabel = createStyledLabel("Username:", 16, Color.WHITE);
        Label passwordLabel = createStyledLabel("Password:", 16, Color.WHITE);

        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter username");
        usernameField.setStyle("-fx-background-color: #333; -fx-text-fill: white;");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter password");
        passwordField.setStyle("-fx-background-color: #333; -fx-text-fill: white;");

        Button submitButton = createStyledButton("Submit", Color.GREEN);
        Button backButton = createStyledButton("Back", Color.RED);
        Label messageLabel = createStyledLabel("", 14, Color.RED);

        submitButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();

            if (userExists(username)) {
                messageLabel.setText("User already exists. Please log in.");
            } else {
                users.add(new User(username, password));
                saveUser(username, password);
                messageLabel.setText("User saved. You can now log in.");
            }
        });

        backButton.setOnAction(e -> showLoginScreen(stage));

        VBox vbox = new VBox(10, title, grid, submitButton, backButton, messageLabel);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(20));

        grid.add(usernameLabel, 0, 0);
        grid.add(usernameField, 1, 0);
        grid.add(passwordLabel, 0, 1);
        grid.add(passwordField, 1, 1);

        Scene scene = createStyledScene(vbox, 400, 300);
        stage.setScene(scene);
        stage.setTitle("Sign Up");
    }

    private GridPane createStyledGrid() {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setStyle("-fx-background-color: #222;");
        return grid;
    }

    private Label createStyledLabel(String text, int fontSize, Color color) {
        Label label = new Label(text);
        label.setFont(Font.font("Segoe UI", FontWeight.BOLD, fontSize));
        label.setTextFill(color);
        return label;
    }

    private Button createStyledButton(String text, Color color) {
        Button button = new Button(text);
        button.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 14));
        button.setStyle("-fx-background-color: " + toString(color) + "; -fx-text-fill: white; -fx-background-radius: 10;");
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: white; -fx-text-fill: " + toString(color) + "; -fx-background-radius: 10;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: " + toString(color) + "; -fx-text-fill: white; -fx-background-radius: 10;"));
        return button;
    }

    private Scene createStyledScene(Region content, int width, int height) {
        StackPane root = new StackPane(content);
        root.setStyle("-fx-background-color: #b49159;");
        return new Scene(root, width, height);
    }

    private String toString(Color color) {
        return "#" + color.toString().substring(2, 8).toUpperCase();
    }

    private static class User {
        private final String username;
        private final String password;

        public User(String username, String password) {
            this.username = username;
            this.password = password;
        }

        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }
    }
}
