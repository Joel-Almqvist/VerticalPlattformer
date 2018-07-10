import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ChunkGenerator
{
    private int boardWidth;
    private int jumpHeight;
    private int minDistance;
    private Random random;
    private int plattformsPerChunk;

    public final static int JUMPHEIGHT = 8;
    public final static int MINDISTANCE = 3;
    public final static int INFINITY = 99999;

    ChunkGenerator(int boardWidth){
        this.boardWidth = boardWidth;
        this.jumpHeight = JUMPHEIGHT;
        this.minDistance = MINDISTANCE;
	this.random = new Random();
	this.plattformsPerChunk = 5;
    }

    public BlockType[][] generateChunk(BlockType[][] board){
        BlockType[][] returnChunk = new BlockType[jumpHeight-1][boardWidth];
        // Fill the new chunk with air
        for(int c = 0; c < boardWidth; c++){
            for(int r = 0; r < returnChunk.length; r++){
		returnChunk[r][c] = BlockType.AIR;
	    }
	}

	List<BlockPoint> upmostPlattforms = getTopPlattforms(board);
	List<BlockPoint> reachablePositions = getReachablePositions(returnChunk, upmostPlattforms);

	// TODO ADD FAILSAFE SUCH THAT REACHABLE POSITIONS NEVER CAN BE EMPTY



	List<BlockPoint> chosenBlockPositions = new ArrayList<>();
	for(BlockPoint start: reachablePositions){
	    for(BlockPoint dest: reachablePositions){
		double distance = start.distanceTo(dest);
		if(distance > start.weight){
		    start.weight = distance;
		}
		if(distance > dest.weight){
		    dest.weight = distance;
		}
	    }
	}

	List<BlockPoint> chosenPositions = chooseFurthestPoints(reachablePositions, plattformsPerChunk);


	/*  This code chooses platforms randomly from reachable positions


	// Choose a predefined amount of random positions
	List<BlockPoint> randomPositions = new ArrayList<>(plattformsPerChunk);
	for(int i = 0; i < this.plattformsPerChunk; i++){
	    int randomIndex = random.nextInt(reachablePositions.size()-1);
	    randomPositions.add(reachablePositions.get(randomIndex));
	    reachablePositions.remove(randomIndex);
	}
	*/


	// Set the randomly chosen blocks to plattform and if possible
	// also set their neighbors.
	for(BlockPoint pos : chosenPositions){
	    returnChunk[pos.y][pos.x] = BlockType.PLATTFORM;
	    if(pos.x > 0){
		returnChunk[pos.y][pos.x-1] = BlockType.PLATTFORM;
	    }
	    if(pos.x < boardWidth-1){
	    	returnChunk[pos.y][pos.x+1] = BlockType.PLATTFORM;
	    }
	}

	return returnChunk;

    }

    /**
     * Returns all positions of all plattforms existing on the highest row
     * on board with atleast one plattform.
     */
    private List<BlockPoint> getTopPlattforms(BlockType[][] board){
	// Find the upmost row with atleast one plattform in it
	// and save the position of all plattforms in said row.
	List<BlockPoint> topPlattforms = new ArrayList<>(boardWidth);
	for(int r = 0; r < board.length; r++){
             boolean foundTopRow = false;
             for(int c = 0; c < boardWidth; c++){
                 if(board[r][c] == BlockType.PLATTFORM){
 		    topPlattforms.add(new BlockPoint(c,r));
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
		    BlockPoint chunkPoint = new BlockPoint(c,r+heightOffset);
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

    /** Given a list of points this function iterates over these points and chooses
     * "amount"-amount of points within this list which are far away from each other.
     *
     * NOTE: This function uses a greedy algorithm and an optimal solution is neither guaranteed nor likely.
     *
     * The first point is chosen randomly and given that point the furthest point is chosen greedily
     * "amount"-amount of times.
     *
     * @param originalPoints - A list of points from which the function chooses points which are
     *                       far away from each other.
     *
     * @param amount - How many points far away from each other the function should find.
     *
     * @return A list of points which are far away from each other.
     */
    private List<BlockPoint> chooseFurthestPoints(List<BlockPoint> originalPoints, int amount){
        if(amount <= 0 || amount > originalPoints.size()){
            throw new IllegalArgumentException("Amount can not be negative or larger than originalPoints");
	}
	List<BlockPoint> startingPoints = new ArrayList<>(originalPoints);
        List<BlockPoint> chosenPoints = new ArrayList<>();
	int randomIndex = random.nextInt(startingPoints.size()-1);
	// Add the random starting position
        chosenPoints.add(startingPoints.get(randomIndex));
        // Remove it from the list since we can't add the same point many times
	startingPoints.remove(randomIndex);

	// Choose "amount"-amount of nodes
	for(int i = 0; i < amount; i++){
		if(startingPoints.isEmpty()){
		    break;
		}

	    // Set every consideredPositions weight to its lowest distance
	    // to any considered node
	    for(BlockPoint consideredPos : startingPoints){
		double globalMinDist = INFINITY;
		for(BlockPoint chosenPos : chosenPoints){
		    double currentMinDist = consideredPos.distanceTo(chosenPos);
		    if(currentMinDist < globalMinDist){
			globalMinDist = currentMinDist;
		    }
		}
		consideredPos.weight = globalMinDist;
	    }

	    // Choose the position with the highest weight and add/remove it.
	    double highestMinDist = -1;
	    BlockPoint chosenPoint = null;
	    for(BlockPoint consideredPos : startingPoints){
		if(highestMinDist < consideredPos.weight){
		    highestMinDist = consideredPos.weight;
		    chosenPoint = consideredPos;
		}
	    }
	    chosenPoints.add(chosenPoint.copy());
	    startingPoints.remove(chosenPoint);
	}
	return chosenPoints;
    }
}
