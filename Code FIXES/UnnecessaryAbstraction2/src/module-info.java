import javax.swing.JButton;
import java.awt.Color;

public class BlueButton {
    public static JButton createBlueButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(Color.BLUE);
        return button;
    }
}
