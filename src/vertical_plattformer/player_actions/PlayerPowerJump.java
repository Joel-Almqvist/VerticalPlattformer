package vertical_plattformer.player_actions;

import vertical_plattformer.Board;

public class PlayerPowerJump extends PlayerActions{

    public PlayerPowerJump(Board board){
        super(board);
    }

    @Override
    public void jump(int height){
        if(!board.playerIsFloating() || board.getJumpsSinceLanded() < 1){
	    board.jump(height + 1, false);
	}
    }

    @Override
    public String getPowerupName(){
        return "Power Jump";
    }
}
