package vertical_plattformer;

public enum BlockType {
    AIR(false, false), PLATTFORM(true, false), PLAYER(false, false), HIGHJUMP(true, true), POWERJUMP(true, true),
    QUADJUMP(true, true);

    public final boolean SOLID;
    public final boolean POWERUP;

    private BlockType(boolean solid, boolean isPowerUp){
        SOLID = solid;
        POWERUP = isPowerUp;
    }
}
