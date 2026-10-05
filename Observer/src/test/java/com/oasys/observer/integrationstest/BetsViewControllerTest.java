package com.oasys.observer.integrationstest;

import com.oasys.observer.logic.MethodHandler;
import com.oasys.observer.logic.PlayerData;
import com.oasys.observer.logic.StageBet;
import com.oasys.observer.presentation.BetsViewController;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BetsViewControllerTest {

    private BetsViewController betsViewController;

    @BeforeAll
    public static void initToolkit() {
        Platform.startup(() -> {});
    }

    @BeforeEach
    public void setUp() {
        betsViewController = new BetsViewController();
        betsViewController.winnerBetLabel = new Label();
        betsViewController.loserBetLabel = new Label();
        betsViewController.betsTable = new TableView<>();
    }

    @Test
    public void testSetPlayerData() {
        // Spieler-Daten erstellen
        PlayerData playerData = new PlayerData(1, "Alice", 100, 2, 3, 1, true, new MethodHandler());
        playerData.getStageBets().add(new StageBet("Blau", 5));
        playerData.getStageBets().add(new StageBet("Rot", 3));

        Platform.runLater(() -> betsViewController.setPlayerData(playerData));

        // JavaFX-Thread warten lassen!
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Überprüfen, ob die Labels korrekt sind
        assertEquals("2", betsViewController.winnerBetLabel.getText(), "Falscher Wert für WinnerBetLabel");
        assertEquals("3", betsViewController.loserBetLabel.getText(), "Falscher Wert für LoserBetLabel");

        // Überprüfen, ob die Tabelle korrekte Werte hat
        assertEquals(2, betsViewController.betsTable.getItems().size(), "Falsche Anzahl an Einträgen in der Tabelle");
        assertEquals("Blau", betsViewController.betsTable.getItems().get(0).getCamel(), "Erster Eintrag der Tabelle hat falsches Kamel");
        assertEquals(5, betsViewController.betsTable.getItems().get(0).getValue(), "Erster Eintrag der Tabelle hat falschen Wert");
    }
}