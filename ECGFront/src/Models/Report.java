package Models;

import java.sql.Blob;

public class Report {

    private int id;
    private String patient_name;
    private Blob report;
    private boolean verifiedByTech;
    private boolean verifiedByDoctor;

    public Report(int id, String patient_name, Blob report, boolean verifiedByTech, boolean verifiedByDoctor) {
        this.id = id;
        this.patient_name = patient_name;
        this.report = report;
        this.verifiedByTech = verifiedByTech;
        this.verifiedByDoctor = verifiedByDoctor;
    }


    public int getId() {
        return id;
    }

    public String getPatient_name() {
        return patient_name;
    }

    public Blob getReport() {
        return report;
    }

    public boolean isVerifiedByTech() {
        return verifiedByTech;
    }

    public boolean isVerifiedByDoctor() {
        return verifiedByDoctor;
    }

    public void setVerifiedByTech(boolean verifiedByTech) {
        this.verifiedByTech = verifiedByTech;
    }

    public void setVerifiedByDoctor(boolean verifiedByDoctor) {
        this.verifiedByDoctor = verifiedByDoctor;
    }

    @Override
    public String toString() {
        return String.format("%d. %s [%s]", id, patient_name, verifiedByDoctor ?"Doctor":verifiedByTech?"Tech":"Unverified");
    }
}
