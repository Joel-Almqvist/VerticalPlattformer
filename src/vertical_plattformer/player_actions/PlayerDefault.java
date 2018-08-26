package vertical_plattformer.player_actions;


import vertical_plattformer.Board;

/** The default behavior for actions are defined here in PlayerDefault, this class allows
 * for creating powerups easier through subclassing, but any powerup fullfilling the PlayerActions
 * interface is enough to be a considered a powerup.
 *
 * PlayerDefault reads the gamestate from Board and accesses Board's public interface to modify
 * the player's state as seen fit.
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
	board.movePlayerLeft();
    }

    public void playerCallMoveRight(){
	board.movePlayerRight();
    }

    public boolean playerCallMoveDown(){
        return board.movePlayerDown();
    }

    public boolean playerCallIsFloating(){
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
