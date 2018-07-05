import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

public class PlattformGenerator{

    private int boardWidth;
    private int jumpHeight;
    private int minDistance;
    private Random random;
    private int amountOfPlattforms;

    PlattformGenerator(int boardWidth, int jumpHeight, int minDistance){
        this.boardWidth = boardWidth;
        this.jumpHeight = jumpHeight;
        this.minDistance = minDistance;
	this.random = new Random();
	this.amountOfPlattforms = 3;
    }

    public BlockType[][] generateChunk(BlockType[][] board){
        List<int[]> upmostPlattforms = new ArrayList<>(this.boardWidth);

	// Find the upmost row with atleast one plattform in it
	// and save the position of all plattforms in said row.
	for(int r = 0; r < board[0].length; r++){
             boolean foundTopRow = false;
             for(int c = 0; c < board.length; c++){
                 if(board[c][r] == BlockType.PLATTFORM){
 		    upmostPlattforms.add(new int[]{c,r});
 		    foundTopRow = true;
 		}
 	    }
 	    if(foundTopRow) break;
 	}

        BlockType[][] returnChunk = new BlockType[boardWidth][jumpHeight-3];

        // Fill the chunk with air
        for(int c = 0; c < boardWidth; c++){
            for(int r = 0; r < returnChunk[0].length; r++){
		returnChunk[c][r] = BlockType.AIR;
	    }
	}

	// Find all positions the player can jump to
	List<int[]> reachablePositions = new LinkedList<>();
	for(int[] pos : upmostPlattforms){
	    for(int c = 0; c < boardWidth; c++){
		for(int r = 0; r < returnChunk[0].length; r++){
		    double distanceBetweenPos = Math.sqrt(Math.pow(pos[0] - c,2) + Math.pow(pos[1] - r,2));
	    		if(distanceBetweenPos < jumpHeight-1){
	    		    	//System.out.println("pos "+pos[0]+" , "+pos[1]+" reachable from "+c+" , "+r);
	    		    	//System.out.println("distance = "+distanceBetweenPos);
				reachablePositions.add(new int[]{c,r});
			}
	    	    }
	    	}
	}

	// Find all positions within the minimum distance to a highest-point in blocks
	// and remove them from reachablePositions
	for(int[] pos : upmostPlattforms){
	    for(int c = 0; c < boardWidth; c++){
		for(int r = 0; r < returnChunk[0].length; r++){
		    double distanceBetweenPos = Math.sqrt((pos[0] - c)^2 + (pos[1] - r)^2);
		    if(distanceBetweenPos < minDistance){

			// We have found a position to remove, now remove it
			Iterator<int[]> iterator = reachablePositions.iterator();
			while(iterator.hasNext()){
			    int[] reachablePos = iterator.next();
			    if(reachablePos[0] == c && reachablePos[1] == r){
				iterator.remove();
			    }
			}
		    }
		}
	    }
	}


	// TODO Add a check that reachablePositions exist here

	// TODO Fixa denna fullösning!
	if(reachablePositions.isEmpty()){
	    // Find all positions the player can jump to
	   	for(int[] pos : upmostPlattforms){
	   	    for(int c = 0; c < boardWidth; c++){
	   		for(int r = 0; r < returnChunk[0].length; r++){
	   		    double distanceBetweenPos = Math.sqrt(Math.pow(pos[0] - c,2) + Math.pow(pos[1] - r,2));
	   	    		if(distanceBetweenPos < jumpHeight-1){
	   				reachablePositions.add(new int[]{c,r});
	   			}
	   	    	    }
	   	    	}
	   	}
	}



	// Choose a predefined amount of random positions
	List<int[]> randomPositions = new ArrayList<>();
	for(int i = 0; i < this.amountOfPlattforms; i++){
	    int pos = random.nextInt(reachablePositions.size()-1);
	    randomPositions.add(reachablePositions.get(pos));
	    reachablePositions.remove(pos);
	}


	// Set the randomly chosen blocks to plattform and if possible
	// also set their neighbors.
	for(int[] pos : randomPositions){
	    returnChunk[pos[0]][pos[1]] = BlockType.PLATTFORM;
	    if(pos[0] > 0){
		returnChunk[pos[0]-1][pos[1]] = BlockType.PLATTFORM;
	    }
	    if(pos[0] < boardWidth-1){
	    	returnChunk[pos[0]+1][pos[1]] = BlockType.PLATTFORM;
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


}
