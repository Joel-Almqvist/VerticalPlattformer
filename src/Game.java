import java.util.Timer;
import java.util.TimerTask;

public class Game{
    public final static int TICKRATE = 500;
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
        this.tickrate = TICKRATE;
        this.gameTimer = new Timer(true);
    }

    public static void main(String[] args) {
        Board board = new Board(26, 35);
        BoardVisual boardVisual = new BoardVisual(board);
        BoardFrame frame = new BoardFrame(board, "Platformer", boardVisual);
        Player player = new Player(boardVisual, board);

        board.addBoardListener(boardVisual);
        board.addBoardListener(player);

        Game game = new Game(board, boardVisual, frame, player);
        game.start();
        // TODO Fix fullösning and add jump collision!

        // TODO Fixa initialiseringen av ChunkHandler
    }

    public void start(){
        this.gameTimer.scheduleAtFixedRate(new TimerTask(){
            @Override
            public void run() {
                boolean isGameOver = !board.shiftDown();
                if (!player.playerAlive() || isGameOver) {
                    gameTimer.cancel();
                    player.stop();
                    System.out.println("GAME OVER");
                }
            }
        }, TICKRATE, TICKRATE);
    }

}