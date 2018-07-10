package vertical_plattformer;

import javax.swing.*;
import java.awt.*;

/** vertical_plattformer.BoardVisual draws the current state of vertical_plattformer.Board within vertical_plattformer.BoardFrame.
 */
public class BoardVisual extends JComponent implements BoardListener{
    public Board board;
    public final static int RECTANGLE_HEIGHT = 20;
    public final static int RECTANGLE_WIDTH = 20;
    public final static int SPACE_OFFSET = 2;
    public final static int SCORE_WIDTH = 250;

    public BoardVisual(Board board) {
        this.board = board;
    }

    public void boardChange(){
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        final Graphics2D g2d = (Graphics2D) g;
        BlockType[][] currentBoard = this.board.getBoard();
        for(int r = 0; r < board.getHeight(); r++){
            for(int c = 0; c < board.getWidth(); c++){
                BlockType type = currentBoard[r][c];
                switch (type) {
                    case PLAYER:
                        g2d.setColor(Color.RED);
                        break;
                    case PLATTFORM:
                        g2d.setColor(Color.BLACK);
                        break;
                    default:
                        g2d.setColor(Color.WHITE);
                        break;
                    }

                    g2d.fillRect(RECTANGLE_WIDTH*c+c*SPACE_OFFSET,RECTANGLE_HEIGHT*r+r*SPACE_OFFSET,RECTANGLE_WIDTH,RECTANGLE_HEIGHT);
                }
            }
    }

    @Override
    public Dimension getPreferredSize(){
        super.getPreferredSize();
        return new Dimension((RECTANGLE_WIDTH+SPACE_OFFSET)*this.board.getWidth()+SCORE_WIDTH,
                             (RECTANGLE_HEIGHT+SPACE_OFFSET)*this.board.getHeight());
    }
}

