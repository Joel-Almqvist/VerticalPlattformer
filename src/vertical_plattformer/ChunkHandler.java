package vertical_plattformer;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static vertical_plattformer.Config.CHUNK_CAPACITY;
import static vertical_plattformer.Config.CHUNK_CHECK_INTERVAL;

/**
 * ChunkHandler is responsible for mainting a list of chunks which Board reads from
 * when the board needs to be expanded with a new chunk. This list is ordered where
 * the first element in it will be appended to the current board, and the second element
 * will be appended to the first element and so on. Board reads at the start of this list
 * whereas ChunkGenerator reads the end of this list to create new chunks.
 *
 * ChunkHandler periodically checks if its list of chunk is at MAX_CAPACITY, if this
 * is not the case another chunk is requested by ChunkGenerator.
 *
 * ChunkHandler runs on its own thread in order to reduce the amount of work is needed
 * to be done by shiftDown() as shiftDown() never should have to create a chunk. This
 * should make every shiftDown() take more uniformly distributed time and prevent certain
 * shifts from taking significantly longer than others.
 */
public class ChunkHandler implements Runnable {
    private List<BlockType[][]> chunks;
    private ChunkGenerator chunkGenerator;
    private Timer generationTimer;


    public ChunkHandler(BlockType[][] board){
        this.chunks = new ArrayList<>();

        // This interval must be higher than shiftrate * chunkHeight
	// else the queue of chunks will deplete quicker than its replenished
	// 3 < chunkHeight -> the queue will never depletes

        this.chunkGenerator = new ChunkGenerator(board[0].length);
	this.generationTimer = new Timer(true);
	this.chunks.add(chunkGenerator.generateChunk(board));
    }

    public void run(){
	this.generationTimer.scheduleAtFixedRate(new TimerTask(){
     		@Override
     		public void run(){
                      generateChunk();
		}
	},CHUNK_CHECK_INTERVAL,CHUNK_CHECK_INTERVAL);
    }

    /** Used to fill the vertical_plattformer.ChunkHandler's chunk list manually
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
	    BlockType[][] newChunk = chunkGenerator.generateChunk(invertChunk(lastChunk));
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


    public void generateLargerChunks(){
        chunkGenerator.increaseChunkHeight();
    }

    public void generateWithHigherMinDist(){
        chunkGenerator.increaseMinDistance();
    }

    public void generateFewerPlattforms(){
	chunkGenerator.lowerAmountOfPlattforms();
    }


}
