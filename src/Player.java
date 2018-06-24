import javax.swing.*;
import java.awt.event.ActionEvent;

public class Player {
    private BoardVisual comp;
    private Board board;

    public Player(BoardVisual bVComp, Board board) {

	this.comp = bVComp;
	this.board = board;
	setUpKeyBinds();
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

    }
