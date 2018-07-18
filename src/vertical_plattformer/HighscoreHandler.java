package vertical_plattformer;

/**
 * The HighscoreHandler is responsible for defining how the player get points. Board pushes
 * information for when the player's Y-coordinate changes, but what is done with that information
 * is defined by HighscoreHandler itself.
 *
 * Currently highscore is defined as the highest position the player has reached but this can
 * easily be changed.
 */
public class HighscoreHandler {
    private int currentPlayerHeight;
    private int highestReacedPosition;
    private int highscore;

    public int getHighscore(){
        return highscore;
    }

    /** This function is called whenever the player's height is changed, whether the
     * player moved up or down, and is responsible for changing the player's highscore
     * correspondingly.
     *
     * @param height A positive or negative integer indicating how far up the player's
     *               position has changed.
     */
    public void updateHighscore(int height){
        currentPlayerHeight += height;
        if(currentPlayerHeight > highestReacedPosition){
            highestReacedPosition = currentPlayerHeight;
            highscore = highestReacedPosition;
	}
    }
}
