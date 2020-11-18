package quyennv.becamex.voteeoffice.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class PollPlan {
    @SerializedName("id")
    private int Id;
    @SerializedName("pollId")
    private int PollId;
    @SerializedName("planName")
    private String PlanName;
    @SerializedName("isCheck")
    private Boolean IsCheck;
    @SerializedName("countUserVote")
    private int CountUserVote;

    @SerializedName("pollUserPlans")
    private ArrayList<PollUserPlan>  PollUserPlans;

    public PollPlan(int id, int pollId, String planName) {
        Id = id;
        PollId = pollId;
        PlanName = planName;
    }

    public PollPlan() {
    }


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

    public String getPlanName() {
        return PlanName;
    }

    public void setPlanName(String planName) {
        PlanName = planName;
    }

    public Boolean getCheck() {
        return IsCheck;
    }

    public void setCheck(Boolean check) {
        IsCheck = check;
    }

    public int getCountUserVote() {
        return CountUserVote;
    }

    public void setCountUserVote(int countUserVote) {
        CountUserVote = countUserVote;
    }

    public ArrayList<PollUserPlan> getPollUserPlans() {
        return PollUserPlans;
    }

    public void setPollUserPlans( ArrayList<PollUserPlan> userPlans) {
        PollUserPlans = userPlans;
    }
}
