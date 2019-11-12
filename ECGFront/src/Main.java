import javax.swing.*;

public class Main {

    public static void main(String[] args) {

        Global.frame = new JFrame("ECG App");
        Global.frame.setContentPane(new LoginPage().panel1);
        Global.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Global.frame.pack();
        Global.frame.setVisible(true);
        Global.frame.setLocationRelativeTo(null);


    }
}