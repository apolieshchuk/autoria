import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class GUI_autoria extends JFrame implements ActionListener {

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
        this.setBounds(200,100,550,250); // position
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // main panel
        JPanel containerMain = new JPanel();
        containerMain.setLayout(new BoxLayout(containerMain,BoxLayout.Y_AXIS));
        this.add(containerMain);

        // UP CONTAINER
        JPanel containerUp = new JPanel(new GridLayout(3, 0) );
        // NORTH (AUTO-SCAN)
        JLabel l = new JLabel("AUTO.RIA.COM. AUTO-SCANNER ");
        l.setHorizontalAlignment(JLabel.CENTER);
        containerUp.add(l);
        // wrapper
        JPanel wrapperUp = new JPanel();
        wrapperUp.add(labelNumOfAuto, BorderLayout.SOUTH);
        wrapperUp.add(inputNumOfAuto, BorderLayout.SOUTH);
        wrapperUp.add(labelMinYear, BorderLayout.SOUTH);
        wrapperUp.add(inputMinYear, BorderLayout.SOUTH);
        wrapperUp.add(labelMaxPrice, BorderLayout.SOUTH);
        wrapperUp.add(inputMaxPrice, BorderLayout.SOUTH);
        containerUp.add(wrapperUp);
        //button
        containerUp.add(buttonAutoScan);
        buttonAutoScan.addActionListener(this);

        // Bottom container
        JPanel containerBot = new JPanel(new GridLayout(3, 0) );
        // SOUTH (MANUAL-SCAN)
        JLabel l2 = new JLabel("AUTO.RIA.COM. MANUAL-SCANNER ");
        l2.setHorizontalAlignment(JLabel.CENTER);
        containerBot.add(l2);
        JPanel wrapperBot = new JPanel();
        wrapperBot.add(labelAutoManual, BorderLayout.SOUTH);
        wrapperBot.add(inputAutoManual, BorderLayout.SOUTH);
        wrapperBot.add(labelMileageManual, BorderLayout.SOUTH);
        wrapperBot.add(inputMileageManual, BorderLayout.SOUTH);
        wrapperBot.add(labelYearManual, BorderLayout.SOUTH);
        wrapperBot.add(inputYearManual, BorderLayout.SOUTH);
        wrapperBot.add(labelGboManual, BorderLayout.SOUTH);
        wrapperBot.add(inputGboManual, BorderLayout.SOUTH);
        containerBot.add(wrapperBot);
        //button
        containerBot.add(buttonManualSearch);
        buttonManualSearch.addActionListener(this);

        // add containers in main pane
        containerMain.add(containerUp);
        containerMain.add(containerBot);
    }

    public void actionPerformed(ActionEvent e){
        if (e.getSource() == buttonAutoScan){
            AutoScan as = new AutoScan(Integer.parseInt(inputNumOfAuto.getText()),
                    Integer.parseInt(inputMinYear.getText()),
                    Integer.parseInt(inputMaxPrice.getText()));
            try {
                as.doAutoScan();
            } catch (IOException | ClassNotFoundException ex) {
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
