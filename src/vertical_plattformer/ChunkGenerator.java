package vertical_plattformer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ChunkGenerator {
    private int boardWidth;
    private int maxDistance;
    private int minDistance;
    private Random random;
    private int plattformsPerChunk = 4;
    /** How high the chunkgenerator assumes the player can jump when the game starts.*/
    public final static int EXPECTED_STARTING_JUMPHEIGHT = 8;
    /** The shortest distance a plattform must be from a highest place to be considered "reachable" */
    public final static int MINDISTANCE = 3;
    public final static int INFINITY = Integer.MAX_VALUE;

    /** A list containing one object of every powerup BlockType */
    private List<BlockType> allPowerUps;

    ChunkGenerator(int boardWidth){
        this.boardWidth = boardWidth;
        this.maxDistance = EXPECTED_STARTING_JUMPHEIGHT;
        this.minDistance = MINDISTANCE;
	this.random = new Random();
	this.allPowerUps = new ArrayList<>();

	for(BlockType block : BlockType.values()){
	    if(block.POWERUP){
	        allPowerUps.add(block);
	    }
	}
    }

    /** Given a chunk generate a new chunk such that all plattforms within the new one is
     * MINDISTANCE and EXPECTED_STARTING_JUMPHEIGHT-1 distance away from an upmost position position of the input
     * chunk.
     *
     * NOTE: This function does not guarantee a "solveable" chunk for the player but makes it
     * unlikely that such a chunk would be generated.
     *
     * NOTE: inputChunk's index grows downwards while the generated chunk index grows upwards,
     * see the drawing below for clarification. The generatedChunks row 0 is put atop of inputChunk row 0.
     *
     *GeneratedChunk:
     * Row 2*******
     * Row 1*******
     * Row 0*******
     *
     * InputChunk:
     * Row 0*******
     * Row 1*******
     * Row 2*******
     *
     * @param inputChunk The original state of the game upon which the generated chunk
     *                   is going to be appended to.
     *
     * @return A new chunk to append to the inputChunk.
     */
    public BlockType[][] generateChunk(BlockType[][] inputChunk){
        // A chunk must never be higher than the player can jump
        BlockType[][] returnChunk = new BlockType[EXPECTED_STARTING_JUMPHEIGHT-1][boardWidth];
        // Fill the new chunk with air
        for(int c = 0; c < boardWidth; c++){
            for(int r = 0; r < returnChunk.length; r++){
		returnChunk[r][c] = BlockType.AIR;
	    }
	}
	// Find all plattforms on the highest row with a plattform
	// Find all positions reachable from these upmost plattforms
	// Make a suitable selections of the reachable positions to avoid plattforms bunching up
	List<BlockPoint> upmostPlattforms = getTopPlattforms(inputChunk);
	List<BlockPoint> reachablePositions = getReachablePositions(returnChunk, upmostPlattforms);
	if(reachablePositions.isEmpty()){
	    System.out.println("No reachable positions");
	}
	List<BlockPoint> chosenPositions = chooseFurthestPoints(reachablePositions);


	// Set the randomly chosen blocks to plattform or powerup randomly,
	// and if possible also set their neighbors.
	for(BlockPoint pos : chosenPositions){
	    BlockType randomBlock = randomBlock();
	    returnChunk[pos.y][pos.x] = randomBlock;
	    if(pos.x > 0){
		returnChunk[pos.y][pos.x-1] = randomBlock;
	    }
	    if(pos.x < boardWidth-1){
	    	returnChunk[pos.y][pos.x+1] = randomBlock;
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
                 if(board[r][c].SOLID){
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
		    if(pos.distanceTo(chunkPoint) < maxDistance - 1 && pos.distanceTo(chunkPoint) >= minDistance){
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
     * "plattformsPerChunk"-amount of points within this list which are far away from each other.
     *
     * NOTE: This function uses a greedy algorithm and an optimal solution is neither guaranteed nor likely.
     *
     * The first point is chosen randomly and given that point the furthest point is chosen greedily
     * "amount"-amount of times.
     *
     * @param originalPoints - A list of points from which the function chooses points which are
     *                       far away from each other.
     *
     * @return A list of points which are far away from each other.
     */
    private List<BlockPoint> chooseFurthestPoints(List<BlockPoint> originalPoints){
        if(plattformsPerChunk <= 0 || plattformsPerChunk > originalPoints.size()){
            throw new IllegalArgumentException("plattformsPerChunk can not be negative or larger than originalPoints");
	}
	List<BlockPoint> startingPoints = new ArrayList<>(originalPoints);
        List<BlockPoint> chosenPoints = new ArrayList<>();
	int randomIndex = random.nextInt(startingPoints.size()-1);
	// Add the random starting position
        chosenPoints.add(startingPoints.get(randomIndex));
        // Remove it from the list since we can't add the same point many times
	startingPoints.remove(randomIndex);

	// Choose "plattformsPerChunk - 1" amount of plattforms other than the starting point
	for(int i = 0; i < plattformsPerChunk-1; i++){
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

    public void lowerAmountOfPlattforms(){
        if(plattformsPerChunk > 1 ){
            plattformsPerChunk--;
	}
    }

    // TODO redo this function when its needed again
    public void increaseMaxDist(int topBound){
        if(maxDistance < topBound){
            maxDistance++;
	}
    }

    /** Returns a random powerup BlockType 10% of time and
     * the PLATTFORM blocktype in the other cases.
     *
     * @return A powerup BlockType or a PLATTFORM BlockType.
     */
    private BlockType randomBlock(){
        // Check whether a powerup should be chosen or not
        if(random.nextInt(4) == 0){
            // Return a random powerup
            return allPowerUps.get(random.nextInt(allPowerUps.size()));
	}
	return BlockType.PLATTFORM;
    }
}
