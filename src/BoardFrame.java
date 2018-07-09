import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/** BoardFrame is the JFrame upon which BoardVisual is drawn.
 *  To do this BoardFrame needs a BoardVisual to add.
 *
 */
public class BoardFrame extends JFrame{

    public BoardFrame(String windowName, BoardVisual comp) throws HeadlessException {
        super(windowName);

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