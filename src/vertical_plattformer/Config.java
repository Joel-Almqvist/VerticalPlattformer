package vertical_plattformer;


/**
 * This project uses a lot of constants, Config gathers them all in one place.
 */
public final class Config {

    // GAME  **************************************************************

    /** The delay in ms between each shiftDown at the start of the game. */
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

    /** The width used to create board in main() */
    public final static int BOARD_WIDTH= 20;

    /** The height used to create board in main() */
    public final static int BOARD_HEIGHT = 45;



    // PLAYER **********************************************************
    /** How long the player must be in the air before falling one block down */
    public static final int FALLTIME = 120;

    /** The interval between calling moveRight/Left if holding down the corresponding key */
    public static final int MOVEMENTSPEED = 75;

    /** How long a powerup should last in ms*/
    public static final int POWERUP_DURATION = 5000;

    /** How high the player jumps with no powerups in a SINGLE jump  */
    public static final int JUMP_HEIGHT = 5;


    // CHUNKHANDLER ***************************************************************
    /** How many chunks ChunkHandler can maximally store */
    public final static int CHUNK_CAPACITY = 3;


    // CHUNKGENERATION *******************************************************
    /** The maximal distance between two BlockPoint to be considered reachable */
    public final static int MAX_DISTANCE = 2 * JUMP_HEIGHT -1 ; //SHOULD BE JUMPHEIGHT -1


    // BOARD **********************************************************************
    /** How many rows the player should stand on at the start*/
    public static final int STARTING_ROWS = 8;


    // BOARDVISUAL ****************************************************************
    /** The height of every block when drawn */
    public final static int RECTANGLE_HEIGHT = 20;

    /** The width of every block when drawn */
    public final static int RECTANGLE_WIDTH = 20;

    /** The whitespace between two blocks*/
    public final static int SPACE_OFFSET = 2;

    /** How much extra to the right the window should expand after the board  */
    public final static int SCORE_WIDTH = 280;

    /** The padding between the board itself and the text to the right of it*/
    public final static int HIGHSCORE_TEXT_PADDING = 15;

    /** The height of every row containing text */
    public final static int TEXT_ROW_HEIGHT = 20;
    /** The font size for all text to the right of board */
    public final static int FONT_SIZE = 22;

    private Config(){};


}
