package vertical_plattformer;

public class HighscoreHandler {
    private int currentPlayerHeight;
    private int topPlayerHeight;

    public int getHighscore(){
        return topPlayerHeight;
    }

    public void playerMovedUp(int height){
        currentPlayerHeight += height;
        if(currentPlayerHeight > topPlayerHeight){
            topPlayerHeight = currentPlayerHeight;
	}
    }
}
