public class BlockPoint{
    public int x;
    public int y;
    public BlockType type;

    public BlockPoint(int x, int y, BlockType type){
        this.x = x;
        this.y = y;
        this.type = type;
    }

    public double distanceTo(BlockPoint point){
        return Math.sqrt(Math.pow(x - point.x,2) + Math.pow(y - point.y,2));
    }
}
