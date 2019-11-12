import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        Global.frame = new JFrame("ECG App");
        Global.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Global.ChangePanel(new LoginPage().panel1);
    }
}