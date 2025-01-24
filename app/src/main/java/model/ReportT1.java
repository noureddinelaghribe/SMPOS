package model;

public class ReportT1 {

    String type;
    String name;
    String date;

    public ReportT1() {}

    public ReportT1(String type, String name, String date) {
        this.type = type;
        this.name = name;
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
