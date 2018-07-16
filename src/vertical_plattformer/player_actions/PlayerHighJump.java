package vertical_plattformer.player_actions;

import vertical_plattformer.*;

public class PlayerHighJump extends PlayerActions{

    public PlayerHighJump(Board board){
        super(board);
    }

    @Override
    public void jump(int height){
        super.jump(height * 2);
    }
}
