package com.oasys.server.presentation;

import com.google.gson.JsonObject;
import com.oasys.server.logic.PlayerListManager;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


public class GameListView {
    private Stage stage;
    // Method to set the stage (so we can update it later)
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    //Table of configured games
    @FXML
    private TableView<Map<String, Object>> gameListTable;
    @FXML
    private TableColumn<Map<String, Object>, String> gameNameColumn;
    @FXML
    private TableColumn<Map<String, Object>, String> statusColumn;
    @FXML
    private TableColumn<Map<String, Object>, String> timerColumn; // New column for timer

    // Table with details of configured game
    @FXML
    private TableView<Map.Entry<String, Object>> gameConfigTable;
    @FXML
    private TableColumn<Map.Entry<String, Object>, String> configKeyColumn;
    @FXML
    private TableColumn<Map.Entry<String, Object>, String> configValueColumn;

    private Map<String, Timer> gameTimers = new HashMap<>(); // To track timers per game
    private Map<String, Integer> remainingTime = new HashMap<>(); // To track remaining time per game

    private ObservableList<Map<String, Object>> gameList = FXCollections.observableArrayList();
    private Map<String, Object> selectedGame;
    private Timer overallTimer;
    private Timer currentTurnTimer;
    private boolean gamePaused = false;
    private List<String> playerTurnOrder = new ArrayList<>();
    private List<String> selectedPlayers = new ArrayList<>();

