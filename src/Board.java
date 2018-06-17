public class Board {
    private int width;
    private int height;
    private int[][] board;

    public Board(int width, int height){
        this.width = width;
        this.height = height;
        this.board = new int[width][height];
    }

    public void clear(){
        for(int c = 0; c < width; c++){
            for(int r = 0; r < height; r++){
		this.board[c][r] = 0;
	    }
	}
    }
}
