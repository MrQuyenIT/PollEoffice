package quyennv.becamex.voteeoffice.models;

import com.google.gson.annotations.SerializedName;

public class PollUserAssign {
    @SerializedName("id")
    private  int Id;
    @SerializedName("pollId")
    private  int PollId;
    @SerializedName("userAssign")
    private  String UserAssign;
    @SerializedName("name")
    private  String Name;
    @SerializedName("dateCreated")
    private  String DateCreated;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getPollId() {
        return PollId;
    }

    public void setPollId(int pollId) {
        PollId = pollId;
    }

    public String getUserAssign() {
        return UserAssign;
    }

    public void setUserAssign(String userAssign) {
        UserAssign = userAssign;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDateCreated() {
        return DateCreated;
    }

    public void setDateCreated(String dateCreated) {
        DateCreated = dateCreated;
    }
}
