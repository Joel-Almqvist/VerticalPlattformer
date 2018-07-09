import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PlattformGenerator{
    private int boardWidth;
    private int jumpHeight;
    private int minDistance;
    private Random random;
    private int plattformsPerChunk;

    public final static int JUMPHEIGHT = 8;
    public final static int MINDISTANCE = 3;

    PlattformGenerator(int boardWidth){
        this.boardWidth = boardWidth;
        this.jumpHeight = JUMPHEIGHT;
        this.minDistance = MINDISTANCE;
	this.random = new Random();
	this.plattformsPerChunk = 5;
    }

    public BlockType[][] generateChunk(BlockType[][] board){
        BlockType[][] returnChunk = new BlockType[jumpHeight-1][boardWidth];
        // Fill the chunk with air
        for(int c = 0; c < boardWidth; c++){
            for(int r = 0; r < returnChunk.length; r++){
		returnChunk[r][c] = BlockType.AIR;
	    }
	}

	List<BlockPoint> upmostPlattforms = getTopPlattforms(board);
	List<BlockPoint> reachablePositions = getReachablePositions(returnChunk, upmostPlattforms);

	// TODO ADD FAILSAFE SUCH THAT REACHABLE POSITIONS NEVER CAN BE EMPTY



	// Choose a predefined amount of random positions
	List<BlockPoint> randomPositions = new ArrayList<>(plattformsPerChunk+1);
	for(int i = 0; i < this.plattformsPerChunk; i++){
	    int randomIndex = random.nextInt(reachablePositions.size()-1);
	    randomPositions.add(reachablePositions.get(randomIndex));
	    reachablePositions.remove(randomIndex);
	}


	// Set the randomly chosen blocks to plattform and if possible
	// also set their neighbors.
	for(BlockPoint pos : randomPositions){
	    returnChunk[pos.y][pos.x] = BlockType.PLATTFORM;
	    if(pos.x > 0){
		returnChunk[pos.y][pos.x-1] = BlockType.PLATTFORM;
	    }
	    if(pos.x < boardWidth-1){
	    	returnChunk[pos.y][pos.x+1] = BlockType.PLATTFORM;
	    }
	}

	/*
	for(int i = 0; i < returnChunk.length; i++){
	    for(int j = 0; j < returnChunk[0].length; j++){
		System.out.println(returnChunk[i][j]);
	    }
	}
	*/
	//returnChunk[0][0] = BlockType.PLAYER;
	return returnChunk;

    }

    /**
     * Returns all positions of all plattforms existing on the highest row
     * on board with atleast one plattform.
     */
    private List<BlockPoint> getTopPlattforms(BlockType[][] board){
	// Find the upmost row with atleast one plattform in it
	// and save the position of all plattforms in said row.
	List<BlockPoint> topPlattforms = new ArrayList<>(boardWidth+1);
	for(int r = 0; r < board.length; r++){
             boolean foundTopRow = false;
             for(int c = 0; c < boardWidth; c++){
                 if(board[r][c] == BlockType.PLATTFORM){
 		    topPlattforms.add(new BlockPoint(c,r,BlockType.PLATTFORM));
 		    foundTopRow = true;
 		}
 	    }
 	    if(foundTopRow) break;
 	}
        return topPlattforms;
    }


    /** Finds all reachable positions within the given chunk which are atleast minDistance
     *  away from an upmost position.
     *
     * @param chunk The chunk to which the positions relate to
     * @param topPlattforms A list with positions of all the currently upmost plattforms within
     *                      the main board. (They are neccesarily on the same row)
     *
     * @return A list of all the positions within chunk which the player can reach and which are
     * atleast of distance minDistance.
     */
    private List<BlockPoint> getReachablePositions(BlockType[][] chunk, List<BlockPoint> topPlattforms){
	List<BlockPoint> reachablePositions = new ArrayList<>();
	// The distance between the highest plattform within the main board
	// and the start of the chunk.
	int heightOffset = topPlattforms.get(0).y+1;
	for(BlockPoint pos : topPlattforms){
	    for(int c = 0; c < boardWidth; c++){
		for(int r = 0; r < chunk.length; r++){
		    BlockPoint chunkPoint = new BlockPoint(c,r+heightOffset,null);
		    if(pos.distanceTo(chunkPoint) < jumpHeight -1 && pos.distanceTo(chunkPoint) >= minDistance){
		        // Remove the offset so we save the actuall position within the chunk
		        chunkPoint.y -= heightOffset;
		        reachablePositions.add(chunkPoint);
		    }
		}
	    }
	}
	return reachablePositions;
    }
}
