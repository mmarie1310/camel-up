package com.oasys.observer.logic;


import javafx.application.Platform;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The observer, as defined in the observer pattern.
 */
public class BoardUpdater
{
    private final Map<BoardEvent, List<EventListener>> listeners;

    /**
     * Initialize the subject of the observer pattern.
     */
    public BoardUpdater() {
        listeners = new HashMap<BoardEvent, List<EventListener>>();
        for (BoardEvent value : BoardEvent.values()) {
            listeners.put(value, new ArrayList<EventListener>());
        }
    }

    /**
     * Subscribe to receiving notifications on an event.
     * @param event The event to subscribe to.
     * @param listener A reference to the subscribing observer object.
     */
    public void subscribe(BoardEvent event, EventListener listener) {
        listeners.get(event).add(listener);
    }

    /**
     * Unsubscribe from receiving notifications to an event.
     * @param event The event to unsubscribe from.
     * @param listener A reference to the unsubscribing object.
     */
    public void unsubscribe(BoardEvent event, EventListener listener) {
        listeners.get(event).remove(listener);
    }

    /**
     * Remove all subscribed listeners.
     */
    public void unsubscribeAll() {
        for (BoardEvent value : BoardEvent.values()) {
            listeners.get(value).clear();
        }
        for (BoardEvent value : BoardEvent.values()) {
            listeners.put(value, new ArrayList<EventListener>());
        }
    }

    /**
     * Unsubscribe a listener from all events.
     * @param listener
     */
    public void unsubscribeAll(EventListener listener) {
        for (BoardEvent value : BoardEvent.values()) {
            unsubscribe(value, listener);
        }
    }

    /**
     * Notifies all listeners who are subscribed to a specified event.
     * @param event The event to notify the listeners for.
     */
    public void notify(BoardEvent event) {
        for (EventListener listener : listeners.get(event)) {
            Platform.runLater(() ->
                    listener.update(event));
        }
    }
}
