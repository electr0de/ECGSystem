package Models;

public class Patient {

    private int id;
    private String name;
    private String sex;

    public Patient(int id, String name, String sex) {
        this.id = id;
        this.name = name;
        this.sex = sex;
    }

    public Patient(String name, String sex) {
        this.name = name;
        this.sex = sex;
    }

    @Override
    public String toString() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSex() {
        return sex;
    }
}
