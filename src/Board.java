import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Board {
    private int width;
    private int height;
    private BlockType[][] board;
    private BlockType[][] nextChunk = null;
    private PlattformGenerator plattformGenerator;
    // TODO remove below
    //private int[] playerPos = null;
    private BlockPoint playerPos = null;
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
        this.board = new BlockType[height][width];
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
	        if (r > this.height-9 || c == 0) {
		//if (r == 0 || r == this.height-1 || c == this.height - 1) {
		    this.board[r][c] = BlockType.PLATTFORM;
		}
		else if (c == 8 && r == height-10){
		    this.board[r][c] = BlockType.PLAYER;
		    this.playerPos = new BlockPoint(c,r, BlockType.PLAYER);
		}
		else if(r % 3 == 0 && c <= 4){
		    this.board[r][c] = BlockType.PLATTFORM;
		}
		else{
		    this.board[r][c] = BlockType.AIR;
		}
	    }
	}
	// TODO Fix
	//this.nextChunk = this.plattformGenerator.generateChunk(board);
	this.nextChunk = new BlockType[][]{{}};


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
    public BlockType getBlockAt(int row, int column){
        return board[row][column];
    }

    public boolean playerIsFloating(){
        if(playerPos.y == this.height -1){
            return true;
	}
	return !board[playerPos.y+1][playerPos.x].SOLID;
    }

    // TODO add out of bounds check
    public void movePlayerRight(){
        if(!board[playerPos.y][playerPos.x+1].SOLID) {
	    board[playerPos.y][playerPos.x] = BlockType.AIR;
	    playerPos.x += 1;
	    board[playerPos.y][playerPos.x] = BlockType.PLAYER;
	    notifyListeners();
	}

    }

    // TODO add out of bounds check
    public void movePlayerLeft(){
	if(!board[playerPos.y][playerPos.x-1].SOLID) {
	    board[playerPos.y][playerPos.x] = BlockType.AIR;
	    playerPos.x -= 1;
	    board[playerPos.y][playerPos.x] = BlockType.PLAYER;
	    notifyListeners();
	}
    }

    /**
     * Shifts the player one step downward, used to simulate gravity. The return bool
     * signals whether the player died by reaching the bottom or not.
     */
    public boolean movePlayerDown(){
        if(playerIsFloating()){
            if(playerPos.y == this.height-1) {
                return false;
	    }
	    board[playerPos.y][playerPos.x] = BlockType.AIR;
	    playerPos.y += 1;
	    board[playerPos.y][playerPos.x] = BlockType.PLAYER;
	    notifyListeners();
	}
	else{
            System.out.println("Failed shift down");
	}
	return true;
    }


    public void jump(){
        if(!playerIsFloating() && playerPos.y != 0) {
	    board[playerPos.y][playerPos.x] = BlockType.AIR;
            if(playerPos.y < 8 ){
                playerPos.y = 0;
	    }
	    else {
		playerPos.y -= 8;
	    }
	    board[playerPos.y][playerPos.x] = BlockType.PLAYER;
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
        if(playerPos.x == this.height -1) {
	    return false;
	}

	if(nextChunk[0].length == 0){
            nextChunk = plattformGenerator.generateChunk(board);
	}

	playerPos.y++;
	for (int r = this.height-1; r >= 0; r--) {
	    for (int c = 0; c < this.width; c++) {
	        if(r == 0){
	            // Make the top row of board take on the next chunks blocks
		    //board[r][c] = nextChunk[0][c];
		    // TODO Fix this
		    board[r][c] = BlockType.AIR;

		}
		else {
		    board[r][c] = board[r-1][c];
		}
	    }
	}
	//System.out.println(nextChunk[0].length);
	// TODO this will have to change in the new coordinate system
	//for(int c = 0; c < nextChunk.length; c++){
	//    nextChunk[c] = Arrays.copyOfRange(nextChunk[c],1,nextChunk[c].length);
	//}
	//System.out.println(nextChunk[0].length);


	notifyListeners();
	return true;
    }
}
