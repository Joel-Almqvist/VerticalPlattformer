import javax.swing.*;
import java.awt.*;

public class BoardVisualComponent extends JComponent{


        public Board board;
        public final static int RECTANGLE_HEIGHT = 12;
        public final static int RECTANGLE_WIDTH = 12;
        public final static int SPACE_OFFSET = 2;
        public final static int SCORE_WIDTH = 250;
        /** The margin between the end of the board
         * and the start of the text*/
        public final static int TEXT_RIGHT_MARGIN = 30;
        /** The top offset of the text */
        public final static int TEXT_TOP_MARGIN = 40;
        /** The marginal between  the POWERUP text and the actuall name of the powerup */
        public final static int MARGINAL_BETWEEN_POWERUP = 40;
        /** The space between the score text and powerup text*/
        public final static int TEXT_POWERUP_TOP_MARGIN = 120;
        public final static int FONT_SIZE_SCORE = 22;
        public final static int FONT_SIZE_POWERUP = 30;


        public BoardVisualComponent(Board board) {
            this.board = board;
        }

        public void boardChange(){
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g){
            super.paintComponent(g);
            final Graphics2D g2d = (Graphics2D) g;
            for(int r = 0; r < board.getHeight(); r++){
                for(int c = 0; c < board.getWidth(); c++){
                    boolean empyRect = false;

                    BlockType type = board.getBlockAt(c,r);
                    switch (type) {

                        case AIR:
                            g2d.setColor(Color.WHITE);
                            break;
                        case PLAYER:
                            g2d.setColor(Color.RED);
                            break;
			case PLATTFORM:
                            g2d.setColor(Color.BLACK);
                            break;
                        default:
                            //g2d.setColor(Color.BLACK);
                            //empyRect = true;
                            break;
                        }

                        if(true){
                            g2d.fillRect(RECTANGLE_WIDTH*c+c*SPACE_OFFSET,RECTANGLE_HEIGHT*r+r*SPACE_OFFSET,RECTANGLE_WIDTH,RECTANGLE_HEIGHT);
                        }
                        else{
                            g2d.drawRect(RECTANGLE_WIDTH*c+c*SPACE_OFFSET,RECTANGLE_HEIGHT*r+r*SPACE_OFFSET,RECTANGLE_WIDTH,RECTANGLE_HEIGHT);
                        }
                    }
                }
        }

        @Override
        public Dimension getPreferredSize(){
            super.getPreferredSize();
            //Increment RECTANGLE_WIDTH and RECTANGLE_HEIGHT slightly since container must be larger than the board
            //return new Dimension(board.getWidth()*(RECTANGLE_WIDTH+2+SCORE_WIDTH),(RECTANGLE_HEIGHT+2)*board.getHeight());
            //return new Dimension(1000,1000);

            System.out.println("height = "+this.board.getHeight());
            System.out.println("RECT HEIGHT = "+RECTANGLE_HEIGHT);
            int x = (RECTANGLE_HEIGHT+3*SPACE_OFFSET)*this.board.getHeight()+100;
            System.out.println("calced height = "+x);


            //int y = Math.min((RECTANGLE_HEIGHT+SPACE_OFFSET)*this.board.getHeight(), 500);

            return new Dimension((RECTANGLE_WIDTH+SPACE_OFFSET)*this.board.getWidth()+SCORE_WIDTH,
                                 (RECTANGLE_HEIGHT+SPACE_OFFSET)*this.board.getHeight());
        }
}

