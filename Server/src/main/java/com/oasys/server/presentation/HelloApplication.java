package com.oasys.server.presentation;

import com.oasys.server.logic.LobbyManager;
import com.oasys.server.logic.PlayerListManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * The HelloApplication class serves as the entry point of the application and launches the JavaFX application.
 * It loads the start-view.fxml file and displayed the start view.
 */

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // Clear the lobbies file when the app starts
        LobbyManager.clearLobbiesFile();
        PlayerListManager.clearPlayerListSave();

        // Continue with the rest of your application initialization
        System.out.println("Application started and lobbies.json cleared!");
        // Load the FXML layout for the server configuration
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("GameListView.fxml"));
        Scene scene = new Scene(fxmlLoader.load()); // Set the window size to 600x500

        // Get the controller and set the stage
        GameListView controller = fxmlLoader.getController();
        controller.setStage(stage); // Set the stage in the controller

        // Set the title of the stage (window)
        stage.setTitle("Camel UP");
        // Set the scene on the stage and display it
        stage.setScene(scene);
        // Make the window take up the entire screen (maximized, but with window decorations)
        stage.setMaximized(true);
        stage.show();
    }

    public static void main(String[] args) {
        // Launch the JavaFX application
        launch();
    }
}
