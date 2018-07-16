package vertical_plattformer.player_actions;


import vertical_plattformer.Board;

/** PlayerActions is responsible for sending the players action to board. Subclass
 * this class to create new behavior for a particular action.
 *
 */
public class PlayerActions {
    protected Board board;

    public String getPowerupName(){
        return "None";
    }

    public PlayerActions(Board board){
        this.board = board;
    }

    public void movePlayerLeft(){
	board.movePlayerLeft();
    }

    public void movePlayerRight(){
	board.movePlayerRight();
    }

    public boolean movePlayerDown(){
	return board.movePlayerDown();
    }

    public boolean playerIsFloating(){
        return board.playerIsFloating();
    }

    public void jump(int height){
        if(!board.playerIsFloating() || board.getJumpsSinceLanded() < 1){
	    board.jump(height, true);
	}
    }
}
