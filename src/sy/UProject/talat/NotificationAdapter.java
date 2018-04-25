package sy.UProject.talat;

public class NotificationAdapter {
    // id String, txt TEXT, dat CHAR(50), type CHAR(2), read CHAR(1), extra CHAR(50)
    private String id, text, date, type, read, extra;

    public NotificationAdapter(String id, String text, String date, String type,
                               String extra) {
        this.id = id;
        this.text = text;
        this.date = date;
        this.type = type;
        this.extra = extra;
        this.read = "0";
    }
    public NotificationAdapter(String id, String date, String type,
                               String extra) {
        this.id = id;
        this.date = date;
        this.type = type;
        this.extra = extra;
        this.text = "";
        this.read = "0";
    }

    public String getId() {
        return id;
    }

    public NotificationAdapter setId(String id) {
        this.id = id;
        return this;
    }

    public String getText() {
        return text;
    }

    public NotificationAdapter setText(String text) {
        this.text = text;
        return this;
    }

    public String getDate() {
        return date;
    }

    public NotificationAdapter setDate(String date) {
        this.date = date;
        return this;
    }

    public String getType() {
        return type;
    }

    public NotificationAdapter setType(String type) {
        this.type = type;
        return this;
    }

    public String getExtra() {
        return extra;
    }

    public NotificationAdapter setExtra(String extra) {
        this.extra = extra;
        return this;
    }

    public String getRead() {
        return read;
    }

    public NotificationAdapter setRead(String read) {
        this.read = read;
        return this;
    }
}
