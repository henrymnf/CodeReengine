import javax.swing.JButton;
import java.awt.Color;

public class RedButton {
    public static JButton createRedButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(Color.RED);
        return button;
    }
}