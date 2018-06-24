import javax.swing.*;
import java.awt.event.ActionEvent;

public class Player implements BoardListener{
    private static final int FALLTIME = 200;
    private static final int MOVEMENTSPEED = 80;
    private BoardVisual boardVisual;
    private Board board;
    private Timer gravityTimer;
    private Timer movementTimer;
    private boolean movingRight;

    public Player(BoardVisual boardVisual, Board board) {

	this.boardVisual = boardVisual;
	this.board = board;
	setUpKeyBinds();
	gravityTimer = new Timer(FALLTIME, fallDown);
	gravityTimer.setRepeats(false);

	movementTimer = new Timer(MOVEMENTSPEED, autoMovePlayer);
	movingRight = true;
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
	     //System.out.println("Left arrow pressed down");
	     if(!movementTimer.isRunning()) {
		 //board.movePlayerLeft();
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
	 board.jump();
 	}
    };

    final private Action fallDown = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            board.movePlayerDown();
            }
    };


    final private Action autoMovePlayer = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(movingRight){
                board.movePlayerRight();
	    }
	    else{
                board.movePlayerLeft();
	    }
	}
    };

    public void boardChange(){
        if(this.board.playerIsFloating()){

	    if(!gravityTimer.isRunning()){
	        gravityTimer.start();
	    }
	}
	else{
            gravityTimer.stop();
	}
    }

}
