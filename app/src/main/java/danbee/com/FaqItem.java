package danbee.com;

public class FaqItem {

    private String userid;
    private String title;
    private String content;
    private String answer="";

    public FaqItem(String userid, String title, String content, String answer){
        this.userid = userid;
        this.title = title;
        this.content=content;
        this.answer = answer;
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

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
