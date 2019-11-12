import Models.Report;
import org.apache.commons.io.IOUtils;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.sql.SQLException;
import java.util.List;

public class ReviewReport {
    private JButton backButton;
    private JList list1;
    public JPanel panel1;
    private JButton verifyButton;
    private JButton viewReportButton;

    private Report selectedReport;
    DefaultListModel<Report> listModel;

    public ReviewReport() {

        verifyButton.setVisible(false);
        viewReportButton.setVisible(false);

        try {
            List<Report> reports = DatabaseManager.GetAllReports();
            listModel = new DefaultListModel<>();

            list1.setModel(listModel);

            for(Report r: reports)
                if(!Global.user.isDoctor() || (Global.user.isDoctor() && r.isVerifiedByTech()))
                    listModel.addElement(r);


        } catch (SQLException e) {
            e.printStackTrace();
        }

        backButton.addActionListener(e -> {
            Global.ChangePanel(new Options().panel1);
        });

        list1.getSelectionModel().addListSelectionListener(e -> {

            //stops one of two callback
            if(e.getValueIsAdjusting()) {

                selectedReport = (Report) list1.getSelectedValue();

                viewReportButton.setVisible(true);
                verifyButton.setVisible(true);
                verifyButton.setEnabled(VerifyButtonState());

                Global.frame.pack();

                try {
                    InputStream in = selectedReport.getReport().getBinaryStream();
                    OutputStream out = new FileOutputStream(new File("temp.pdf"));
                    IOUtils.copy(in, out);

                } catch (SQLException e1) {
                    e1.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

            }
        });

        verifyButton.addActionListener(e -> {
            try {

                DatabaseManager.VerifyReport(selectedReport, Global.user.isDoctor()?2:1);
                verifyButton.setEnabled(VerifyButtonState());
                list1.setModel(listModel);

            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        });

        viewReportButton.addActionListener(e -> {
            try {
                Desktop.getDesktop().open(new File("temp.pdf"));
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
    }

    private boolean VerifyButtonState()
    {
        return !(Global.user.isDoctor() && selectedReport.isVerifiedByDoctor()) && !(!Global.user.isDoctor() && selectedReport.isVerifiedByTech());
    }

}
