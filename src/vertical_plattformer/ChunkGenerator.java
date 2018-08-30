package vertical_plattformer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static vertical_plattformer.Config.*;

/** ChunkGenerator is responsible for creating "chunks" which are small extensions of the board to be
 * appended at the top of the board whenever a shift occurs. ChunkGenerator may create more difficult
 * or more easy chunks by changing:
 * 	1 - How many plattforms is within said chunk
 * 	2 - The height of a chunk
 * 	3 - The minimum distance between the boards highest point and the next chunk's plattforms.
 *
 * ChunkGenerator provides an interface for modifying these parameters, it is called from
 * Game -> ChunkHandler -> ChunkGenerator by the levelUp() function originaly.
 *
 */
public class ChunkGenerator {
    private int boardWidth;
    private Random random;
    /** Infinity is used in one of the functions */
    public final static int INFINITY = Integer.MAX_VALUE;

    /** These 4 fields/constants may be modified to generate more difficult chunks as long as the
     * following restrictions are upheld:
     *
     *	1: minDistance < MAX_DISTANCE or the pattern of plattforms generated
     *   	may be trivial and non random.
     *
     *  2: chunkHeight < MAX_DISTANCE or getReachablePositions() will provide the empty list
     *  	due to finding no reachable positions.
     *
     *  3: MAX_DISTANCE < playerJumpHeight, else unsolveable chunks may be created.
     *
     *  NOTE: A high amount of plattforms may create an unsolveable chunk due to blocking the player.
     *
     */
    private double minDistance = STARTING_MIN_DISTANCE;
    private int chunkHeight = STARTING_CHUNK_HEIGHT;
    private int plattformsPerChunk = STARTING_PLATTFORMS_PER_CHUNK;


    /** A list containing one object of every powerup BlockType */
    private List<BlockType> allPowerUps;

    ChunkGenerator(int boardWidth){
        this.boardWidth = boardWidth;
	this.random = new Random();
	this.allPowerUps = new ArrayList<>();


	for(BlockType block : BlockType.values()){
	    if(block.powerup){
	        allPowerUps.add(block);
	    }
	}
    }

    /** Given an input chunk generate a new chunk to be appended on top of the original one.
     *
     * A new chunk is generated in the following steps:
     *
     *  1 - Find the highest plattforms in the input chunk
     *
     *  2 - Find all points in the newly created chunk reachable from the points found in step (1)
     *
     *  3 - Choose one point from step (2) randomly, and (amountOfPlattforms - 1) amount of points
     *  as far away from the randomly chosen point as possible (while still being reachable).
     *
     *	4 - Make the points given by step (3) into powerup-plattforms with 10% probability and make the
     *	points to their immediate left/right into the same BlockType as them.
     *
     * For more details of step (1) - (3) see their corresponding function.
     *
     * NOTE: The inputChunk is expected to grow downwards (the same way board, or most matrixes do)
     * whereas the newly generated chunk will grow upwards.
     *
     * Example:
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
     *                   is going to be appended to. This parameter can be the entire board.
     *
     * @return A new chunk to be append on top of the inputChunk.
     */
    public BlockType[][] generateChunk(BlockType[][] inputChunk){
        BlockType[][] returnChunk = new BlockType[chunkHeight][boardWidth];
        // Fill the new chunk with air initially
        for(int c = 0; c < boardWidth; c++){
            for(int r = 0; r < returnChunk.length; r++){
		returnChunk[r][c] = BlockType.AIR;
	    }
	}


	List<BlockPoint> upmostPlattforms = getTopPlattforms(inputChunk);
	List<BlockPoint> reachablePositions = getReachablePositions(returnChunk, upmostPlattforms);
	List<BlockPoint> chosenPositions = chooseFurthestPoints(reachablePositions);


	// Set the randomly chosen blocks to plattform or powerup randomly,
	// and if possible also set their neighbors.
	for(BlockPoint pos : chosenPositions){
	    BlockType randomBlock = randomBlock();
	    returnChunk[pos.y][pos.x] = randomBlock;

	    // Extend the point to a plattform of length 3 if possible
	    if(pos.x > 0){
		returnChunk[pos.y][pos.x-1] = randomBlock;
	    }
	    if(pos.x < boardWidth-1){
	    	returnChunk[pos.y][pos.x+1] = randomBlock;
	    }
	}
	return returnChunk;

    }


