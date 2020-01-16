import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;

public class GUI_autoria extends JFrame implements ActionListener, Constants {

    private static final int WINDOW_MARGIN_X = 150;
    private static final int WINDOW_MARGIN_Y = 50;

    private JPanel mainContainer;
    private MyConsole myConsole;

    /* Auto Input */
    private JLabel labelNumOfAuto = new JLabel("Scanned auto:");
    private JLabel labelMinYear = new JLabel("Min year:");
    private JLabel labelMaxPrice = new JLabel("Max price:");
    private JTextField inputNumOfAuto = new JTextField("5", 5);
    private JTextField inputMinYear = new JTextField("2008", 5);
    private JTextField inputMaxPrice = new JTextField("7000", 5);
    private JButton buttonAutoScan = new JButton("Auto-scan");

    /* Manual Input */
    private JLabel labelAutoManual = new JLabel("Auto: ");
    private JLabel labelMileageManual = new JLabel("Mileage (тыс.км):");
    private JLabel labelYearManual = new JLabel("Year:");
    private JLabel labelGboManual = new JLabel("GBO:");
    private JTextField inputAutoManual = new JTextField("Daewoo Lanos", 10);
    private JTextField inputMileageManual = new JTextField("100", 5);
    private JTextField inputYearManual = new JTextField("2008", 5);
    private JComboBox inputGboManual = new JComboBox(new String [] {"ДА", "НЕТ"});
    private JButton buttonManualSearch = new JButton("Manual-scan");

    public static void main(String[] args) {
        GUI_autoria app = new GUI_autoria();
        app.setVisible(true);
    }

    public GUI_autoria() throws HeadlessException {
        super("Autoria scanner");
        this.setBounds(WINDOW_MARGIN_X,WINDOW_MARGIN_Y, WINDOW_WIDTH, WINDOW_HEIGHT); // position
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // main panel
        mainContainer = new JPanel ();
        mainContainer.setLayout(new BorderLayout());
        this.add(mainContainer);

        // left panel
        JScrollPane sp = new JScrollPane(MyConsole.cmd());
        sp.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
        sp.getVerticalScrollBar().setUnitIncrement(16);
        mainContainer.add(sp, BorderLayout.WEST);

        // right interact menu
        mainContainer.add(createRightMenu(), BorderLayout.EAST);
    }

    private JPanel createRightMenu() {
        JPanel rightMenu = new JPanel();
        rightMenu.setLayout(new BoxLayout(rightMenu, BoxLayout.Y_AXIS));
        // rightMenu.setBackground(Color.blue);

        /* Auto-scan block */
        // label
        JLabel l = new JLabel("AUTO.RIA.COM. AUTO-SCANNER ");
        l.setHorizontalAlignment(JLabel.CENTER);
        rightMenu.add(l);

        // Interactors
        rightMenu.add(createAutoScanInteractors());

        /* Manual -scan block*/
        // label
        JLabel l2 = new JLabel("AUTO.RIA.COM. MANUAL-SCANNER ");
        l2.setHorizontalAlignment(JLabel.CENTER);
        rightMenu.add(l2);

        // Interactors
        rightMenu.add(createManualScanInteractors());

        return rightMenu;
    }

    private JPanel createManualScanInteractors() {
        JPanel wrapper = new JPanel();

        wrapper.add(labelAutoManual, BorderLayout.SOUTH);
        wrapper.add(inputAutoManual, BorderLayout.SOUTH);
        wrapper.add(labelMileageManual, BorderLayout.SOUTH);
        wrapper.add(inputMileageManual, BorderLayout.SOUTH);
        wrapper.add(labelYearManual, BorderLayout.SOUTH);
        wrapper.add(inputYearManual, BorderLayout.SOUTH);
        wrapper.add(labelGboManual, BorderLayout.SOUTH);
        wrapper.add(inputGboManual, BorderLayout.SOUTH);

        //button
        wrapper.add(buttonManualSearch);
        buttonManualSearch.addActionListener(this);

        return wrapper;
    }

    private JPanel createAutoScanInteractors() {
        JPanel wrapper = new JPanel();
        // wrapper.setBackground(Color.red);

        wrapper.add(labelNumOfAuto);
        wrapper.add(inputNumOfAuto);
        wrapper.add(labelMinYear);
        wrapper.add(inputMinYear);
        wrapper.add(labelMaxPrice);
        wrapper.add(inputMaxPrice);

        //button
        wrapper.add(buttonAutoScan);
        buttonAutoScan.addActionListener(this);

        return wrapper;

    }

    public void actionPerformed(ActionEvent e){
        if (e.getSource() == buttonAutoScan){
            MyConsole.clear();
            AutoScan as = new AutoScan(Integer.parseInt(inputNumOfAuto.getText()),
                    Integer.parseInt(inputMinYear.getText()),
                    Integer.parseInt(inputMaxPrice.getText()));
            as.start();
            new KeyBlocker(as, buttonAutoScan).start();
        }
        else if (e.getSource() == buttonManualSearch){

        }
    }

    public static class KeyBlocker extends Thread {

        private JComponent blockedKey;
        private AutoScan runningThread;

        public KeyBlocker(AutoScan thread, JButton buttonAutoScan) {
            blockedKey = buttonAutoScan;
            runningThread = thread;
        }

        public void run() {
            blockedKey.setEnabled(false);
            while (runningThread.getState() == State.RUNNABLE)
            blockedKey.setEnabled(true);
        }
    }
}
