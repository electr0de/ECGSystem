import Models.Patient;
import Models.User;
import org.json.simple.JSONObject;

import javax.swing.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class StartForm {
    public JPanel panel1;
    private JTextField nameTextField;
    private JTextField ageTextField;
    private JButton sendButton;
    private JComboBox sexComboBox;
    private JComboBox docotorComboBox;
    private JComboBox patientComboBox;
    private JButton createButton;

    public StartForm(){


        try {
            List<User> user = DatabaseManager.GetListOfUsers();
            for(User u: user)
                if(u.isDoctor())
                    docotorComboBox.addItem(u.getName());


            List<Patient> patients = DatabaseManager.GetAllPatient();
            for(Patient p: patients)
                patientComboBox.addItem(p);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        sendButton.addActionListener(e -> {


            Patient p = (Patient) patientComboBox.getSelectedItem();

            JSONObject obj = new JSONObject();
            obj.put("id", p.getId());
            obj.put("name", p.getName());
            obj.put("sex", p.getSex());
            obj.put("age", ageTextField.getText());
            obj.put("doctor_name", docotorComboBox.getSelectedItem().toString());
            obj.put("tech_name", Global.user.getName());

            try {
                System.out.println(obj.toJSONString());

                Global.awesomeClient = new AwesomeSockets.AwesomeClientSocket("localhost", 4321);
                Global.awesomeClient.sendMessageLine(obj.toJSONString()); // send message
                System.out.println(Global.awesomeClient.readMessageLine()); // read message
                Global.ChangePanel(new GraphShow().panel1);

            } catch (IOException e1) {
                Global.ShowPopup(panel1, "Error", "Server not responding");
                e1.printStackTrace();
            }

        });

        createButton.addActionListener(e -> Global.ChangePanel(new CreatePatient().panel1));
    }



}
