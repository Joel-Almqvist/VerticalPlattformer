import javax.swing.*;
import java.awt.event.ActionEvent;

public class Game{
    public final static int STARTING_TICKRATE = 200;
    private Board board;
    private BoardVisual boardVisual;
    private BoardFrame frame;
    private Player player;
    private Timer gameTimer;
    private int tickrate;

    Game(Board board, BoardVisual boardVisual, BoardFrame frame, Player player){
        this.board = board;
        this.boardVisual = boardVisual;
        this.frame = frame;
        this.player = player;
        this.tickrate = STARTING_TICKRATE;
        this.gameTimer = new Timer(this.tickrate, shiftWorld);
    }

    public static void main(String[] args) {
        Board board = new Board(40, 50);
        BoardVisual boardVisual = new BoardVisual(board);
        BoardFrame frame = new BoardFrame(board, "Platformer", boardVisual);
        Player player = new Player(boardVisual, board);

        board.addBoardListener(boardVisual);
        board.addBoardListener(player);

        Game game = new Game(board, boardVisual, frame, player);
        game.start();
        // TODO Add jumping functionality
    }

    public void start(){

    }

    private Action shiftWorld = new AbstractAction(){
     @Override
     public void actionPerformed(ActionEvent e){
         if(!board.shiftDown()){
            System.out.println("Game Over!");
            gameTimer.stop();
        }
     }
    };
}