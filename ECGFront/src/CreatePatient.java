import Models.Patient;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class CreatePatient {
    private JButton OKButton;
    private JTextField textField1;
    private JComboBox comboBox1;
    public JPanel panel1;

    public CreatePatient() {
        OKButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    DatabaseManager.CreatePatient(new Patient(textField1.getText(), comboBox1.getSelectedItem().toString()));
                    Global.ChangePanel(new StartForm().panel1);
                } catch (SQLException e1) {
                    Global.ShowPopup(panel1, "Error", "Unknown Error");
                    e1.printStackTrace();
                }
            }
        });
    }
}
