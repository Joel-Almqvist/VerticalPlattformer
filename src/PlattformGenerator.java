import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PlattformGenerator{

    private int boardWidth;
    private int jumpHeight;
    private int minDistance;
    private Random random;

    PlattformGenerator(int boardWidth, int jumpHeight, int minDistance){
        this.boardWidth = boardWidth;
        this.jumpHeight = jumpHeight;
        this.minDistance = minDistance;
	this.random = new Random();
    }

    public BlockType[][] generateChunk(BlockType[][] board){
        List<int[]> upmostPlattforms = new ArrayList<>(this.boardWidth);




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







        // Find the upmost row with plattforms within it
	// and store their position
        /*for(int c = 0; c < board.length; c++){
            boolean foundTopRow = false;
            for(int r = 0; r < board[0].length; r++){
                if(board[c][r] == BlockType.PLATTFORM){
		    upmostPlattforms.add(new int[]{c,r});
		    foundTopRow = true;
		}
	    }
	    if(foundTopRow) break;
	}*/

	/**
	 * Randomize a potential length between minDistance and the players jumping height.
	 * Remove the height needed for the player to reach the old top of the board.
	 */

        int distance = minDistance + random.nextInt(jumpHeight-1-minDistance);
        /*
        System.out.println("old distance = "+distance);
	System.out.println("highest plattform at y = "+upmostPlattforms.get(0)[1]);

	System.out.println("");
	for(int[] pos : upmostPlattforms){
	    System.out.println("x = "+pos[0]);
	    System.out.println("y = "+pos[1]);
	    System.out.println("");
	}
	*/

        // TODO This check prevents new spawns if the map is impossible to solve
	//distance -= upmostPlattforms.get(0)[1];

	// TODO This is only occurs when an impossible map is generated, something
	// TODO we currently do not safe guard against
	if(distance <= 0){
	    System.out.println(distance);
	    distance = minDistance;
	}



        BlockType[][] returnChunk = new BlockType[boardWidth][distance];

        // Do not generate any plattforms within the min distance to avoid cluttering
        for(int c = 0; c < boardWidth; c++){
            for(int r = 0; r < distance; r++){
                if(r < minDistance) {
		    returnChunk[c][r] = BlockType.AIR;
		}
		else{
		    returnChunk[c][r] = BlockType.PLATTFORM;
		}
	    }
	}

	// TODO Add a check such that it is always possible to reach the next level

	for(int r = distance-1; r >= 0; r--){
            for(int c = 0; c < boardWidth; c++){
                if(0 != random.nextInt(9)){
                    returnChunk[c][r] = BlockType.AIR;
		}
	    }
	}

	/*
	for(int i = 0; i < returnChunk.length; i++){
	    for(int j = 0; j < returnChunk[0].length; j++){
		System.out.println(returnChunk[i][j]);
	    }
	}
	*/

	return returnChunk;

    }


}
