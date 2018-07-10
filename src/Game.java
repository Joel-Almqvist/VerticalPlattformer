import java.util.Timer;
import java.util.TimerTask;

public class Game{
    /** The delay in ms between each shift down.
     * NOTE: A delay less than 500 ms will likely cause
     * shifts to bunch up and execute in too quick succession.
     * */
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
        BoardFrame frame = new BoardFrame("Platformer", boardVisual);
        Player player = new Player(boardVisual, board);

        board.addBoardListener(boardVisual);
        board.addBoardListener(player);

        Game game = new Game(board, boardVisual, frame, player);
        game.start();


        // TODO 1 - Lägg till powerups
        // TODO 2 - Lägg till nya plattforms typer (super trampolin?, no-collision trampolin, osv)
        // TODO 3 - Förbättra plattforms genereringen (tänk på kollision vid hopp)
        //
        // Note för 3an, lägg till en "kollisions avstånds"-funktion i BlockPoint
        // 3 - Kanske använd vanligt avstånd och sedan en A* sökning på de redan hittade noderna
        // för att på så sätt se att de nåbara PÅ RIKTIGT.

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
                    System.out.println("You got "+board.getHighscore()+" points!");
                }
            }
        }, TICKRATE, TICKRATE);
    }

}