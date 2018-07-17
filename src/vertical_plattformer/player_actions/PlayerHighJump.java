package vertical_plattformer.player_actions;

import vertical_plattformer.*;

/**
 *  PlayerHighJump is a powerup called High Jump which makes the player jump higher.
 */
public class PlayerHighJump extends PlayerActions{

    public PlayerHighJump(Board board){
        super(board);
    }

    @Override
    public void playerCallJump(int height){
        super.playerCallJump(height + 4);
    }

    @Override
    public String getPowerupName(){
        return "High Jump";
    }
}