    /** Returns the positions of all the most upmost plattforms on the input
     * chunk.
     *
     * NOTE: The positions are given in the input chunk's coordinate-system
     * and all of them are neccessarily on the same row.
     *
     * @param chunk The chunk which the upmost plattform's are to be extracted from.
     *
     * @return A list of BlockPoint(s) of the highest solid BlockType(s) in chunk.
     */
    private List<BlockPoint> getTopPlattforms(BlockType[][] chunk){
	// Find the upmost row with atleast one plattform in it
	// and save the position of all plattforms in said row.
	List<BlockPoint> topPlattforms = new ArrayList<>(boardWidth);
	for(int r = 0; r < chunk.length; r++){
             boolean foundTopRow = false;
             for(int c = 0; c < boardWidth; c++){
                 if(chunk[r][c].solid){
 		    topPlattforms.add(new BlockPoint(c,r));
 		    foundTopRow = true;
 		}
 	    }
 	    if(foundTopRow) break;
 	}
        return topPlattforms;
    }


    /** Finds all reachable positions within the given chunk which are atleast minDistance
     *  away from an upmost position. Throws an error if no reachable positions exists within
     *  said chunk, this usually indicates poorly set constant-values.
     *
     * @param chunk The chunk to which the reachable positions relate to, it is different from the
     *              chunk the topPlattforms exists within.
     *
     * @param topPlattforms A list with positions of all the currently upmost plattforms within
     *                      the main board. (They are neccesarily on the same row)
     *
     * @return A list of all the positions within "chunk" which are atleast minDistance away from an
     * 		upmost position given by "topPlattforms".
     */
    private List<BlockPoint> getReachablePositions(BlockType[][] chunk, List<BlockPoint> topPlattforms) throws
	    IllegalStateException {
	List<BlockPoint> reachablePositions = new ArrayList<>();
	// For every upmost position find all reachable positions
	for(BlockPoint pos : topPlattforms){
	    // Transpose the highest position point into chunks coordinate system by placing
	    // directly below the start of chunk.
	    BlockPoint highPos = new BlockPoint(pos.x, -pos.y - 1);
	    for(int c = 0; c < boardWidth; c++){
		for(int r = 0; r < chunk.length; r++){
		    BlockPoint pointInChunk = new BlockPoint(c,r);
		    if(pointInChunk.distanceTo(highPos) <= MAX_DISTANCE && pointInChunk.distanceTo(highPos) >= minDistance){
		        reachablePositions.add(pointInChunk);
		    }
		}
	    }
	}
	if(reachablePositions.isEmpty()){
	    throw new IllegalStateException("No reachable positions exists");
	}
	return reachablePositions;
    }

    /** Given a list of BlockPoint this functionen chooses one point randomly and (plattformsPerChunk - 1) amount
     * of points as far away from the randomly chosen point as possible.
     *
     * NOTE: The more plattforms being chosen by this function the worse their spread within the chunk will be.
     *
     * @param originalPoints - A list of points from which the function may choose points from.
     *
     * @return A list containing points from originalPoints of length "plattFormsPerChunk" with all elements
     * 		hopefully being far away from eachother.
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
	    // This means choose the point with largest min distance
	    double highestMinDist = -1;
	    BlockPoint chosenPoint = null;
	    for(BlockPoint consideredPos : startingPoints){
		if(highestMinDist < consideredPos.weight){
		    highestMinDist = consideredPos.weight;
		    chosenPoint = consideredPos;
		}
	    }
	    assert chosenPoint != null;
	    chosenPoints.add(chosenPoint.copy());
	    startingPoints.remove(chosenPoint);
	}
	return chosenPoints;
    }


    /** Returns a random powerup BlockType 10% of time and
     * the PLATTFORM blocktype in the other cases.
     *
     * @return A powerup BlockType or a PLATTFORM BlockType.
     */
    private BlockType randomBlock(){
        // Check whether a powerup should be chosen or not
        if(random.nextInt(9) == 0){
            // Return a random powerup
            return allPowerUps.get(random.nextInt(allPowerUps.size()));
	}
	return BlockType.PLATTFORM;
    }


    /** The three functions below together form the public interface for
     * other classes to notify the ChunkGenerator to create more difficult chunks.
     *
     * The restrictions of the fields are discussed more in their declaration.
     */

    public void lowerAmountOfPlattforms(){
        if(plattformsPerChunk > 2 ){
            plattformsPerChunk--;
	}
    }

    public void increaseMinDistance(){
        if(minDistance + 1 < MAX_DISTANCE){
            minDistance++;
	}
    }

    public void increaseChunkHeight(){
        if(chunkHeight < MAX_DISTANCE -1){
            chunkHeight++;
	}
    }

}
