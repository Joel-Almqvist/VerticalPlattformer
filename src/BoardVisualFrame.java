import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BoardVisualFrame extends JFrame{
    public Board board;
    private BoardVisualComponent comp;

    public BoardVisualFrame(Board board, String windowName, BoardVisualComponent comp) throws HeadlessException {
        super(windowName);
        this.board = board;
        this.comp = comp;

        //setUpKeyBinds();
        createMenus();
        setLayout(new BorderLayout());
        add(comp, BorderLayout.CENTER);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        pack();
        setVisible(true);
    }
    /*
    private void setUpKeyBinds(){
        comp.getInputMap().put(KeyStroke.getKeyStroke("RIGHT"), "moveRight");
        comp.getActionMap().put("moveRight", moveRight);

        comp.getInputMap().put(KeyStroke.getKeyStroke("LEFT"), "moveLeft");
        comp.getActionMap().put("moveLeft", moveLeft);

        comp.getInputMap().put(KeyStroke.getKeyStroke("UP"), "turn");
        comp.getActionMap().put("turn", turn);
    }


    private Action moveRight = new AbstractAction(){
        @Override
        public void actionPerformed(ActionEvent e) {
            board.moveTetroRight();
        }
    };

    private Action moveLeft = new AbstractAction(){
        @Override
        public void actionPerformed(ActionEvent e){
            board.moveTetroLeft();
        }
    };


    private Action turn = new AbstractAction(){
        @Override
        public void actionPerformed(ActionEvent e){
            board.turnTetroRight();
        }
    };
	*/

    public void createMenus(){
        final JMenuBar menuBar = new JMenuBar();
        final JMenu file = new JMenu("File");
        file.addSeparator();
        menuBar .add(file);
        JMenuItem exit = new JMenuItem("Exit");
        file.add(exit);
        exit.addActionListener(new MyActionListener());
        this.setJMenuBar(menuBar);
    }

    private class MyActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if(JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(null, "Really want to exit?")){
                System.exit(0);
            }
        }
    }
}