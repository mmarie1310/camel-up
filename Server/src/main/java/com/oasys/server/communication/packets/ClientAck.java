package com.oasys.server.communication.packets;

import java.util.Objects;

/**
 * The packet ClientAck is sent from the server to the client in order to provide the client ID.
 */
public class ClientAck extends Packet {
    private int clientId;

    /**
     * Initializes an object of ClientAck.
     * @param clientId The ID of the Client. Must be non-negative.
     * @throws IllegalArgumentException If the client ID is negative.
     */
    public ClientAck(int clientId) throws IllegalArgumentException {
        if (clientId < 0) {
            throw new IllegalArgumentException("clientId must be non-negative");
        }
        this.clientId = clientId;
    }

    /**
     * Gets the Client ID.
     * @return The Client ID.
     */
    public int getClientId() {
        return this.clientId;
    }

    /**
     * Sets the client ID.
     * @param clientId The ID of the Client. Must be non-negative.
     * @throws IllegalArgumentException If the client ID is negative.
     */
    public void setClientId(int clientId) throws IllegalArgumentException {
        if (clientId < 0) {
            throw new IllegalArgumentException("clientId must be non-negative");
        }
        this.clientId = clientId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClientAck clientAck = (ClientAck) o;
        return clientId == clientAck.clientId;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(clientId);
    }
}
