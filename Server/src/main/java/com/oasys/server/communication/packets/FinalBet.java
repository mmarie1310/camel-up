package com.oasys.server.communication.packets;

import java.util.Objects;

/**
 * The Packet Finalbet is sent from client to server, if the next player decides to put
 *a bet on the first camel to win or on the last camel to lose.
 */
public class FinalBet extends Packet {
    private boolean isFirst;
    private int id;

    /**
     * Initializes an object of FinalBet.
     * @param isFirst Gives information whether a player put a bet on the winning camel(true) or losing camel(false).
     * @param id The ID of the camel that player put a bet on.
     */
    public FinalBet(boolean isFirst, int id) {
        this.isFirst = isFirst;
        this.id = id;
    }

    /**
     * Sets isFirst.
     * @param first Gives information if player bets, that this camel wins or loses.
     */
    public void setIsFirst(boolean first) {
        this.isFirst = first;
    }

    /**
     * Gets isFirst.
     * @return isFirst
     */
    public boolean getFirst() {
        return this.isFirst;
    }

    /**
     * Gets Camel's ID.
     * @return Camel's ID.
     */
    public int getId() {
         return this.id;
    }

    /**
     * Sets Camel's ID.
     * @param id Of the camel who was put a bet on.
     */
    public void setId(int id) {
         this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FinalBet finalBet = (FinalBet) o;
        return isFirst == finalBet.isFirst && id == finalBet.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }


}

