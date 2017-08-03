package comindmytroskoryk.linkedin.ua.qrhelper;

/**
 * Created by Dem on 31.07.2017.
 */
public class History {

    public History(String date, String text) {

        this.date = date;
        this.text = text;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    private String date;
    private String text;

}