    @FXML
    public void initialize() {
        gameNameColumn.setCellValueFactory(cellData ->
                new ReadOnlyStringWrapper(cellData.getValue().get("Spielname").toString())
        );
        statusColumn.setCellValueFactory(cellData ->
                new ReadOnlyStringWrapper(cellData.getValue().get("Status").toString())
        );

        timerColumn.setCellValueFactory(cellData -> {
            // Get the timer value from the game, default to 19 if it's null
            Integer timerValue = (Integer) cellData.getValue().get("Maximale Spieldauer");
            if (timerValue == null) {
                timerValue = 19; // Default to 19 seconds if no value is found
            }
            return new ReadOnlyStringWrapper(formatTime(timerValue)); // Format the time value
        });


        gameListTable.setItems(gameList);

        gameListTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedGame = newSelection;
                populateGameConfigTable(newSelection);
            }
        });

        configKeyColumn.setCellValueFactory(cellData ->
                new ReadOnlyStringWrapper(cellData.getValue().getKey())
        );
        configValueColumn.setCellValueFactory(cellData ->
                new ReadOnlyStringWrapper(cellData.getValue().getValue() != null ? cellData.getValue().getValue().toString() : "")
        );

        loadGames();
    }

    private void loadGames() {
        // Mocking game data, assume this comes from elsewhere (e.g., ConfigViewController)
        for (Map<String, Object> gameConfig : ConfigViewController.getGameConfigs()) {
            gameConfig.put("Status", "Wartet"); // Default status
            //System.out.println("Lobbies: " +ConfigViewController.lobbies.values());
            gameList.add(gameConfig);

        }
    }

    private void populateGameConfigTable(Map<String, Object> gameConfig) {
        ObservableList<Map.Entry<String, Object>> configEntries = FXCollections.observableArrayList(gameConfig.entrySet());
        gameConfigTable.setItems(configEntries);
    }
    @FXML
    private void randomizeTurnOrder(){
        if (selectedGame != null && selectedPlayers != null && !selectedPlayers.isEmpty()){
            Collections.shuffle(selectedPlayers);
            playerTurnOrder = new ArrayList<>(selectedPlayers);
            displayAlert("Turn order Randomzed", "The player turn order has been randomized.");
        }else {
            displayAlert("No Players Available", "No players have been added to this game.");
        }
    }

    private void displayAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

   /* @FXML
    private void startGame() {
        // Ask the server administrator to choose the player turn order and select participants
        chooseTurnOrderAndPlayers();

        // Randomize turn order if selected by the admin
        if (selectedGame.containsKey("Zugreihenfolge") && selectedGame.get("Zugreihenfolge").equals("Zufällig")) {
            Collections.shuffle(playerTurnOrder);
        }

        // Once the order is selected, set game status to 'Gestartet' and begin
        selectedGame.put("Status", "Gestartet");
        gameListTable.refresh();
        startTimers(); // Start timers for overall game time and current turn time
    }
*/

    //@FXML
    @FXML
    private void startGame() {
        // Define the maximum number of players allowed (fixed number)
        //int maxPlayers = 4;
        int maxPlayers = (Integer) selectedGame.get("Anzahl der Spieler");


        // List of all available players (replace with actual list)
        ArrayList<JsonObject> currentPlayerList = PlayerListManager.loadPlayerList();
        //local variable
        List<String> allPlayers = new ArrayList<>();
        for (int i = 0; i < currentPlayerList.size(); i++) {
            String playerName = currentPlayerList.get(i).get("name").getAsString();
            allPlayers.add(playerName);
            System.out.println(allPlayers);
        }
        //List<String> allPlayers = Arrays.asList("Player1", "Player2", "Player3", "Player4", "Player5", "Player6");

        // Create a ListView to display players with checkboxes
        ListView<CheckBox> listView = new ListView<>();
        List<CheckBox> checkboxes = new ArrayList<>();

        // Create a CheckBox for each player
        for (String player : allPlayers) {
            CheckBox checkBox = new CheckBox(player);
            checkBox.setUserData(player); // Set the player name as the userData for reference
            checkBox.setOnAction(event -> updateSelectedPlayers(listView, maxPlayers)); // Update selected players when clicked
            checkboxes.add(checkBox);
        }

        // Add the checkboxes to the ListView
        listView.getItems().addAll(checkboxes);

        // Create a dialog to display the ListView
        Dialog<List<String>> dialog = new Dialog<>();
        dialog.setTitle("Player Selection");
        dialog.setHeaderText("Select " + maxPlayers + " Players to Join the Game");
        dialog.getDialogPane().setContent(new VBox(listView));

        // Add "OK" and "Cancel" buttons to the dialog
        Button okButton = new Button("OK");
        okButton.setOnAction(e -> {
            // Collect the selected players (those with checked checkboxes)
            List<String> selectedPlayers = new ArrayList<>();
            for (CheckBox checkBox : checkboxes) {
                if (checkBox.isSelected()) {
                    selectedPlayers.add((String) checkBox.getUserData());
                }
            }

            // Ensure the number of selected players matches the maxPlayers limit
            if (selectedPlayers.size() == maxPlayers) {
                dialog.setResult(selectedPlayers);
                dialog.close(); // Close the dialog if selection is valid
            } else {
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("Player Selection Error");
                alert.setHeaderText("You must select exactly " + maxPlayers + " players.");
                alert.showAndWait();
            }
        });

        // Add OK and Cancel buttons to the dialog
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        // Set result converter
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                List<String> selectedPlayers = new ArrayList<>();
                for (CheckBox checkBox : checkboxes) {
                    if (checkBox.isSelected()) {
                        selectedPlayers.add((String) checkBox.getUserData());
                    }
                }
                return selectedPlayers;
            }
            return null;
        });

        // Show the dialog
        Optional<List<String>> result = dialog.showAndWait();
        if (result.isPresent()) {
            List<String> selectedPlayers = result.get();

            // Store the selected players and set the player turn order
            this.selectedPlayers = selectedPlayers;
            playerTurnOrder = new ArrayList<>(selectedPlayers); // Set the initial turn order based on selection

            // Now, ask for the turn order

            // Create a dialog for turn order selection
            chooseTurnOrder();
        }
    }

    @FXML
    private void chooseTurnOrder() {
        // Dialog for turn order selection
        Dialog<List<String>> turnOrderDialog = new Dialog<>();
        turnOrderDialog.setTitle("Select Turn Order");
        turnOrderDialog.setHeaderText("Please select the turn order of the players.");

        // ListView to display the selected players
        ListView<String> turnOrderListView = new ListView<>();
        turnOrderListView.getItems().addAll(playerTurnOrder);

        // Up button to move a player up in the list
        Button upButton = new Button("Move Up");
        upButton.setOnAction(event -> {
            int selectedIndex = turnOrderListView.getSelectionModel().getSelectedIndex();
            if (selectedIndex > 0) {
                // Swap players
                String selectedPlayer = turnOrderListView.getSelectionModel().getSelectedItem();
                turnOrderListView.getItems().remove(selectedIndex);
                turnOrderListView.getItems().add(selectedIndex - 1, selectedPlayer);
                turnOrderListView.getSelectionModel().select(selectedIndex - 1); // Update selection
            }
        });

        // Down button to move a player down in the list
        Button downButton = new Button("Move Down");
        downButton.setOnAction(event -> {
            int selectedIndex = turnOrderListView.getSelectionModel().getSelectedIndex();
            if (selectedIndex < turnOrderListView.getItems().size() - 1) {
                // Swap players
                String selectedPlayer = turnOrderListView.getSelectionModel().getSelectedItem();
                turnOrderListView.getItems().remove(selectedIndex);
                turnOrderListView.getItems().add(selectedIndex + 1, selectedPlayer);
                turnOrderListView.getSelectionModel().select(selectedIndex + 1); // Update selection
            }
        });

        //Button to randomize the player order
        Button randomButton = new Button("Random");
        randomButton.setOnAction(event -> {
            Collections.shuffle(turnOrderListView.getItems());
        });


        // Buttons to confirm or cancel
        Button okButton = new Button("OK");
        okButton.setOnAction(event -> {
            playerTurnOrder = turnOrderListView.getItems();  // Update turn order with the new list
            turnOrderDialog.setResult(playerTurnOrder);
            turnOrderDialog.close();
        });

        VBox vbox = new VBox(10, turnOrderListView, upButton, downButton, randomButton);
        vbox.setAlignment(Pos.CENTER);

        turnOrderDialog.getDialogPane().setContent(vbox);
        turnOrderDialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        // Show the dialog
        turnOrderDialog.showAndWait();

        // After selecting the turn order, update the game and start the timer
        selectedGame.put("Status", "Gestartet");
        gameListTable.refresh();
        startTimer(selectedGame); // Start the game timer
    }

    private void updateSelectedPlayers(ListView<CheckBox> listView, int maxPlayers) {
        long selectedCount = listView.getItems().stream().filter(CheckBox::isSelected).count();

        // Disable all unchecked checkboxes if the max number of players is reached
        for (CheckBox checkBox : listView.getItems()) {
            checkBox.setDisable(selectedCount >= maxPlayers && !checkBox.isSelected());
        }
    }

    private void chooseTurnOrderAndPlayers() {
        // Example: Assuming the administrator selects the player turn order and participants
        // In a real scenario, this should be a prompt in the UI
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Zugreihenfolge");
        alert.setHeaderText("Wählen Sie die Zugreihenfolge aus.");

        // Example: Static list of players
        List<String> allPlayers = Arrays.asList("Player1", "Player2", "Player3", "Player4", "Player5", "Player6");

        // Example: Server admin selects which players can join
        selectedPlayers = Arrays.asList("Player1", "Player2", "Player3");

        // Set up the player turn order (this could be randomized)
        playerTurnOrder = new ArrayList<>(selectedPlayers);

        // Check if there are more players than allowed in the game
        if (selectedPlayers.size() > 4) { // Assuming max 4 players for simplicity
            alert.setContentText("Es sind mehr Spieler als die maximal erlaubte Anzahl.");
            alert.showAndWait();
        }
    }

    private void startTimer(Map<String, Object> game) {
        String gameName = game.get("Spielname").toString();
        Integer timeLeft = remainingTime.get(gameName); // Get the remaining time (from pause or new game)

        if (timeLeft == null) {


            timeLeft =  (Integer) game.get("Maximale Spieldauer")  ;; // Default to 19 seconds if no previous time is saved
        }

        // Start the timer for the game
        remainingTime.put(gameName, timeLeft); // Update the map with the current time

        Timer timer = new Timer();
        gameTimers.put(gameName, timer);

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (!gamePaused) {
                    int remaining = remainingTime.get(gameName);

                    if (remaining > 0) {
                        remainingTime.put(gameName, remaining - 1);

                        Platform.runLater(() -> {
                            for (Map<String, Object> gameInList : gameList) {
                                if (gameInList.get("Spielname").equals(gameName)) {
                                    gameInList.put("Maximale Spieldauer", remaining - 1);
                                    gameListTable.refresh(); // Refresh to update the timer display
                                    break;
                                }
                            }
                        });
                    } else {
                        stopGame(gameName); // Stop when the timer reaches 0
                    }
                }
            }
        }, 0, 1000); // 1 second intervals
    }

    private void pauseTimer(Map<String, Object> game) {
        String gameName = game.get("Spielname").toString();
        Timer timer = gameTimers.get(gameName);
        if (timer != null) {
            timer.cancel(); // Stop the timer
        }
    }

    private void resumeTimer(Map<String, Object> game) {
        String gameName = game.get("Spielname").toString();
        Integer remaining = remainingTime.get(gameName); // Get the remaining time when the game was paused

        if (remaining != null && remaining > 0) {
            // Start the timer from the remaining time
            startTimer(game);
        }
    }

    private void stopGame(String gameName) {
        // Mark the game as stopped
        for (Map<String, Object> game : gameList) {
            if (game.get("Spielname").toString().equals(gameName)) {
                game.put("Status", "Abgebrochen");
                gameListTable.refresh(); // Refresh the table to update the status
                break;
            }
        }

        // Stop the timer and clean up
        pauseTimer(selectedGame);
    }

    // Format time as "00:00"
    private String formatTime(int seconds) {
        int minutes = seconds / 60;
        int remainingSeconds = seconds % 60;
        return String.format("%02d:%02d", minutes, remainingSeconds);
    }


    /*private void startTimers() {
        // Start the overall game timer and current turn timer
        overallTimer = new Timer();
        currentTurnTimer = new Timer();

        overallTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (!gamePaused) {
                    // Update overall time logic
                }
            }
        }, 0, 1000); // 1-second intervals

        currentTurnTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (!gamePaused) {
                    // Update current turn timer logic
                }
            }
        }, 0, 1000); // 1-second intervals
    }

    */

    /*@FXML
    private void pauseGame() {
        if (selectedGame != null) {
            selectedGame.put("Status", "Pausiert");
            gamePaused = true;
            gameListTable.refresh();
            pauseTimers(); // Pause the timers
        }
    }

    @FXML
    private void resumeGame() {
        if (selectedGame != null) {
            selectedGame.put("Status", "Fortgesetzt");
            gamePaused = false;
            gameListTable.refresh();
            resumeTimers(); // Resume the timers
        }
    }

    private void pauseTimers() {
        if (overallTimer != null) {
            overallTimer.cancel();
        }
        if (currentTurnTimer != null) {
            currentTurnTimer.cancel();
        }
    }

    private void resumeTimers() {
        startTimers(); // Restart timers from where they left off
    }*/

    @FXML
    private Label gameStatusLabel; // Declare the label for game status

    // Pause Game: Pauses the timer
    @FXML
    private void pauseGame() {
        if (selectedGame != null) {
            selectedGame.put("Status", "Pausiert");
            gamePaused = true;
            gameListTable.refresh();

            // Pause the timer
            pauseTimer(selectedGame);
        }
    }

    // Resume Game: Resumes the timer
    @FXML
    private void resumeGame() {
        if (selectedGame != null) {
            selectedGame.put("Status", "Fortgesetzt");
            gamePaused = false;
            gameListTable.refresh();

            // Resume the timer
            resumeTimer(selectedGame);
        }
    }

    @FXML
    private void cancelGame() {
        if (selectedGame != null) {
            // Set the game status to "Abgebrochen" (canceled)
            selectedGame.put("Status", "Abgebrochen");
            gameListTable.refresh();

            // Optionally, determine the winning order manually or automatically.
            // For simplicity, assume the players are in the turn order they were selected
            List<String> winningOrder = playerTurnOrder;

            // Create an Alert with a well-designed winning order
            Alert cancelAlert = new Alert(AlertType.INFORMATION);
            cancelAlert.setTitle("Game Canceled");
            cancelAlert.setHeaderText("The game has been canceled.");

            // Display the winning order in a clear and readable format
            StringBuilder winningOrderText = new StringBuilder("The winning order is: \n");
            for (int i = 0; i < winningOrder.size(); i++) {
                winningOrderText.append(i + 1).append(". ").append(winningOrder.get(i)).append("\n");
            }

            cancelAlert.setContentText(winningOrderText.toString());
            cancelAlert.showAndWait();

            // Cancel the game timers
            cancelTimers();
        }
    }

    private void cancelTimers() {
        if (overallTimer != null) {
            overallTimer.cancel();
        }
        if (currentTurnTimer != null) {
            currentTurnTimer.cancel();
        }
    }

    @FXML
    private void resetAllGames() {
        for (Map<String, Object> game : gameList) {
            game.put("Status", "Wartet");
            game.put("Timer", 19); // Reset the timer to 19 seconds
        }
        gameListTable.refresh();
    }

    @FXML
    private void addGame() {
        // Load ConfigView to add a new game
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("config-view.fxml"));
            Stage stage = (Stage) gameListTable.getScene().getWindow();
            stage.setScene(new Scene(fxmlLoader.load()));
            stage.setMaximized(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Helper methods
    private ObservableList<Map<String, Object>> getGamesByStatus(String status) {
        return FXCollections.observableArrayList(
                gameList.stream().filter(game -> game.get("Status").equals(status)).collect(Collectors.toList())
        );
    }



}
