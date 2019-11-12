import Models.Patient;
import Models.Report;
import Models.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {


    private static Connection Connect() throws SQLException {
        return DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/ecg?serverTimezone=IST","root","");
    }


    public static User GetUser(String username) throws SQLException {

        Connection conn = Connect();
        PreparedStatement stmt=conn.prepareStatement("SELECT * FROM user WHERE name = ?");

        stmt.setString(1, username);
        ResultSet rs = stmt.executeQuery();

        if (!rs.isBeforeFirst() ) {
            return null;
        }

        rs.next();
        return new User(
                rs.getString("name"),
                null,
                rs.getString("pass_enc"),
                rs.getBoolean("is_doctor")
        );

    }

    public static List<User> GetListOfUsers() throws SQLException {
        Connection conn = Connect();
        PreparedStatement stmt=conn.prepareStatement("SELECT * FROM user");

        ResultSet rs = stmt.executeQuery();

        List<User> users = new ArrayList<>();

        while(rs.next()) {
            users.add(new User(
                    rs.getString("name"),
                    null,
                    rs.getString("pass_enc"),
                    rs.getBoolean("is_doctor")
            ));
        }

        return users;
    }


    public static List<Report> GetAllReports() throws SQLException{
        Connection conn = Connect();
        PreparedStatement stmt=conn.prepareStatement("" +
                "SELECT report.id as report_id,patient.id as patient_id,name,sex,patient_age,doctor_name,doctor_name,tech_name,raw_data,report,verfied_by_tech,verfied_by_doctor FROM patient INNER JOIN report ON patient_id = patient.id");

        ResultSet rs = stmt.executeQuery();

        List<Report> reports = new ArrayList<>();

        while(rs.next()) {
            reports.add(new Report(
                    rs.getInt("report_id"),
                    rs.getString("name"),
                    rs.getBlob("report"),
                    rs.getBoolean("verfied_by_tech"),
                    rs.getBoolean("verfied_by_doctor")
            ));
        }

        return reports;
    }

    public static void VerifyReport(Report report, int verifyLevel) throws SQLException {

        Connection conn = Connect();
        PreparedStatement stmt = conn.prepareStatement("UPDATE report SET verfied_by_tech = ?, verfied_by_doctor = ? WHERE id = ?");

        report.setVerifiedByTech(verifyLevel > 0);
        report.setVerifiedByDoctor(verifyLevel > 1);

        stmt.setBoolean(1, report.isVerifiedByTech());
        stmt.setBoolean(2, report.isVerifiedByDoctor());
        stmt.setInt(3, report.getId());
        stmt.executeUpdate();

    }

    public static void CreatePatient(Patient patient) throws SQLException {
        Connection conn = Connect();
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO patient VALUES (NULL, ?, ?)");

        stmt.setString(1, patient.getName());
        stmt.setString(2, patient.getSex());
        stmt.executeUpdate();
    }

    public static List<Patient> GetAllPatient() throws SQLException {
        Connection conn = Connect();
        PreparedStatement stmt=conn.prepareStatement("SELECT * FROM patient");

        ResultSet rs = stmt.executeQuery();

        List<Patient> patients = new ArrayList<>();

        while(rs.next()) {
            patients.add(new Patient(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("sex")
            ));
        }

        return patients;
    }

}
