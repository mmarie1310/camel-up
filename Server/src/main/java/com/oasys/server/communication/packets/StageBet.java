package com.oasys.server.communication.packets;

import java.util.Objects;

/**
 * This message is sent, f a player decides to bet on a camel to win.
 */
public class StageBet extends Packet {
    private int camelId;

    /**
     * Initializes an object of StageBet.
     * @param camelId Is the ID of the camel the player decided to put a bet on.
     */
    public StageBet(int camelId) {
        this.camelId = camelId;
    }

    /**
     * Gets CamelId
     * @return CamelId
     */
    public int getCamelId() {
        return camelId;
    }

    /**
     * Sets CamelId.
     * @param camelId Is the ID of the camel the player decided to put a bet on.
     */
    public void setCamelId(int camelId) {
        this.camelId = camelId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StageBet stageBet = (StageBet) o;
        return camelId == stageBet.camelId;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(camelId);
    }
}
