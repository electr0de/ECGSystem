import Models.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {


    private static Connection Connect() throws SQLException {
        return DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/ecg?serverTimezone=IST","root","");

//        Statement stmt=con.createStatement();
//
//        ResultSet rs=stmt.executeQuery("select * from emp");
//
//        while(rs.next())
//            System.out.println(rs.getInt(1)+"  "+rs.getString(2)+"  "+rs.getString(3));
//
//        con.close();
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


}
