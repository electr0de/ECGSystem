import javax.swing.*;

public class Options {
    private JButton startButton;
    public JPanel panel1;
    private JButton reviewReportButton;
    private JButton logoutButton;
    private JLabel userName;


    public Options() {

        if(Global.user.isDoctor())
            startButton.setVisible(false);

        userName.setText("Welcome, "+Global.user.getName());

        startButton.addActionListener(e -> Global.ChangePanel(new StartForm().panel1));

        logoutButton.addActionListener(e -> {

            Global.user = null;
            Global.ChangePanel(new LoginPage().panel1);

        });

        reviewReportButton.addActionListener(e -> Global.ChangePanel(new ReviewReport().panel1));
    }
}
