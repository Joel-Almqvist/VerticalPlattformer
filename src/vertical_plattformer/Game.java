package vertical_plattformer;

import java.util.Timer;
import java.util.TimerTask;

/**
 *  The purpose of the class is to create games and store metadata for them such as difficulty or
 *  information from a previous run. Currently this is not implemented and the class is quite bare-bone
 *  because of it. Its primary reponsibility currently is to instantiate the other classes
 *  and determine whether the game is over or not.

 *  Game has a reference to Board to be able to tell whether the game is over or not and a reference
 *  to Player to be able to tell Player that the game is over.
 *
 *  Game has many constants determining how the difficulty of the game escalates.
 */
public class Game{
    /** The delay in ms between each shift down at the start. */
    public final static int STARTING_SHIFT_INTERVAL = 600;
    /** The lowest value the shift delay is able to assume */
    public final static int LOWEST_SHIFT_INTERVAL = 200;
    /** How many points (IE rows the player reaches) to reach a new level */
    public final static int POINTS_TO_REACH_NEXT_LEVEL = 30;
    /** How many ms faster the next shift will be after a level up*/
    public final static int SHIFT_REDUCTION_PER_LEVEL = 100;

    /** How many times the player must level up before chunkGenerator
     *  generates fewer plattforms per chunk*/
    public final static int LEVELS_TO_FEWER_PLATTFORMS = 6;

    /** How many times the player must level up before chunkGenerator
     *  generates larger chunks*/
    public final static int LEVELS_TO_LARGER_CHUNKS = 4;

    /** How many times the player must level up before chunkGenerator
     *  generates chunks with higher min distance between chunks*/
    public final static int LEVELS_TO_LARGER_MIN_DIST = 4;

    private Board board;
    private Player player;
    private Timer gameTimer;
    private int shiftInterval;
    private int currentLevel = 1;


    Game(Board board, Player player){
        this.board = board;
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

        Game game = new Game(board, player);
        game.queueNextShift();
    }

    /** Shifts the board once, checks if the player has reached a new level
     * and queues the next shift.
     *
     * NOTE: The shifts take execution-time + shiftInterval and may as such
     * be slower or quicker than expected. Noteably takes longer when a new chunk is started.
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
                        levelUp();
                    }
                    // Queue next shift with possible new shiftInterval
                    queueNextShift();
                }
            }
        }, this.shiftInterval);
        }



    /** May increases the difficulty by :
     * 1: Making shifts down occuring more frequently
     * 2: Generating fewer plattforms per chunk
     * 3: Generating larger chunks
     * 4: Generating chunks with a larger minDistance
     *
     * NOTE: ChunkHandler stores chunks to use and as such the more difficult chunks are not seen instantly
     */
    public void levelUp(){
        currentLevel++;
        System.out.println("Level up, now at "+currentLevel);
        if(shiftInterval - SHIFT_REDUCTION_PER_LEVEL >= LOWEST_SHIFT_INTERVAL) {
            shiftInterval -= SHIFT_REDUCTION_PER_LEVEL;
        }
        else{
            shiftInterval = LOWEST_SHIFT_INTERVAL;
        }

        if(currentLevel % LEVELS_TO_FEWER_PLATTFORMS == 0){
            this.board.getChunkHandler().generateFewerPlattforms();
        }

        if(currentLevel % LEVELS_TO_LARGER_CHUNKS == 0){
                    this.board.getChunkHandler().generateLargerChunks();
        }

        if(currentLevel % LEVELS_TO_LARGER_MIN_DIST == 0){
                    this.board.getChunkHandler().generateWithHigherMinDist();
        }



    }

}