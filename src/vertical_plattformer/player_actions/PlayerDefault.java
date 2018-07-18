package vertical_plattformer.player_actions;


import vertical_plattformer.Board;

/** The default behavior for actions are defined here in PlayerDefault, subclass this to
 * create a powerup which inherits the default behavior for non-modified actions.
 *
 *
 * PlayerDefault is responsible for reading the game state of board and determining
 * the state the player is in. It also has the responsibility of mapping player actions
 * to how the game state should change.
 *
 */
public class PlayerDefault implements PlayerActions{
    protected Board board;

    public PlayerDefault(Board board){
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
