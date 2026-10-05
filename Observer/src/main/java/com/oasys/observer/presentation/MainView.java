package com.oasys.observer.presentation;

import com.oasys.observer.logic.MethodHandler;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * The main class of this software.
 */
public class MainView extends Application {

    private static Stage stage;
    private SceneController sceneController;
    private MethodHandler methodHandler;

    /**
     * Starts the software.
     * @param args Main args.
     */
    public static void main(String[] args) {
        launch();
    }

    /**
     * Initializes the main stage window.
     * @param stage The main stage.
     * @throws IOException If stage setup fails.
     */
    @Override
    public void start(Stage stage) throws IOException {
        this.stage = stage;
        this.methodHandler = new MethodHandler();
        this.sceneController = new SceneController(stage, methodHandler);

        String title = "Camel Up Observer";
        stage.setTitle(title);

        stage.setWidth(1280);
        stage.setHeight(720);

        stage.setOnCloseRequest(event -> {sceneController.closeApplication();});

        sceneController.showJoinServerMenu();
    }

    /**
     * Returns the stage used for the main window of the application.
     * @return Stage
     */
    public static Stage getStage() {
        return stage;
    }

}