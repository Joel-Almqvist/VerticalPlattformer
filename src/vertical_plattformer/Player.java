package vertical_plattformer;

import vertical_plattformer.player_actions.PlayerActions;
import vertical_plattformer.player_actions.PlayerHighJump;
import vertical_plattformer.player_actions.PlayerPowerJump;
import vertical_plattformer.player_actions.PlayerQuadJump;


import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.TimerTask;


/** The vertical_plattformer.Player class maps player action to their corresponding keybinding to vertical_plattformer.BoardVisual
 *  and communicates to vertical_plattformer.Board how to change the board state through player action.
 *  Also stores information regarding the players stats, such as speed and jumping height.
 *
 */
public class Player implements BoardListener{
    public static final int FALLTIME = 120;
    public static final int MOVEMENTSPEED = 75;
    /** How long a powerup should last in ms*/
    public static final int POWERUP_DURATION = 2500;

    private Board board;
    private BoardVisual boardVisual;
    private javax.swing.Timer gravityTimer;
    private javax.swing.Timer movementTimer;
    private java.util.Timer powerupTimer;
    private TimerTask powerupCountdown;
    private boolean movingRight;
    private boolean alive = true;
    private int jumpHeight;
    private PlayerActions playerActions;

    public Player(BoardVisual boardVisual, Board board) {
        this.board = board;
	this.boardVisual = boardVisual;
	this.playerActions = new PlayerActions(board);

	this.gravityTimer = new javax.swing.Timer(FALLTIME, fallDown);
	this.gravityTimer.setRepeats(false);

	this.movementTimer = new javax.swing.Timer(MOVEMENTSPEED, autoMovePlayer);
	this.movingRight = true;
	this.jumpHeight = 5;

	this.powerupTimer = new java.util.Timer();
	this.powerupCountdown = new TimerTask()
	{
	    @Override public void run() {

	    }
	};


	setUpKeyBinds();
    }

     private void setUpKeyBinds(){
	 boardVisual.getInputMap().put(KeyStroke.getKeyStroke("RIGHT"), "moveRightPress");
	 boardVisual.getActionMap().put("moveRightPress", moveRightPress);

	 boardVisual.getInputMap().put(KeyStroke.getKeyStroke("released RIGHT"), "moveRightRelease");
	 boardVisual.getActionMap().put("moveRightRelease", stopMovement);

	 boardVisual.getInputMap().put(KeyStroke.getKeyStroke("LEFT"), "moveLeftPress");
	 boardVisual.getActionMap().put("moveLeftPress", moveLeftPress);

	 boardVisual.getInputMap().put(KeyStroke.getKeyStroke("released LEFT"), "moveLeftRelease");
	 boardVisual.getActionMap().put("moveLeftRelease", stopMovement);


	 boardVisual.getInputMap().put(KeyStroke.getKeyStroke("UP"), "jump");
	 boardVisual.getActionMap().put("jump", jump);
     }

     public void stop(){
         alive = false;
         boardVisual.getInputMap().clear();
         gravityTimer.stop();
         movementTimer.stop();
     }

     public boolean playerAlive(){
         return alive;
     }


    private Action moveRightPress = new AbstractAction(){
     @Override
     public void actionPerformed(ActionEvent e){
	 if(!movementTimer.isRunning()) {
	     movingRight = true;
	     movementTimer.start();
	    	}
	 else if(!movingRight){
	     movementTimer.stop();
	 }
	    }
	};


     private Action moveLeftPress = new AbstractAction(){
	 @Override
	 public void actionPerformed(ActionEvent e){
	     if(!movementTimer.isRunning()) {
		 movingRight = false;
		 movementTimer.start();
	     }
	     else if(movingRight){
	     	     movementTimer.stop();
	     	 }
	 }
     };

    private Action stopMovement = new AbstractAction(){
     @Override
     public void actionPerformed(ActionEvent e){
         movementTimer.stop();
     }
    };



    private Action jump = new AbstractAction(){
     @Override
     public void actionPerformed(ActionEvent e){
         gravityTimer.stop();
         gravityTimer.start();
	 playerActions.playerCallJump(jumpHeight);
 	}
    };

    final private Action fallDown = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(!playerActions.playerCallMoveDown()){
                alive = false;
                stop();
	    }
	}
    };


    final private Action autoMovePlayer = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(movingRight){
                playerActions.playerCallMoveRight();
	    }
	    else{
		playerActions.playerCallMoveLeft();
	    }
	}
    };

    /**
     * Whenever the board changes check if the player floating, if he is check if the gravity timer is running.
     * If the player reaches solid ground before the timer expires stop the timer. If this does not happen
     * move the player down one step.
     */
    public void boardChange(){
        checkGravity();
        setPlayerPowerup();
    }

    /** Checks if the player is floating and if there is a countdown timer
     * for moving him down. If there is no such timer start it.
     */
    public void checkGravity(){
	if(this.playerActions.playerCallIsFloating()){
	    if(!gravityTimer.isRunning()){
		gravityTimer.start();
	    }
	}
	else{
	    gravityTimer.stop();
	}
    }

    /** This function is called continuously whenever board changes, it checks
     * whether the player should be powered-up. If so, power-up the player and
     * cancel the current powerup countdown and create a new one countdown.
     */
    public void setPlayerPowerup(){
        boolean powerupChanged = true;
        // Power-up the player
        switch(board.getBlockUnderPlayer()){
	    case HIGHJUMP:
	        playerActions = new PlayerHighJump(board);
	        break;
	    case POWERJUMP:
		playerActions = new PlayerPowerJump(board);
		break;
	    case QUADJUMP:
		playerActions = new PlayerQuadJump(board);
		break;
	    default:
	        powerupChanged = false;
	        break;
	}
	if(powerupChanged) {
	    // Notify boardVisual and create a new countdown
	    boardVisual.setPowerupName(playerActions.getPowerupName());
	    powerupCountdown.cancel();
	    powerupCountdown = new TimerTask()
	    {
		@Override public void run() {
		    playerActions = new PlayerActions(board);
		    boardVisual.setPowerupName(playerActions.getPowerupName());
		}
	    };
	    powerupTimer.schedule(powerupCountdown, POWERUP_DURATION);
	}
    }

}
