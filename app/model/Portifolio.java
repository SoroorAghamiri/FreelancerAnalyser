package model;

public class Portifolio {
    String title;
    String Desc;

    public Portifolio() {
    }

    public Portifolio(String title, String desc) {
        this.title = title;
        Desc = desc;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return Desc;
    }

    public void setDesc(String desc) {
        Desc = desc;
    }
}
