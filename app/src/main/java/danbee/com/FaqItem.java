package danbee.com;

public class FaqItem {

    private String userid;
    private String title;
    private String content;

    public FaqItem(String userid, String title, String content){
        this.userid = userid;
        this.title = title;
        this.content=content;
    }

    public String getTitle() {
        return title;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
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

}
