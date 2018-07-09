import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Board {
    private int width;
    private int height;
    private BlockType[][] board;
    private BlockType[][] nextChunk = null;
    private int nextChunkIndex = 0;
    private ChunkHandler chunkHandler;
    //private PlattformGenerator plattformGenerator;
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
	this.init();
    }

    /**
     * Initializes all blocktypes in the board to plattform or air
     */
    private void init() {
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
	// Initialize the chunkHandler with the new board data
	this.chunkHandler = new ChunkHandler(board);
	Thread t = new Thread(this.chunkHandler);
	this.chunkHandler.fillList();
	t.start();
	this.nextChunk = this.chunkHandler.getNextChunk();
    }

    public BlockType getBlockAt(int row, int column){
        return board[row][column];
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
	    notifyListeners();
	}

    }

    public void movePlayerLeft(){
	if(playerPos.x != 0 && !board[playerPos.y][playerPos.x-1].SOLID) {
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
}
