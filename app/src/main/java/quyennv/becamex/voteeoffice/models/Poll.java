package quyennv.becamex.voteeoffice.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;


public class Poll {
    @SerializedName("id")
    private  int Id;
    @SerializedName("question")
    private  String Question;
    @SerializedName("status")
    private  String Status;
    @SerializedName("position")
    private  int Position;
    @SerializedName("userCreated")
    private  String UserCreated;
    @SerializedName("avatar")
    private  String Avatar;
    @SerializedName("name")
    private  String Name;
    @SerializedName("dateCreated")
    private String DateCreated;

    @SerializedName("pollPlans")
    private ArrayList<PollPlan> PollPlans;

    @SerializedName("pollUserAssign")
    private ArrayList<PollUserAssign> PollUserAssign;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getQuestion() {
        return Question;
    }

    public void setQuestion(String question) {
        Question = question;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public int getPosition() {
        return Position;
    }

    public void setPosition(int position) {
        Position = position;
    }

    public String getUserCreated() {
        return UserCreated;
    }

    public void setUserCreated(String userCreated) {
        UserCreated = userCreated;
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

    public String getDateCreated() {
        return DateCreated;
    }

    public void setDateCreated(String dateCreated) {
        DateCreated = dateCreated;
    }

    public ArrayList<PollPlan> getPollPlans() {
        return PollPlans;
    }

    public void setPollPlans(ArrayList<PollPlan> pollPlans) {
        PollPlans = pollPlans;
    }

    public ArrayList<quyennv.becamex.voteeoffice.models.PollUserAssign> getPollUserAssign() {
        return PollUserAssign;
    }

    public void setPollUserAssign(ArrayList<quyennv.becamex.voteeoffice.models.PollUserAssign> pollUserAssign) {
        PollUserAssign = pollUserAssign;
    }
}
