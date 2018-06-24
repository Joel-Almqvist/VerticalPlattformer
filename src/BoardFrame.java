import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BoardFrame extends JFrame{
    public Board board;
    private BoardVisual comp;

    public BoardFrame(Board board, String windowName, BoardVisual comp) throws HeadlessException {
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