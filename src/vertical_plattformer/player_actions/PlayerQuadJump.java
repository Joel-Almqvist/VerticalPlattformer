package vertical_plattformer.player_actions;

import vertical_plattformer.Board;

public class PlayerQuadJump extends PlayerActions{

    public PlayerQuadJump(Board board){
        super(board);
    }

    @Override
    public void jump(int height){
        if(!board.playerIsFloating() || board.getJumpsSinceLanded() < 3){
	    board.jump(height, true);
	}
    }

    @Override
    public String getPowerupName(){
        return "Quad Jump";
    }
}
