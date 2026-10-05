package com.oasys.observer.integrationstest;

import com.oasys.observer.logic.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import javafx.application.Platform;

public class DataManagerEventHandlingTest {

    private DataManager dataManager;
    private BoardUpdater boardUpdater;
    private MethodHandler methodHandler;
    private boolean camelUpdateNotified;

    @BeforeEach
    public void setUp() {
        FxTestSupport.initializeToolkit();

        boardUpdater = new BoardUpdater();
        methodHandler = new MethodHandler();
        dataManager = new DataManager(boardUpdater, methodHandler);

        // Listener für das CAMEL-Event
        camelUpdateNotified = false;
        boardUpdater.subscribe(BoardEvent.CAMEL, event -> camelUpdateNotified = true);
    }

    @Test
    public void testNotifyCamelEvent() throws InterruptedException {
        // Manuelles Auslösen eines CAMEL-Events
        Platform.runLater(() -> boardUpdater.notify(BoardEvent.CAMEL)); // sicherstellen, dass es im richtigen Thread läuft

        Thread.sleep(100);

        // Überprüfen, ob die Benachrichtigung korrekt registriert wurde
        assertTrue(camelUpdateNotified, "CAMEL-Event wurde nicht korrekt benachrichtigt.");
    }

    @Test
    public void testEventListener() throws InterruptedException {
        EventListener testListener = event -> assertEquals(BoardEvent.CAMEL, event, "Falsches Event empfangen!");


        boardUpdater.subscribe(BoardEvent.CAMEL, testListener);

        // Manuelles Auslösen eines Events
        Platform.runLater(() -> boardUpdater.notify(BoardEvent.CAMEL)); // sicherstellen, dass es im richtigen Thread läuft


        Thread.sleep(100);
    }

    @Test
    public void testAllBoardEvents() throws InterruptedException {
        // Listener hinzufügen, der für jedes Event überprüft wird
        for (BoardEvent event : BoardEvent.values()) {
            final boolean[] eventNotified = {false};
            boardUpdater.subscribe(event, receivedEvent -> eventNotified[0] = true);

            Platform.runLater(() -> boardUpdater.notify(event));

            Thread.sleep(100);

            assertTrue(eventNotified[0], event + " wurde nicht korrekt benachrichtigt.");
        }
    }
}
