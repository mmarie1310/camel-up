package com.oasys.observer.logic;

/**
 * Each observant has to implement this interface to receive event notifications.
 */
public interface EventListener {
    /**
     * React to an event notification.
     */
    void update(BoardEvent event);
}
