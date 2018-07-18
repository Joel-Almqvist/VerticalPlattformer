package vertical_plattformer;

/**
 * BlockType contains all different types of blocks which populate the board. A block can be solid
 * or non-solid indicating whether the player can stand on it. A BlockType can also be a powerup and
 * every powerup needs their own BlockType.
 */
public enum BlockType {
    /** Air is the most common blocktype and is seen by the player as empty space*/
    AIR(false, false),
    /** The Plattform BlockType is a regular plattform which the player can stand on*/
    PLATTFORM(true, false),
    /** A BlockType indicating that the player is standing within said block*/
    PLAYER(false, false),
    /** A BlockType for the powerup HighJump*/
    HIGHJUMP(true, true),
    /** A BlockType for the powerup PowerJump*/
    POWERJUMP(true, true),
    /** A BlockType for the powerup QuadJump*/
    QUADJUMP(true, true);

    /** Indicates whether a block blocks normal jumps and if the player can stand on it*/
    public final boolean SOLID;
    /** Indicates whether said block is a powerup or not*/
    public final boolean POWERUP;

    private BlockType(boolean solid, boolean isPowerUp){
        SOLID = solid;
        POWERUP = isPowerUp;
    }
}
