import Models.User;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class LoginPage {
    public JPanel panel1;
    private JTextField textField1;
    private JPasswordField passwordField1;
    private JButton loginButton;

    public LoginPage() {
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                    User user= DatabaseManager.GetUser(textField1.getText());

                    if(user == null) {
                        Global.ShowPopup(panel1, "Error", "User doesn't exist.");
                        return;
                    }

                    if(user.isValidPassword(passwordField1.getText())) {
                        Global.user = user;
                        System.out.println("success");

                        Global.ChangePanel(new Options().panel1);
                    }else {
                        Global.ShowPopup(panel1, "Error", "Incorrect password.");
                    }

                } catch (SQLException e1) {
                    e1.printStackTrace();
                    Global.ShowPopup(panel1, "Error", "Unknown Error");
                }

            }
        });
    }

}
