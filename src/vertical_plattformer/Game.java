package vertical_plattformer;

import java.util.Timer;
import java.util.TimerTask;

public class Game{
    /** The delay in ms between each shift down.
     * NOTE: A delay less than 500 ms will likely cause
     * shifts to bunch up and execute in too quick succession.
     * */
    public final static int STARTING_SHIFT_INTERVAL = 700;
    public final static int LOWEST_SHIFT_INTERVAL = 150;
    public final static int POINTS_TO_REACH_NEXT_LEVEL = 40;
    public final static int INTERVAL_REDUCTION_PER_LEVEL = 100;
    /** How many times the player must level up before chunkGenerator creates harder chunks*/
    public final static int LEVELS_TO_INCREASE_CHUNK_DIFFICULTY = 3;


    private Board board;
    private BoardVisual boardVisual;
    private BoardFrame frame;
    private Player player;
    private Timer gameTimer;
    private int shiftInterval;
    private int currentLevel = 1;


    Game(Board board, BoardVisual boardVisual, BoardFrame frame, Player player){
        this.board = board;
        this.boardVisual = boardVisual;
        this.frame = frame;
        this.player = player;
        this.shiftInterval = STARTING_SHIFT_INTERVAL;
        this.gameTimer = new Timer(true);
    }

    public static void main(String[] args) {
        Board board = new Board(20, 45, LOWEST_SHIFT_INTERVAL);
        BoardVisual boardVisual = new BoardVisual(board);
        BoardFrame frame = new BoardFrame("Platformer", boardVisual);
        Player player = new Player(boardVisual, board);

        board.addBoardListener(boardVisual);
        board.addBoardListener(player);

        Game game = new Game(board, boardVisual, frame, player);
        game.queueNextShift();


        // TODO 1 - Lägg till powerups
        // TODO 2 - Lägg till nya plattforms typer (super trampolin?, no-collision trampolin, osv)
        // TODO 3 - Visa highscore
    }

    /** Shifts the board once, checks if the player has reached a new level
     * and queues the next shift.
     */
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
                    // Queue next shift with possible new shiftInterval
                    queueNextShift();
                }
            }
        }, this.shiftInterval);
        }



    /** Increases the difficulty by making shifts down occuring more frequently and reducing
     * how many plattforms exist in every chunk.
     *
     * NOTE: ChunkHandler stores chunks to use and as such the more difficult chunks are not seen instantly
     */
    public void increaseDifficulty(){
        currentLevel++;
        System.out.println("Level up, now at "+currentLevel);
        if(shiftInterval - INTERVAL_REDUCTION_PER_LEVEL >= LOWEST_SHIFT_INTERVAL) {
            shiftInterval -= INTERVAL_REDUCTION_PER_LEVEL;
        }
        else{
            shiftInterval = LOWEST_SHIFT_INTERVAL;
        }

        if(currentLevel % LEVELS_TO_INCREASE_CHUNK_DIFFICULTY == 0){
            this.board.getChunkHandler().decreasePlattformAmount();
        }
    }

}