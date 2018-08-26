package vertical_plattformer.player_actions;

/**
 * PlayerActions is an interface for all possible actions the player might want to perform, and for all
 * actions the game might wish to perform on the player. What to do when an action is
 * called is up for every powerup to decide.
 *
 * Any class which fullfills this interface may be used as a powerup.
 */
public interface PlayerActions {

    public void playerCallJump(int height);

    public void playerCallMoveLeft();

    public void playerCallMoveRight();

    public boolean playerCallMoveDown();

    public boolean playerCallIsFloating();

    public String getPowerupName();

}
