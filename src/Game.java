import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.Timer;
import java.util.TimerTask;

public class Game{
    public final static int STARTING_TICKRATE = 350;
    private Board board;
    private BoardVisual boardVisual;
    private BoardFrame frame;
    private Player player;
    private java.util.Timer gameTimer;
    private int tickrate;

    Game(Board board, BoardVisual boardVisual, BoardFrame frame, Player player){
        this.board = board;
        this.boardVisual = boardVisual;
        this.frame = frame;
        this.player = player;
        this.tickrate = STARTING_TICKRATE;
        //this.gameTimer = new Timer(this.tickrate, shiftWorld);
        this.gameTimer = new Timer();
    }

    public static void main(String[] args) {
        Board board = new Board(30, 40);
        BoardVisual boardVisual = new BoardVisual(board);
        BoardFrame frame = new BoardFrame(board, "Platformer", boardVisual);
        Player player = new Player(boardVisual, board);

        board.addBoardListener(boardVisual);
        board.addBoardListener(player);

        Game game = new Game(board, boardVisual, frame, player);
        game.start();
        // TODO Fix fullösning and add jump collision!
        // TODO Fixa en klass som håller i alla chunks och generar nya vid behov,
        // TODO denna klass behöver ha en egen tråd
    }

    public void start(){
        this.gameTimer.scheduleAtFixedRate(new TimerTask(){
            @Override
            public void run(){
                if(!player.playerAlive() || !board.shiftDown()) {
                             gameTimer.cancel();
                             player.stop();
                             System.out.println("GAME OVER");
                         }
            }
        },STARTING_TICKRATE,STARTING_TICKRATE);
        //this.gameTimer.start();
    }

    private Action shiftWorld = new AbstractAction(){
     @Override
     public void actionPerformed(ActionEvent e){
         if(!player.playerAlive() || !board.shiftDown()) {
             //gameTimer.stop();
             player.stop();
             System.out.println("GAME OVER");
         }
     }
    };
}