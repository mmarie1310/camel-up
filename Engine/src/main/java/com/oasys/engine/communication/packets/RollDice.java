package com.oasys.engine.communication.packets;

/**
 * This packet is sent from client to server, if player in turn decides to throw dice.
 */
public class RollDice extends Packet {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return true;
    }
}
