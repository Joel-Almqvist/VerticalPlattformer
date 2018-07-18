package vertical_plattformer;

import javax.swing.*;
import java.awt.*;

import static vertical_plattformer.Config.*;

/** vertical_plattformer.BoardVisual draws the current state of vertical_plattformer.Board within vertical_plattformer.BoardFrame.
 */
public class BoardVisual extends JComponent implements BoardListener{
    public Board board;


    /** The coordinate within the JComponent where the score text is shown.
     * Save coordidnate as field rather than recalculating it for every redraw*/
    private final int SCORE_X_COORDINATE;
    private final int SCORE_Y_COORDINATE;


    private String powerUpName = "None";

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
        g2d.drawString("Current Powerup:",SCORE_X_COORDINATE,SCORE_Y_COORDINATE+TEXT_ROW_HEIGHT);

        g2d.setColor(Color.ORANGE);
        g2d.drawString(powerUpName,SCORE_X_COORDINATE,SCORE_Y_COORDINATE + 2 * TEXT_ROW_HEIGHT);
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

