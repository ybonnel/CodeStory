package fr.ybonnel.codestory.database.modele;

public class Enonce {
    public static final int MAX_CONTENT_SIZE = 3500;
    private int id;
    private String title;
    private String content;

    public Enonce(int id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
        if (content.length() > MAX_CONTENT_SIZE) {
            this.content = content.substring(0, MAX_CONTENT_SIZE);
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
