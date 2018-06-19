public class Board {
    private int width;
    private int height;
    private BlockType[][] board;

    public int getHeight(){
        return this.height;
    }

    public int getWidth(){
        return this.width;
    }

    public Board(int width, int height){
        this.width = width-1;
        this.height = height-1;
        this.board = new BlockType[width][height];
        this.init();
    }

    /**
     * Initializes all blocktypes in the board to plattform or air
     */
    public void init() {
	for (int c = 0; c < width; c++) {
	    for (int r = 0; r < height; r++) {
		if (r == 0 || r == width-1 || c == height - 1) {
		    this.board[c][r] = BlockType.PLATTFORM;
		}
		else{
		    this.board[c][r] = BlockType.AIR;
		}
	    }
	}
    }

    public BlockType getBlockAt(int column, int row){
        return board[column][row];
    }

}
