package fr.ybonnel.codestory.database.modele;

public class Enonce {
    private int id;
    private String title;
    private String content;

    public Enonce(int id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
        if (content.length() > 3500) {
            this.content = content.substring(0, 3500);
        } else {
            this.content = content;
        }
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }
}
