import org.swingplus.JHyperlink;

import javax.swing.*;
import java.awt.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Stack;

public final class MyConsole implements Constants{

    private static JPanel console;
    private static Stack<JComponent> consoleText;

    static {
        console = new JPanel();
        console.setLayout(new BoxLayout(console,BoxLayout.Y_AXIS));
        console.setPreferredSize(new Dimension(400, WINDOW_HEIGHT * 2));
    }

    /* Print message in console */
    public static void printLog(String str) {
        addMsgInConsole(createMsg(str));
    }

    /* Create message component */
    private static JComponent createMsg(String str){
        JComponent component;
        try {
            URL link = new URL(str.trim()); // if wrong url - throw exception
            component = new JHyperlink(str.trim(), str.trim());
        } catch (MalformedURLException e) {
            component = new JLabel(str);
        }
        return component;
    }

    /* Add JComponent in console */
    private static void addMsgInConsole (JComponent component){
        console.add(component);
        consoleText.push(component);
        console.repaint();
        console.updateUI();
    }

    /* Print message in console in color */
    public static void printLog(String str, Color color) {
        JComponent component = createMsg(str);
        component.setForeground(color);
        addMsgInConsole(component);
    }

    public static void removeLastMsg(){
        consoleText.pop();
        console.removeAll();
        for (JComponent component : consoleText) {
            console.add(component);
        }
        console.repaint();
        console.updateUI();
    }

    public static JPanel cmd() {
        return console;
    }

    public static void clear(){
        console.removeAll();
        consoleText = new Stack<>();
        console.repaint();
        console.updateUI();
    }
}