package fr.ybonnel.codestory.database.modele;

import java.util.Date;


public class LogMessage {
    private Date date;
    private String type;
    private String message;

    public LogMessage(Date date, String type, String message) {
        this.date = date;
        this.type = type;
        if (message.length() > 450) {
            this.message = message.substring(0, 450);
        } else {
            this.message = message;
        }
    }

    public LogMessage(String type, String message) {
        date = new Date();
        this.type = type;
        if (message.length() > 450) {
            this.message = message.substring(0, 450);
        } else {
            this.message = message;
        }
    }

    public Date getDate() {
        return date;
    }

    public String getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }
}
