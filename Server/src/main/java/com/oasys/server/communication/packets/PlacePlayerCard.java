package com.oasys.server.communication.packets;

import java.util.Objects;

/**
 * The Packet PlacePlayerCard is sent from client to server, when a player card is placed.
 */
public class PlacePlayerCard extends Packet {
    private int spaceId;
    private int movingDirection;

    /**
     * Initializes an Object of PlacePlayerCard.
     * @param spaceId The ID of the boardspace where the Player Card is put.
     * @param movingDirection Informs whether number of boardspaces, that the camel moves, is extended(+1) or reduced(-1).
     * @throws IllegalArgumentException Is thrown, if moving direction is not either 1 or -1.
     */
    public PlacePlayerCard(int spaceId, int movingDirection) throws IllegalArgumentException {
        if (movingDirection == -1 || movingDirection == 1) {
            this.movingDirection = movingDirection;
        } else {
            throw new IllegalArgumentException("movingDirection can only be 1 or -1");
        }
        this.spaceId = spaceId;
    }

    /**
     * Gets space ID.
     * @return Space ID.
     */
    public int getSpaceId() {
        return this.spaceId;
    }

    /**
     * Sets Space ID.
     * @param spaceId ID of the board space that Player Card is put on.
     */
    public void setSpaceId(int spaceId) {
        this.spaceId = spaceId;
    }

    /**
     * Gets moving direction.
     * @return moving direction.
     */
    public int getMovingDirection() {
        return this.movingDirection;
    }

    /**
     * Sets moving direction.
     * @param movingDirection Informs whether number of bord spaces, that camel walks, are extended by one or reduced by
     * one.
     * @throws IllegalArgumentException If the moving direction is not 1 or -1.
     */
    public void setMovingDirection(int movingDirection) throws IllegalArgumentException {
        if (movingDirection == -1 || movingDirection == 1) {
            this.movingDirection = movingDirection;
        } else {
            throw new IllegalArgumentException("movingDirection can only be 1 or -1");
        }
        this.movingDirection = movingDirection;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlacePlayerCard that = (PlacePlayerCard) o;
        return spaceId == that.spaceId && movingDirection == that.movingDirection;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(spaceId);
    }

}
