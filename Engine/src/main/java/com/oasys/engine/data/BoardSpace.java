package com.oasys.engine.data;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Represents a space (field) on the board.
 */
public class BoardSpace {
    private int spaceId;

    //TODO be careful when parsing, is not allowed to be null by interface (might need to be changed in interface?)
    private ArrayList<Integer> camelIds;
    private PlayerCard playerCard;

    /**
     * Initializes a BoardSpace.
     * @param spaceId The unique identifier of the field.
     * @param camelIds The IDs of the camels that are on the field. The earlier in the list,
     *                 the lower is the camel in the stack of camels on the field.
     * @param playerCard The player card that is on the field.
     */
    public BoardSpace(int spaceId, ArrayList<Integer> camelIds, PlayerCard playerCard) { //TODO be careful when parsing, maybe not all values given
        this.spaceId = spaceId;
        this.camelIds = camelIds;
        this.playerCard = playerCard;
    }

    /**
     * Initializes a BoardSpace with no player card.
     * @param spaceId The unique identifier of the field.
     * @param camelIds The IDs of the camels that are on the field. The earlier in the list,
     *                 the lower is the camel in the stack of camels on the field.
     */
    public BoardSpace(int spaceId, ArrayList<Integer> camelIds) {
        this.spaceId = spaceId;
        this.camelIds = camelIds;
        this.playerCard = null;
    }

    /**
     * Initializes a BoardSpace with no camels
     * @param spaceId The unique identifier of the field.
     * @param playerCard The player card that is on the field.
     */
    public BoardSpace(int spaceId, PlayerCard playerCard) {
        this.spaceId = spaceId;
        this.camelIds = new ArrayList<>();
        this.playerCard = playerCard;
    }

    /**
     * Initializes an empty BoardSpace.
     * @param spaceId The unique identifier of the field.
     */
    public BoardSpace(int spaceId) {
        this.spaceId = spaceId;
        this.camelIds = new ArrayList<>();
        this.playerCard = null;
    }

    /**
     * Gets the ID of the field.
     * @return The unique identifier of the field.
     */
    public int getSpaceId() {
        return this.spaceId;
    }

    /**
     * Sets the ID of the field.
     * @param spaceId The unique identifier of the field.
     */
    public void setSpaceId(int spaceId) {
        this.spaceId = spaceId;
    }

    /**
     * Gets the IDs of the camels that stand on the field.
     * @return The IDs of the camels that stand on the field.
     */
    public ArrayList<Integer> getCamelIds() {
        return this.camelIds;
    }

    /**
     * Sets the IDs of the camels that stand on the field.
     * @param camelIds The IDs of the camels that stand on the field.
     */
    public void setCamelIds(ArrayList<Integer> camelIds) {
        this.camelIds = camelIds;
    }

    /**
     * Gets the player card which might be on the field.
     * @return The player card which might be on the field.
     */
    public PlayerCard getPlayerCard() {
        return this.playerCard;
    }

    /**
     * Sets the player card of the field.
     * @param playerCard Sets the player card of the field.
     */
    public void setPlayerCard(PlayerCard playerCard) {
        this.playerCard = playerCard;
    }

    /**
     * Checks equality of another object with the board space.
     * @param o Another object.
     * @return True iff the ID is identical.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BoardSpace that = (BoardSpace) o;
        return getSpaceId() == that.getSpaceId();
    }

    /**
     * Hashes the board space by using its ID.
     * @return The hash value.
     */
    @Override
    public int hashCode() {
        return Objects.hash(getSpaceId());
    }
}
