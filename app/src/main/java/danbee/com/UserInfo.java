package danbee.com;

public class UserInfo {
    public static final UserInfo info = new UserInfo();
    private boolean loginState = false;
    private String userid;
    private String phone;
    private String name;
    private int gender;
    private String birth;
    private int kickid = -1;

    public int getKickid() {
        return kickid;
    }

    public void setKickid(int kickid) {
        this.kickid = kickid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public boolean isLoginState() {
        return loginState;
    }

    public void setLoginState(boolean loginState) {
        this.loginState = loginState;
    }

}
