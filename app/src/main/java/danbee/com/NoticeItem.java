package danbee.com;

public class NoticeItem {
    private String title;
    private String content;
    private String date;

    public NoticeItem(String t, String c, String d){
        this.title = t;
        this.content = c;
        this.date = d;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
