package vertical_plattformer;

import java.util.Timer;
import java.util.TimerTask;

import static vertical_plattformer.Config.*;

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

    private Board board;
    private BoardVisual boardVisual;
    private Player player;
    private Timer gameTimer;
    private int shiftInterval;
    private double timeToNextShift;
    private int currentLevel = 1;


    Game(Board board, Player player, BoardVisual boardVisual){
        this.board = board;
        this.player = player;
        this.boardVisual = boardVisual;
        this.shiftInterval = STARTING_SHIFT_INTERVAL;
        this.timeToNextShift = shiftInterval;
        this.gameTimer = new Timer(true);
    }

    public static void main(String[] args) {
        Board board = new Board(BOARD_WIDTH, BOARD_HEIGHT);
        BoardVisual boardVisual = new BoardVisual(board);
        // The reference to frame is never used, but BoardVisual which lives within
        // the frame and is used contiously, IE warning ignored knowingly.
        BoardFrame frame = new BoardFrame("Platformer", boardVisual);
        Player player = new Player(boardVisual, board);

        board.addBoardListener(boardVisual);
        board.addBoardListener(player);

        Game game = new Game(board, player, boardVisual);
        game.queueNextShift();

        // TODO 3: Mer kommentarer
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
                double startTime = System.currentTimeMillis();
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

                    double executionLength = System.currentTimeMillis() - startTime;
                    timeToNextShift = shiftInterval - executionLength;

                    // Queue next shift with possible new shiftInterval
                    queueNextShift();
                }
            }
        }, (long) this.timeToNextShift);
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
        boardVisual.currentLevel = currentLevel;
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