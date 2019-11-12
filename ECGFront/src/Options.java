import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Options {
    private JButton startButton;
    public JPanel panel1;
    private JButton reviewReportButton;
    private JButton logoutButton;

    public Options() {
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Global.ChangePanel(new StartForm().panel1);
            }
        });
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                Global.ChangePanel(new LoginPage().panel1);
                Global.user = null;

            }
        });
    }
}
