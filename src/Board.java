import java.util.ArrayList;
import java.util.List;

public class Board {
    private int width;
    private int height;
    private BlockType[][] board;
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
        this.init();
        this.boardListeners = new ArrayList<>();
    }

    /**
     * Initializes all blocktypes in the board to plattform or air
     */
    public void init() {
	for (int r = 0; r < this.height; r++) {
	    for (int c = 0; c < this.width; c++) {
	        if (r == this.height-1 || c == this.width -1 || c == 0) {
		//if (r == 0 || r == this.height-1 || c == this.height - 1) {
		    this.board[c][r] = BlockType.PLATTFORM;
		}
		else if (c == 1 && r == this.height-2){
		    this.board[c][r] = BlockType.PLAYER;
		    this.playerPos = new int[]{c,r};
		}
		else{
		    this.board[c][r] = BlockType.AIR;
		}
	    }
	}
	/*
	StringBuilder builder = new StringBuilder();
	for (int c = 0; c < this.width; c++) {
	    for (int r = 0; r < this.height; r++) {
		if (this.board[c][r] == BlockType.AIR) {
		    builder.append("A");
		} else if (this.board[c][r] == BlockType.PLATTFORM) {
		    builder.append("P");
		} else {
		    builder.append("T");
		}
	    }
	    builder.append("\n");
	}
	System.out.println(builder);
	*/
    }

    public BlockType getBlockAt(int column, int row){
        return board[column][row];
    }

    public void movePlayerRight(){
	board[playerPos[0]][playerPos[1]] = BlockType.AIR;
	playerPos[0] += 1;
	board[playerPos[0]][playerPos[1]] = BlockType.PLAYER;
	notifyListeners();

    }

    public void movePlayerLeft(){
	board[playerPos[0]][playerPos[1]] = BlockType.AIR;
	playerPos[0] -= 1;
	board[playerPos[0]][playerPos[1]] = BlockType.PLAYER;
	notifyListeners();
    }

    public void addBoardListener(BoardListener bl){
        boardListeners.add(bl);
    }

    private void notifyListeners(){
        for(BoardListener bl : this.boardListeners){
            bl.boardChange();
	}
    }
}
