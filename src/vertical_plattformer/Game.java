package vertical_plattformer;

import java.util.Timer;
import java.util.TimerTask;

public class Game{
    /** The delay in ms between each shift down.
     * NOTE: A delay less than 500 ms will likely cause
     * shifts to bunch up and execute in too quick succession.
     * */
    public final static int TICKRATE = 700;
    public final static int LOWEST_TICKRATE = 100;
    public final static int POINTS_TO_REACH_NEXT_LEVEL = 40;
    public final static int TICKRATE_REDUCTION_PER_LEVEL = 600;
    private Board board;
    private BoardVisual boardVisual;
    private BoardFrame frame;
    private Player player;
    private Timer gameTimer;
    private int tickrate;

    private int tickrateReduction = 0;
    private int currentLevel = 1;

    Game(Board board, BoardVisual boardVisual, BoardFrame frame, Player player){
        this.board = board;
        this.boardVisual = boardVisual;
        this.frame = frame;
        this.player = player;
        this.tickrate = TICKRATE;
        this.gameTimer = new Timer(true);
    }

    public static void main(String[] args) {
        Board board = new Board(20, 45, LOWEST_TICKRATE);
        BoardVisual boardVisual = new BoardVisual(board);
        BoardFrame frame = new BoardFrame("Platformer", boardVisual);
        Player player = new Player(boardVisual, board);

        board.addBoardListener(boardVisual);
        board.addBoardListener(player);

        Game game = new Game(board, boardVisual, frame, player);
        game.queueNextShift();

        //game.startGameTimer(0);


        // TODO 1 - Lägg till powerups
        // TODO 2 - Lägg till nya plattforms typer (super trampolin?, no-collision trampolin, osv)
        // TODO 3 - Lägg till highscore, få den att påverka ChunkGenerator för progressivt svårare banor

    }

    public void startGameTimer(int timeOffset){
        this.gameTimer.scheduleAtFixedRate(new TimerTask(){
            @Override
            public void run() {
                if (!player.playerAlive() || !board.shiftDown()) {
                    gameTimer.cancel();
                    player.stop();
                    System.out.println("GAME OVER");
                    System.out.println("You got "+board.getHighscore()+" points!");
                }
                else if(board.getHighscore() >= POINTS_TO_REACH_NEXT_LEVEL * (currentLevel)){
                    increaseDifficulty();
                }
            }
        }, TICKRATE - timeOffset, TICKRATE-timeOffset);
    }


    public void queueNextShift(){
        this.gameTimer.schedule(new TimerTask(){
            @Override
            public void run() {
                // Perform shift and see if game is over
                if (!player.playerAlive() || !board.shiftDown()) {
                    gameTimer.cancel();
                    player.stop();
                    System.out.println("GAME OVER");
                    System.out.println("You got "+board.getHighscore()+" points!");
                }
                else {
                    // Check whether to raise the difficulty
                    if(board.getHighscore() >= POINTS_TO_REACH_NEXT_LEVEL * (currentLevel)){
                        increaseDifficulty();
                    }
                    // Queue next shift with possible new tickrate
                    queueNextShift();
                }
            }
        }, this.tickrate);
        }



    /**
     * Alerts the ChunkGenerator to create more difficult chunks for the player.
     *
     * NOTE: ChunkHandler stores chunks to use and as such the more difficult chunk is not seen instantly
     */
    public void increaseDifficulty(){
        currentLevel++;
        System.out.println("Level up, now at "+currentLevel);
        if(tickrate - TICKRATE_REDUCTION_PER_LEVEL >= LOWEST_TICKRATE) {
            // TODO TICKRATE NEEDS A PROPER NAME
            tickrate -= TICKRATE_REDUCTION_PER_LEVEL;
        }

        // Alert chunk generator
    }

}