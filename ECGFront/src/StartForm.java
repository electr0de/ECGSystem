import Models.User;
import org.json.simple.JSONObject;

import javax.swing.*;
import java.awt.event.*;
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

    public StartForm(){
        try {
            List<User> user = DatabaseManager.GetListOfUsers();

            for(User u: user){
                if(u.isDoctor())
                    docotorComboBox.addItem(u.getName());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                JSONObject obj = new JSONObject();
                obj.put("name", nameTextField.getText());
                obj.put("age", ageTextField.getText());
                obj.put("sex", sexComboBox.getSelectedItem().toString());
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

            }
        });
    }



}
