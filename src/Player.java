import javax.swing.*;
import java.awt.event.ActionEvent;

public class Player implements BoardListener{
    private static final int FALLTIME = 200;
    private BoardVisual comp;
    private Board board;
    private Timer timer;

    public Player(BoardVisual boardVisual, Board board) {

	this.comp = boardVisual;
	this.board = board;
	setUpKeyBinds();
	timer = new Timer(FALLTIME, fallDown);
	timer.setRepeats(false);
    }

     private void setUpKeyBinds(){
	 comp.getInputMap().put(KeyStroke.getKeyStroke("RIGHT"), "moveRight");
	 comp.getActionMap().put("moveRight", moveRight);

	 comp.getInputMap().put(KeyStroke.getKeyStroke("LEFT"), "moveLeft");
	 comp.getActionMap().put("moveLeft", moveLeft);
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
