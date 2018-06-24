import javax.swing.*;
import java.awt.event.ActionEvent;

public class Player implements BoardListener{
    private static final int FALLTIME = 200;
    private BoardVisual boardVisual;
    private Board board;
    private Timer timer;

    public Player(BoardVisual boardVisual, Board board) {

	this.boardVisual = boardVisual;
	this.board = board;
	setUpKeyBinds();
	timer = new Timer(FALLTIME, fallDown);
	timer.setRepeats(false);
    }

     private void setUpKeyBinds(){
	 boardVisual.getInputMap().put(KeyStroke.getKeyStroke("RIGHT"), "moveRight");
	 boardVisual.getActionMap().put("moveRight", moveRight);

	 boardVisual.getInputMap().put(KeyStroke.getKeyStroke("LEFT"), "moveLeft");
	 boardVisual.getActionMap().put("moveLeft", moveLeft);

	 boardVisual.getInputMap().put(KeyStroke.getKeyStroke("UP"), "jump");
	 boardVisual.getActionMap().put("jump", jump);
     }


     private Action moveRight = new AbstractAction(){
	 @Override
	 public void actionPerformed(ActionEvent e) {
	     board.movePlayerRight();
	 }
     };

     private Action moveLeft = new AbstractAction(){
	 @Override
	 public void actionPerformed(ActionEvent e){
	     board.movePlayerLeft();
	 }
     };

    private Action jump = new AbstractAction(){
     @Override
     public void actionPerformed(ActionEvent e){
         timer.stop();
         timer.start();
	 board.jump();
 	}
    };

    final private Action fallDown = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            board.movePlayerDown();
            }
    };

    public void boardChange(){
        if(this.board.playerIsFloating()){

	    if(!timer.isRunning()){
	        timer.start();
	    }

	}
    }
}
