package Models;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class User {

    private String name;
    private String password;
    private String encPassword;
    private boolean isDoctor;

    public User(String name, String password, String encPassword, boolean isDoctor) {
        this.name = name;

        if (password != null)
            this.encPassword = getMd5(password);

        this.encPassword = encPassword;
        this.isDoctor = isDoctor;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getEncPassword() {
        return encPassword;
    }

    public boolean isDoctor() {
        return isDoctor;
    }

    public boolean isValidPassword(String password){
        return getMd5(password).equals(encPassword);
    }

    public static String getMd5(String input)
    {
        try {

            // Static getInstance method is called with hashing MD5
            MessageDigest md = MessageDigest.getInstance("MD5");

            // digest() method is called to calculate message digest
            //  of an input digest() return array of byte
            byte[] messageDigest = md.digest(input.getBytes());

            // Convert byte array into signum representation
            BigInteger no = new BigInteger(1, messageDigest);

            // Convert message digest into hex value
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        }

        // For specifying wrong message digest algorithms
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

}