/** BlockPoint is a node with a position and a weight. It is primarly used in
 * ChunkGenerator to generate positions to place plattforms.
 */
public class BlockPoint{
    public int x;
    public int y;
    /** Weight is used by chooseFurthestPoints to remember the cost to traverse
     * to a certain BlockPoint from another BlockPoint */
    public double weight = 0;

    public BlockPoint(int x, int y){
        this.x = x;
        this.y = y;
    }

    public double distanceTo(BlockPoint point){
        return Math.sqrt(Math.pow(x - point.x,2) + Math.pow(y - point.y,2));
    }

    public BlockPoint copy(){
        return new BlockPoint(this.x, this.y);
    }
}
