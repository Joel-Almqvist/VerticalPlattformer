public enum BlockType {
    AIR(false), PLATTFORM(true), PLAYER(true);

    public final boolean SOLID;

    private BlockType(boolean solid){
        SOLID = solid;
    }

}
