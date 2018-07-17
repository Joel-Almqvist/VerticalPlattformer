package vertical_plattformer.player_actions;

import vertical_plattformer.Board;

/**
 *  PlayerPowerJump is a powerup called Power Jump which makes the player jump without collision
 *  destroying any BlockPoint he jumps through. Also makes the player jump slightly higher to make it
 *  more balanced.
 */
public class PlayerPowerJump extends PlayerActions{

    public PlayerPowerJump(Board board){
        super(board);
    }

    @Override
    public void playerCallJump(int height){
        jump(height+1, false, 1);
    }

    @Override
    public String getPowerupName(){
        return "Power Jump";
    }
}
