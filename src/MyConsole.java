import org.swingplus.JHyperlink;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public final class MyConsole implements Constants{

    private static JPanel console;
    private static ArrayList<JComponent> consoleText;

    static {
        console = new JPanel();
        console.setLayout(new BoxLayout(console,BoxLayout.Y_AXIS));
        console.setPreferredSize(new Dimension(WINDOW_WIDTH / 3, WINDOW_HEIGHT));
    }

    public static void printLog(String str) {
        console.add(new JLabel(str));
        console.repaint();
        console.updateUI();
    }

    public static void printLog(String str, boolean isLink) {
        if (isLink) console.add(new JHyperlink(str,str.trim()));
    }

    public static JPanel cmd() {
        return console;
    }

    public static void clear(){
        console.removeAll();
        console.repaint();
        console.updateUI();
    }
}