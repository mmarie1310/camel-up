package com.oasys.observer.unit_test;

import com.oasys.observer.logic.BoardEvent;
import com.oasys.observer.logic.BoardUpdater;
import com.oasys.observer.logic.GameTimer;
import com.oasys.observer.logic.MethodHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GameTimerTest {

    private GameTimer gameTimer;
    private MethodHandler methodHandler;
    private BoardUpdater boardUpdater;

    @BeforeEach
    public void setUp() {
        boardUpdater = mock(BoardUpdater.class);
        methodHandler = mock(MethodHandler.class);
        when(methodHandler.getBoardUpdater()).thenReturn(boardUpdater);

        // Initialisiere den GameTimer mit Testdaten
        gameTimer = new GameTimer(3000, 2000, 10000, methodHandler);
    }

    @Test
    public void testInitialization() {
        // Überprüfen, ob die Zeiten korrekt initialisiert werden
        assertEquals(3000, gameTimer.getThinkingTimeLeft(), "Thinking time wurde nicht korrekt initialisiert.");
        assertEquals(2000, gameTimer.getVisualizationTimeLeft(), "Visualization time wurde nicht korrekt initialisiert.");
        assertEquals(10000, gameTimer.getGameTimeLeft(), "Game time wurde nicht korrekt initialisiert.");
    }


    @Test
    public void testTimeAsString() {
        // Überprüfen der String-Repräsentation
        String thinkingTimeString = gameTimer.getThinkingTimeLeftAsString();
        String visualizationTimeString = gameTimer.getVisualizationTimeLeftAsString();
        String gameTimeString = gameTimer.getGameTimeLeftAsString();

        assertEquals("00:03.000", thinkingTimeString, "Thinking time string representation ist inkorrekt.");
        assertEquals("00:02.000", visualizationTimeString, "Visualization time string representation ist inkorrekt.");
        assertEquals("00:10.000", gameTimeString, "Game time string representation ist inkorrekt.");
    }

    @Test
    public void testStartGameLoop() throws InterruptedException {
        // Starte die Game Loop
        gameTimer.setThinkingTimeRunning(true);
        gameTimer.setGameTimeRunning(true);
        gameTimer.StartGameLoop();

        // Sicherzustellen, dass der Timer herunterzählt (Wartezeit > UPDATE_RATE * 5)
        Thread.sleep(600);

        int thinkingTimeLeft = gameTimer.getThinkingTimeLeft();
        int gameTimeLeft = gameTimer.getGameTimeLeft();

        assertTrue(thinkingTimeLeft < 3000, "Thinking time wurde nicht korrekt heruntergezählt.");
        assertTrue(gameTimeLeft < 10000, "Game time wurde nicht korrekt heruntergezählt.");

        // Timer stoppen
        gameTimer.setThinkingTimeRunning(false);
        gameTimer.setGameTimeRunning(false);
    }

    @Test
    public void testEventNotifications() throws InterruptedException {
        // Starte die Game Loop und aktiviere die Thinking Time
        gameTimer.setThinkingTimeRunning(true);
        gameTimer.StartGameLoop();

        Thread.sleep(500);

        verify(boardUpdater, atLeastOnce()).notify(BoardEvent.THINKING_TIME);
    }
}
