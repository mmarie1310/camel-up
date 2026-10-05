package com.oasys.server.communication.packets;

/**
 * The Packet MoveVisualized is sent from the client to the server, when the client is done with
 * the visualization of the last move. Only when all the clients sent a packet to the server
 * or the end of the visualizing time was reached, the next player can begin his next move.
 */
public class MoveVisualized extends Packet {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return true;
    }
}
