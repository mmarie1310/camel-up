package com.oasys.observer.communication.packets;

/**
 * This is sent from client to server and contains a list of all the currently available lobbies.
 */
public class RequestLobbyList extends Packet {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return true;
    }
}
