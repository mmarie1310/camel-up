package com.oasys.observer.integrationstest;

import com.oasys.observer.data.BettingCard;
import com.oasys.observer.data.Player;
import com.oasys.observer.logic.BoardUpdater;
import com.oasys.observer.logic.DataManager;
import com.oasys.observer.logic.PlayerData;
import com.oasys.observer.logic.MethodHandler;
import com.oasys.observer.presentation.PlayerCard;
import com.oasys.observer.presentation.PlayerCardController;
import javafx.application.Platform;
import javafx.scene.layout.HBox;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class PlayerCardControllerTest {

    private PlayerCardController playerCardController;
    private MethodHandler methodHandler;

    @BeforeAll
    public static void initToolkit() {
        FxTestSupport.initializeToolkit();
    }

    @BeforeEach
    public void setUp() {
        // Mock
        methodHandler = Mockito.mock(MethodHandler.class);

        BoardUpdater mockBoardUpdater = Mockito.mock(BoardUpdater.class);
        when(methodHandler.getBoardUpdater()).thenReturn(mockBoardUpdater);

        DataManager mockDataManager = Mockito.mock(DataManager.class);
        when(methodHandler.getDataManager()).thenReturn(mockDataManager);

        List<PlayerData> mockPlayers = new ArrayList<>();
        mockPlayers.add(new PlayerData(1, "Test_User_1", 100, 2, 3, 1, true, methodHandler));
        mockPlayers.add(new PlayerData(2, "Test_User_2", 200, 2, 1, 2, false, methodHandler));

        when(mockDataManager.getPlayerDataList()).thenReturn(new ArrayList<>(mockPlayers));

        // Initialisiere den PlayerCardController
        playerCardController = new PlayerCardController(methodHandler);
        playerCardController.setPlayerCardBox(new HBox()); // Verwendung des Setters
        playerCardController.setPlayers(mockPlayers);
    }

    @Test
    public void testAddPlayerData() {
        PlayerData playerData = new PlayerData(
                1,
                "Test_User_1",
                100,
                2,
                3,
                1,
                true,
                methodHandler
        );

        Platform.runLater(() -> playerCardController.addPlayerData(playerData));
        waitForJavaFX();

        assertEquals(3, playerCardController.getPlayerCardBox().getChildren().size(), // Verwendung des Getters
                "PlayerCard wurde nicht korrekt zur playerCardBox hinzugefügt.");
    }

    @Test
    public void testUpdatePlayerCards() {
        List<PlayerData> mockPlayers = new ArrayList<>();
        mockPlayers.add(new PlayerData(1, "Test_User_1", 100, 2, 3, 1, true, methodHandler));
        mockPlayers.add(new PlayerData(2, "Test_User_2", 200, 2, 1, 2, false, methodHandler));

        when(methodHandler.getDataManager().getPlayerDataList()).thenReturn(new ArrayList<>(mockPlayers));

        Platform.runLater(() -> playerCardController.updatePlayerCards());
        waitForJavaFX();

        assertEquals(2, playerCardController.getPlayerCardBox().getChildren().size(),
                "PlayerCards wurden nicht korrekt aktualisiert.");
    }


    private void waitForJavaFX() {
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
