import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ChunkHandler implements Runnable {
    private List<BlockType[][]> chunks;
    private int boardWidth;
    private PlattformGenerator plattformGenerator;
    private Timer generationTimer;

    public boolean initCompleted = false;
    public final static int CHUNK_GENERATION_INTERVAL = 2000;
    public final static int CHUNK_CAPACITY = 4;

    public ChunkHandler(BlockType[][] board){
        this.chunks = new ArrayList<>(CHUNK_CAPACITY+1);
        this.boardWidth = board[0].length;
        this.plattformGenerator = new PlattformGenerator(boardWidth);
	this.generationTimer = new Timer(true);
	this.chunks.add(plattformGenerator.generateChunk(board));
    }

    public void run(){
	this.generationTimer.scheduleAtFixedRate(new TimerTask(){
     		@Override
     		public void run(){
                      generateChunk();
		}
	},CHUNK_GENERATION_INTERVAL,CHUNK_GENERATION_INTERVAL);
	this.initCompleted = true;
    }

    /** Used to fill the ChunkHandler's chunk list manually
     */
    public void fillList(){
	for(int i = 0; i < CHUNK_CAPACITY-chunks.size(); i++){
	    generateChunk();
	}
    }


    /** Create a new chunk based on the appearance of the last chunk in the chunk-list.
     */
    private void generateChunk(){
        if(this.chunks.size() < CHUNK_CAPACITY) {
	    //System.out.println("generating chunk");
	    BlockType[][] lastChunk = this.chunks.get(this.chunks.size() - 1);
	    BlockType[][] newChunk = plattformGenerator.generateChunk(invertChunk(lastChunk));
	    this.chunks.add(newChunk);
	}
    }

    /** Inverts the rows in a given chunk
     */
    private BlockType[][] invertChunk(BlockType[][] chunk){
        BlockType[][] invertedChunk = new BlockType[chunk.length][];
        for(int i = 0; i < chunk.length; i++){
	    invertedChunk[i] = chunk[chunk.length - i -1];
	}
	return invertedChunk;
    }

    public BlockType[][] getNextChunk(){
        BlockType[][] nextChunk = this.chunks.get(0);
        this.chunks.remove(0);
        return nextChunk;
    }


}
