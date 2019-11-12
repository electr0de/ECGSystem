import Models.User;

import javax.swing.*;
import java.awt.*;

public class Global {
    public static User user = null;
    public static JFrame frame;
    public static AwesomeSockets.AwesomeClientSocket awesomeClient = null;

    public static void ChangePanel(JPanel panel){
        Global.frame.setContentPane(panel);
        Global.frame.pack();
        Global.frame.setVisible(true);
        Global.frame.setLocationRelativeTo(null);
    }


    public static void ShowPopup(Component parent, String title, String message)
    {
        JOptionPane.showMessageDialog(parent, message, title, JOptionPane.INFORMATION_MESSAGE);
    }
}
