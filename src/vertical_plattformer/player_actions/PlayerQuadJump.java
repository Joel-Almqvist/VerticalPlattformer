package vertical_plattformer.player_actions;

import vertical_plattformer.Board;

/**
 *  PlayerQuadJump is a powerup called Quad Jump which allows the player to
 *  perform four jumps in a row. IE three air jumps are possible.
 */
public class PlayerQuadJump extends PlayerDefault
{

    public PlayerQuadJump(Board board){
        super(board);
    }

    @Override
    public void playerCallJump(int height){
        jump(height, true, 3);
    }

    @Override
    public String getPowerupName(){
        return "Quad Jump";
    }
}
