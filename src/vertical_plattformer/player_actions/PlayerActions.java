package vertical_plattformer.player_actions;


import vertical_plattformer.Board;

/** PlayerActions is responsible for reading the game state of board and determining
 * the state the player is in. It also has the responsibility of mapping player actions
 * to how the game state should change.
 *
 * This is done by providing public functions starting with "playerCall". These
 * playerCalls occur when the player wishes do something or when the game wants to know
 * which state the player is in. By sublcassing PlayerActions and overriding these playercalls
 * every subclass may themselves determine what should happen when the player does something
 * and which condition must hold for the player to enter a certain state.
 *
 * By subclassing PlayerActions new powerups can easily be created.
 */
public class PlayerActions {
    protected Board board;

    public PlayerActions(Board board){
        this.board = board;
    }

    public String getPowerupName(){
	return "None";
    }

    public void playerCallMoveLeft(){
	movePlayerLeft();
    }

    protected void movePlayerLeft(){
        board.movePlayerLeft();
    }

    public void playerCallMoveRight(){
	movePlayerRight();
    }

    protected void movePlayerRight(){
        board.movePlayerRight();
    }

    public boolean playerCallMoveDown(){
        return movePlayerDown();
    }

    protected boolean movePlayerDown(){
	return board.movePlayerDown();
    }

    public boolean playerCallIsFloating(){
        return playerIsFloating();
    }

    protected boolean playerIsFloating(){
        return board.playerIsFloating();
    }

    public void playerCallJump(int height){
	jump(height, true, 1);
    }

    /** Makes the player jumps with or without collision and allows
     * for a set amount of extra air jumps.
     *
     * @param height How high the player will jump
     *
     * @param collisionOn Flag for whether the jump will
     *                    be stopped by solid blocks or not
     *
     * @param airJumps How many jumps may be performed in the air before landing,
     */
    protected void jump(int height, boolean collisionOn, int airJumps){
	if(!board.playerIsFloating() || board.getJumpsSinceLanded() < airJumps){
	board.jump(height, collisionOn);
	}
    }

}
