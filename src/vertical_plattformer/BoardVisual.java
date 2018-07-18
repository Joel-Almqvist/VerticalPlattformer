package vertical_plattformer;

import javax.swing.*;
import java.awt.*;

import static vertical_plattformer.Config.*;

/**
 * BoardVisual is responsible for visualising the current gamestate by creating an image and attaching it to BoardFrame.
 *
 * BoardVisual listens to Board, polls Board for more information, and is pushed information from Player and Game.
 * 1 - It polls Board for the current state of the board when Board indicate that change has occured.
 * 2 - Player pushes the name of the current powerup
 * 3 - Game pushes which level the player is on currently.
 */
public class BoardVisual extends JComponent implements BoardListener{
    private Board board;
    /** The coordinate within the JComponent where the score text is shown.
     * Save coordidnates as fields rather than recalculating it for every redraw*/
    private final int SCORE_X_COORDINATE;
    private final int SCORE_Y_COORDINATE;

    private String powerUpName = "";

    /** BoardVisual is not responsible for keeping track of which level player is in
     * and forfeits all controll of it to Game, which is why currentLevel is public.*/
    public int currentLevel = 1;

    public BoardVisual(Board board) {
        this.board = board;
        SCORE_X_COORDINATE = board.getWidth()*(RECTANGLE_WIDTH+SPACE_OFFSET) + HIGHSCORE_TEXT_PADDING;
        SCORE_Y_COORDINATE = board.getHeight() * RECTANGLE_HEIGHT / 4;
    }

    public void boardChange(){
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        final Graphics2D g2d = (Graphics2D) g;
        this.board.getHighscore();
        for(int r = 0; r < board.getHeight(); r++){
            for(int c = 0; c < board.getWidth(); c++){
                BlockType type = board.getBlockAt(r,c);
                switch (type) {
                    case PLAYER:
                        g2d.setColor(Color.RED);
                        break;
                    case PLATTFORM:
                        g2d.setColor(Color.BLACK);
                        break;
                    case AIR:
                        g2d.setColor(Color.WHITE);
                        break;
                    default:
                        g2d.setColor(Color.ORANGE);
                        break;
                }
                g2d.fillRect(RECTANGLE_WIDTH*c+c*SPACE_OFFSET,RECTANGLE_HEIGHT*r+r*SPACE_OFFSET,RECTANGLE_WIDTH,RECTANGLE_HEIGHT);
            }
        }
        g2d.setFont(new Font("Monospaced", Font.PLAIN, FONT_SIZE));
        g2d.setColor(Color.RED);
        g2d.drawString("Score: "+String.valueOf(board.getHighscore()),SCORE_X_COORDINATE,SCORE_Y_COORDINATE);

        g2d.setColor(Color.black);
        g2d.drawString("Current Powerup:",SCORE_X_COORDINATE,SCORE_Y_COORDINATE + TEXT_ROW_HEIGHT);

        g2d.setColor(Color.ORANGE);
        g2d.drawString(powerUpName,SCORE_X_COORDINATE,SCORE_Y_COORDINATE + 2 * TEXT_ROW_HEIGHT);

        g2d.setColor(Color.blue);
        g2d.drawString("Level: "+currentLevel,SCORE_X_COORDINATE,SCORE_Y_COORDINATE + 3 * TEXT_ROW_HEIGHT);
    }

    @Override
    public Dimension getPreferredSize(){
        super.getPreferredSize();
        return new Dimension((RECTANGLE_WIDTH+SPACE_OFFSET)*this.board.getWidth()+SCORE_WIDTH,
                             (RECTANGLE_HEIGHT+SPACE_OFFSET)*this.board.getHeight());
    }

    public void setPowerupName(String name){
        this.powerUpName = name;
    }

}

