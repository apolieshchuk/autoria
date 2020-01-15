import org.swingplus.JHyperlink;

import javax.swing.*;
import java.awt.*;

public class Console extends JPanel{

    public Console(int width, int height) {
        this.setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
        this.setPreferredSize(new Dimension(width, height));
    }

    public void printLog(String str) {
        this.add(new JLabel(str));
    }

    public void printLog(String str, boolean isLink) {
        if (isLink) this.add(new JHyperlink(str,str.trim()));
    }

}