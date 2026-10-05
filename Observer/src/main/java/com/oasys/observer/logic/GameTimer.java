package com.oasys.observer.logic;

import javafx.application.Platform;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Controls the time logic.
 */
public class GameTimer {
    private final static int UPDATE_RATE = 100;
    private final int thinkingTime;
    private final int visualizationTime;
    private final int maxGameDuration;
    private final MethodHandler methodHandler;
    private Timer timer;
    private int thinkingTimeLeft = 0;
    private int visualizationTimeLeft = 0;
    private int gameTimeLeft = 0;
    private boolean thinkingTimeRunning = false;
    private boolean visualizationTimeRunning = false;
    private boolean gameTimeRunning = false;

    private boolean demoMode = true;
    private int demoRounds = 5;

    /**
     * @param thinkingTime      The maximum allowed time for the visualization of a move (in ms).
     * @param visualizationTime The maximum allowed time for the visualization of a move (in ms).
     * @param maxGameDuration   The maximum duration of the entire game (in ms).
     */
    public GameTimer(int thinkingTime, int visualizationTime, int maxGameDuration, MethodHandler methodHandler) {
        this.thinkingTime = thinkingTime;
        this.visualizationTime = visualizationTime;
        this.maxGameDuration = maxGameDuration;
        this.methodHandler = methodHandler;
        thinkingTimeLeft = thinkingTime;
        visualizationTimeLeft = visualizationTime;
        gameTimeLeft = maxGameDuration;
    }

    /**
     * Get the thinking time left.
     * @return
     */
    public int getThinkingTimeLeft() {
        return thinkingTimeLeft;
    }

    /**
     * Set the thinking time left.
     * @param thinkingTimeLeft
     */
    public void setThinkingTimeLeft(int thinkingTimeLeft) {
        this.thinkingTimeLeft = thinkingTimeLeft;
    }

    /**
     * Get the visualization time left.
     * @return
     */
    public int getVisualizationTimeLeft() {
        return visualizationTimeLeft;
    }

    /**
     * Set the visualization time left.
     * @param visualizationTimeLeft
     */
    public void setVisualizationTimeLeft(int visualizationTimeLeft) {
        this.visualizationTimeLeft = visualizationTimeLeft;
    }

    /**
     * get the game time left.
     * @return
     */
    public int getGameTimeLeft() {
        return gameTimeLeft;
    }

    /**
     * Set the game time left.
     * @param gameTimeLeft
     */
    public void setGameTimeLeft(int gameTimeLeft) {
        this.gameTimeLeft = gameTimeLeft;
    }

    /**
     * Returns true when the thinking time is running.
     * @return
     */
    public boolean isThinkingTimeRunning() {
        return thinkingTimeRunning;
    }

    /**
     * Set whether the thinking time is running.
     * @param thinkingTimeRunning
     */
    public void setThinkingTimeRunning(boolean thinkingTimeRunning) {
        this.thinkingTimeRunning = thinkingTimeRunning;
    }

    /**
     * Returns true when the visualization time is running.
     * @return
     */
    public boolean isVisualizationTimeRunning() {
        return visualizationTimeRunning;
    }

    /**
     * Set whether the visualization time is running.
     * @param visualizationTimeRunning
     */
    public void setVisualizationTimeRunning(boolean visualizationTimeRunning) {
        this.visualizationTimeRunning = visualizationTimeRunning;
    }

    /**
     * Returns true when the game time is running.
     * @return
     */
    public boolean isGameTimeRunning() {
        return gameTimeRunning;
    }

    /**
     * Set whether the game time is running.
     * @param gameTimeRunning
     */
    public void setGameTimeRunning(boolean gameTimeRunning) {
        this.gameTimeRunning = gameTimeRunning;
    }

    /**
     * Starts the game loop.
     */
    public void StartGameLoop(){
        timer = new Timer();
        TimerTask gameUpdate = new TimerTask() {
            @Override
            public void run() {
                if (thinkingTimeRunning) {
                    if (thinkingTimeLeft > UPDATE_RATE) {
                        thinkingTimeLeft = thinkingTimeLeft - UPDATE_RATE;
                    }
                    else {
                        thinkingTimeLeft = 0;

             /*           if (demoMode && demoRounds > 0) {
                            demoRounds--;
                            Platform.runLater(() ->
                                    methodHandler.getBoardUpdater().notify(BoardEvent.ROUND_FINISH));
                            thinkingTimeLeft = thinkingTime;
                        }   */
                    }
                    methodHandler.getBoardUpdater().notify(BoardEvent.THINKING_TIME);
                }

                if (visualizationTimeRunning) {
                    if (visualizationTimeLeft > UPDATE_RATE) {
                        visualizationTimeLeft = visualizationTimeLeft - UPDATE_RATE;
                    }
                    else {
                        visualizationTimeLeft = 0;
                    }
                    methodHandler.getBoardUpdater().notify(BoardEvent.VISUAL_TIME);
                }

                if (gameTimeRunning) {
                    if (gameTimeLeft > UPDATE_RATE) {
                        gameTimeLeft = gameTimeLeft - UPDATE_RATE;
                    }
                    else {
                        gameTimeLeft = 0;
                    }
                    methodHandler.getBoardUpdater().notify(BoardEvent.GAME_TIME);
                }
            }
        };
        timer.scheduleAtFixedRate(gameUpdate, 0, UPDATE_RATE);
    }

    /**
     * Get a time as a presentable string.
     * @param time The time to be shown in ms.
     * @return The time as a presentable string.
     */
    public String getTimeAsString(int time){
        int minutes = (int) Math.floor((double) time / 60000);
        int seconds = (int) Math.floor((double) time % 60000 / 1000);
        int ms = (int) time % 1000;
        return String.format("%02d:%02d.%03d", minutes, seconds, ms);
    }

    public void stopTimer(){
        timer.cancel();
    }

    /**
     * Get the thinking time left as a presentable string.
     * @return The thinking time left as a presentable string.
     */
    public String getThinkingTimeLeftAsString(){
        return getTimeAsString(thinkingTimeLeft);
    }

    /**
     * Get the visualization time left as a presentable string.
     * @return The visualization time left as a presentable string.
     */
    public String getVisualizationTimeLeftAsString(){
        return getTimeAsString(visualizationTimeLeft);
    }

    /**
     * Get the game time left as a presentable string.
     * @return The game time left as a presentable string.
     */
    public String getGameTimeLeftAsString(){
        return getTimeAsString(gameTimeLeft);
    }
}