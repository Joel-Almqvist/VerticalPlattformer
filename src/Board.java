import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Board {
    private int width;
    private int height;
    private BlockType[][] board;
    private BlockType[][] nextChunk = null;
    private PlattformGenerator plattformGenerator;
    private int[] playerPos = null;
    private List<BoardListener> boardListeners;

    public int getHeight(){
        return this.height;
    }

    public int getWidth(){
        return this.width;
    }

    public Board(int width, int height){
        this.width = width;
        this.height = height;
        this.board = new BlockType[width][height];
        this.boardListeners = new ArrayList<>();

        // TODO do not hardcode this!
        this.plattformGenerator = new PlattformGenerator(width,8,3);

        // TODO This should not be here
	this.init();
    }

    /**
     * Initializes all blocktypes in the board to plattform or air
     */
    public void init() {
	for (int r = 0; r < this.height; r++) {
	    for (int c = 0; c < this.width; c++) {
	        if (r > this.height-9 || c == this.width -1 || c == 0) {
		//if (r == 0 || r == this.height-1 || c == this.height - 1) {
		    this.board[c][r] = BlockType.PLATTFORM;
		}
		else if (c == 8 && r == height-10){
		    this.board[c][r] = BlockType.PLAYER;
		    this.playerPos = new int[]{c,r};
		}
		else if(r % 3 == 0 && c <= 4){
		    this.board[c][r] = BlockType.PLATTFORM;
		}
		else{
		    this.board[c][r] = BlockType.AIR;
		}
	    }
	}
	this.nextChunk = this.plattformGenerator.generateChunk(board);
    }

    // TODO Remove this later on
    public void shuffle() {
	for (int r = 0; r < this.height; r++) {
	    for (int c = 0; c < this.width; c++) {
		if(r == c){
		    board[r][c] = BlockType.PLATTFORM;
		}

	    }
	}
    }
    public BlockType getBlockAt(int column, int row){
        return board[column][row];
    }

    public boolean playerIsFloating(){
        if(playerPos[1] == this.height -1){
            return true;
	}
	return !board[playerPos[0]][playerPos[1]+1].SOLID;
    }

    public void movePlayerRight(){
        if(!board[playerPos[0]+1][playerPos[1]].SOLID) {
	    board[playerPos[0]][playerPos[1]] = BlockType.AIR;
	    playerPos[0] += 1;
	    board[playerPos[0]][playerPos[1]] = BlockType.PLAYER;
	    notifyListeners();
	}

    }

    public void movePlayerLeft(){
	if(!board[playerPos[0]-1][playerPos[1]].SOLID) {
	    board[playerPos[0]][playerPos[1]] = BlockType.AIR;
	    playerPos[0] -= 1;
	    board[playerPos[0]][playerPos[1]] = BlockType.PLAYER;
	    notifyListeners();
	}
    }

    /**
     * Shifts the player one step downward, used to simulate gravity. The return bool
     * signals whether the player died by reaching the bottom or not.
     */
    public boolean movePlayerDown(){
        if(playerIsFloating()){
            if(playerPos[1] == this.height-1) {
                return false;
	    }
	    board[playerPos[0]][playerPos[1]] = BlockType.AIR;
	    playerPos[1] += 1;
	    board[playerPos[0]][playerPos[1]] = BlockType.PLAYER;
	    notifyListeners();
	}
	else{
            System.out.println("Failed shift down");
	}
	return true;
    }


    public void jump(){
        if(!playerIsFloating() && playerPos[1] != 0) {
	    board[playerPos[0]][playerPos[1]] = BlockType.AIR;
            if(playerPos[1] < 8 ){
                playerPos[1] = 0;
	    }
	    else {
		playerPos[1] -= 8;
	    }
	    board[playerPos[0]][playerPos[1]] = BlockType.PLAYER;
	    notifyListeners();
	}
    }


    public void addBoardListener(BoardListener bl){
        boardListeners.add(bl);
    }

    private void notifyListeners(){
        for(BoardListener bl : this.boardListeners){
            bl.boardChange();
	}
    }

    /**
     * Moves every block one step down to its original position. Currently the top row
     * is filled with air. The return value signals whether the player died in this shift or not.
     */
    public boolean shiftDown(){
        if(playerPos[1] == this.height -1) {
	    return false;
	}

	if(nextChunk[0].length == 0){
            nextChunk = plattformGenerator.generateChunk(board);
	}

	playerPos[1]++;
	for (int r = this.height-1; r >= 0; r--) {
	    for (int c = 0; c < this.width; c++) {
	        if(r == 0){
		    board[c][r] = nextChunk[c][0];
		}
		else {
		    board[c][r] = board[c][r - 1];
		}
	    }
	}
	//System.out.println(nextChunk[0].length);
	for(int c = 0; c < nextChunk.length; c++){
	    nextChunk[c] = Arrays.copyOfRange(nextChunk[c],1,nextChunk[c].length);
	}
	//System.out.println(nextChunk[0].length);


	notifyListeners();
	return true;
    }
}
