import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PrinterException;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URISyntaxException;

public class GUI_autoria extends JFrame implements ActionListener {

    private static final int WINDOW_WIDTH = 550;
    private static final int WINDOW_HEIGHT = 600;
    private static final int WINDOW_MARGIN_X = 150;
    private static final int WINDOW_MARGIN_Y = 50;

    private JPanel mainContainer;
    private JPanel console;

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


        // TODO: 15.01.2020 !!! 
        // main panel
        mainContainer = new JPanel ();
        this.add(mainContainer);

        // left panel
        console = new Console(WINDOW_WIDTH / 2, WINDOW_HEIGHT);
        mainContainer.add(console);

        // right interact menu
        mainContainer.add(createRightMenu());

    }

    private JPanel createRightMenu() {
        JPanel rightMenu = new JPanel();
        rightMenu.setLayout(new BoxLayout(rightMenu, BoxLayout.Y_AXIS));

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
            AutoScan as = new AutoScan(Integer.parseInt(inputNumOfAuto.getText()),
                    Integer.parseInt(inputMinYear.getText()),
                    Integer.parseInt(inputMaxPrice.getText()));
            try {
                mainContainer.add(as.doAutoScan());
                SwingUtilities.updateComponentTreeUI(mainContainer);
            } catch (IOException | ClassNotFoundException | BadLocationException | PrinterException | URISyntaxException ex) {
                ex.printStackTrace();
            }
        }
        else if (e.getSource() == buttonManualSearch){

        }
    }


    public static class MyThread extends Thread {
        public void run() {
            while (true){
                System.out.println(" Run !!!");
            }
        }
    }

    public void onClick() {
        MyThread myThread = new MyThread();
        myThread.start();
    }
}
