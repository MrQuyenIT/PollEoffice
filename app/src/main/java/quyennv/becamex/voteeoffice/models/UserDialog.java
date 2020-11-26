package quyennv.becamex.voteeoffice.models;

import com.google.gson.annotations.SerializedName;

public class UserDialog {
    private String Avatar;
    private String Name;
    public UserDialog() {
    }
    public UserDialog(String avatar, String name) {
        Avatar = avatar;
        Name = name;
    }



    public String getAvatar() {
        return Avatar;
    }

    public void setAvatar(String avatar) {
        Avatar = avatar;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
