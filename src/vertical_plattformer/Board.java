package vertical_plattformer;

import java.util.ArrayList;
import java.util.List;

/** vertical_plattformer.Board is responsible for storing data related to board and manipulating it
 *  however is seen fit.
 */
public class Board {
    private int width;
    private int height;
    private BlockType[][] board;
    private BlockType[][] nextChunk = null;
    private int nextChunkIndex = 0;
    private ChunkHandler chunkHandler;
    private BlockPoint playerPos = null;
    private List<BoardListener> boardListeners;
    private HighscoreHandler highscoreHandler;
    private int jumpsSinceLanded = 0;
    /** The amount of rows the player stands on upon game start */
    public static final int STARTING_ROWS = 8;

    public int getHeight(){
        return this.height;
    }

    public int getWidth(){
        return this.width;
    }

    public Board(int width, int height, int lowestShiftRate){
        this.width = width;
        this.height = height;
        this.board = new BlockType[height][width];
        this.boardListeners = new ArrayList<>();
        this.highscoreHandler = new HighscoreHandler();
	this.init(lowestShiftRate);
    }

    /** Initializes the board, vertical_plattformer.ChunkHandler and starts the automatic generation of chunks.
     */
    private void init(int lowestShiftRate) {
	initializeBoard();
	// Initialize the chunkHandler with the new board data
	this.chunkHandler = new ChunkHandler(board, lowestShiftRate);
	Thread t = new Thread(this.chunkHandler);
	this.chunkHandler.fillList();
	t.start();
	this.nextChunk = this.chunkHandler.getNextChunk();
    }

    /** Generates plattforms in a predefined pattern for the initial state of the board.
     *  Also initializes every non plattform block as air and generates the players starting position.
     */
    private void initializeBoard(){
	for (int r = 0; r < this.height; r++) {
	    for (int c = 0; c < this.width; c++) {
		// Set the bottom rows
		if (r > this.height - STARTING_ROWS - 1) {
		    this.board[r][c] = BlockType.PLATTFORM;
		}
		// Create the player on the board
		else if (r == height - STARTING_ROWS - 1 && c == 0) {
		    this.board[r][c] = BlockType.PLAYER;
		    this.playerPos = new BlockPoint(c, r);
		}

		// Generates a downwards slope of plattforms, most easily
		// visualized as a discrete y = kx + m line, where:
		// r == c   is comparable to y = x
		// r == 2c  is comparable to y = 2x
		// r == (width - c) is comparable to y = -x
		else if (r < height - STARTING_ROWS - 2 && (r == (2 * width - 2 * c) || r == (2 * width - 2 * c - 1))) {
		    this.board[r][c] = BlockType.PLATTFORM;
		}


		// Create a ceiling near the top of the initialized board, this allows
		// for more random variation of the chunks
		else if (r == 1 && c < width - 5) {
		    this.board[r][c] = BlockType.PLATTFORM;
		} else {
		    this.board[r][c] = BlockType.AIR;
		}
	    }
	}
    }


    public boolean playerIsFloating(){
        if(playerPos.y == height-1){
            return true;
	}
	return !board[playerPos.y+1][playerPos.x].SOLID;
    }

    public void movePlayerRight(){
        if(playerPos.x != width-1 && !board[playerPos.y][playerPos.x+1].SOLID) {
	    board[playerPos.y][playerPos.x] = BlockType.AIR;
	    playerPos.x += 1;
	    board[playerPos.y][playerPos.x] = BlockType.PLAYER;

	    // If we move to a solid block reset jump counter
	    if(playerPos.y != height-1 && board[playerPos.y+1][playerPos.x].SOLID){
		jumpsSinceLanded = 0;
	    }

	    notifyListeners();
	}

    }

    public void movePlayerLeft(){
	if(playerPos.x != 0 && !board[playerPos.y][playerPos.x-1].SOLID) {
	    board[playerPos.y][playerPos.x] = BlockType.AIR;
	    playerPos.x -= 1;
	    board[playerPos.y][playerPos.x] = BlockType.PLAYER;

	    // If we move to a solid block reset jump counter
	    if(playerPos.y != height-1 && board[playerPos.y+1][playerPos.x].SOLID){
	        jumpsSinceLanded = 0;
	    }

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
	    highscoreHandler.playerMovedUp(-1);
	    // Reset jump counter if player is moved to a solid block
	    if(!playerIsFloating()){
	        jumpsSinceLanded = 0;
	    }
	    notifyListeners();
	}
	else{
	    jumpsSinceLanded = 0;
            System.out.println("Failed moving player down");
	}
	return true;
    }


    public void jump(int jumpHeight){
        if(playerPos.y != 0) {
            // Only increment jump counter if we are jumping while floating
            if(playerIsFloating()) {
		jumpsSinceLanded++;
	    }
            for(int i = 0; i < jumpHeight; i++) {
		if (playerPos.y != 0 && !board[playerPos.y - 1][playerPos.x].SOLID) {
		    board[playerPos.y][playerPos.x] = BlockType.AIR;
		    playerPos.y -= 1;
		    board[playerPos.y][playerPos.x] = BlockType.PLAYER;
		    highscoreHandler.playerMovedUp(1);
		    notifyListeners();
		} else {
		    // If we can't jump at all we are on the ground
		    if( i == 0){
		        jumpsSinceLanded = 0;
		    }
		    break;
		}
	    }
	}
    }

    public int getJumpsSinceLanded(){
    	return jumpsSinceLanded;
    }

    public void addBoardListener(BoardListener bl){
        boardListeners.add(bl);
    }

    private void notifyListeners(){
        for(BoardListener bl : this.boardListeners){
            bl.boardChange();
	}
    }


    /** Moves every block in board one step down.
     *
     * @return Signals whether the player survives the shift or not. True means that
     * the game goes on.
     */
    public boolean shiftDown(){
            if(playerPos.y == this.height -1) {
    	    return false;
    	}

    	BlockType[][] newBoard = new BlockType[height][];
	for(int i = height-1; i >= 0; i--){
	    if(i == 0){
	        newBoard[i] = nextChunk[nextChunkIndex];
	    }
	    else {
		newBoard[i] = board[i - 1];
	    }
	}

    	playerPos.y++;
	this.board = newBoard;
	notifyListeners();

	// Update the nextchunk traverse index and ask for a new chunk if
	// the last row of the chunk was used.
    	if(nextChunkIndex + 1 == nextChunk.length){
    	    nextChunk = chunkHandler.getNextChunk();
    	    nextChunkIndex = 0;
    	}
    	else{
    	    nextChunkIndex++;
	}
    	return true;
        }

        public BlockType[][] getBoard(){
        	return this.board;
	}

	public int getHighscore(){
	    return highscoreHandler.getHighscore();
	}

	public ChunkHandler getChunkHandler(){
	    return this.chunkHandler;

	}

	public BlockType getBlockUnderPlayer(){
	    if(playerPos.y != height){
		return board[playerPos.y+1][playerPos.x];
	    }
	    return BlockType.AIR;
	}
}
