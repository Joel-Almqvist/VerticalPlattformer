package vertical_plattformer;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ChunkHandler implements Runnable {
    private List<BlockType[][]> chunks;
    private int boardWidth;
    private ChunkGenerator chunkGenerator;
    private Timer generationTimer;

    public boolean initCompleted = false;
    private int chunkGenerationInterval;
    public final static int CHUNK_CAPACITY = 3;

    public ChunkHandler(BlockType[][] board, int lowestShiftRate){
        this.chunks = new ArrayList<>(CHUNK_CAPACITY+1);
        this.boardWidth = board[0].length;

        // This interval must be higher than shiftrate * chunkHeight
	// else the queue of chunks will deplete quicker than its replenished
	// 3 < chunkHeight -> the queue will never depletes
        this.chunkGenerationInterval = lowestShiftRate * 3;

        this.chunkGenerator = new ChunkGenerator(boardWidth);
	this.generationTimer = new Timer(true);
	this.chunks.add(chunkGenerator.generateChunk(board));
    }

    public void run(){
	this.generationTimer.scheduleAtFixedRate(new TimerTask(){
     		@Override
     		public void run(){
                      generateChunk();
		}
	},chunkGenerationInterval,chunkGenerationInterval);
	this.initCompleted = true;
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

    public void increaseMaxPlattformDistance(int amount){
        chunkGenerator.increaseMaxDist(amount);

    }


    public void decreasePlattformAmount(){
	chunkGenerator.lowerAmountOfPlattforms();
    }


}
