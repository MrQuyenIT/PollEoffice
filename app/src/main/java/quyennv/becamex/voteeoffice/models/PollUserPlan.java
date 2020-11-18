package quyennv.becamex.voteeoffice.models;

import com.google.gson.annotations.SerializedName;

public class PollUserPlan {
    @SerializedName("id")
    private int Id;
    @SerializedName("pollPlanId")
    private int PollPlanId;
    @SerializedName("userVote")
    private String UserVote;
    @SerializedName("voteCreated")
    private String VoteCreated;
    @SerializedName("name")
    private String Name;
    @SerializedName("avatar")
    private String Avatar;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getPollPlanId() {
        return PollPlanId;
    }

    public void setPollPlanId(int pollPlanId) {
        PollPlanId = pollPlanId;
    }

    public String getUserVote() {
        return UserVote;
    }

    public void setUserVote(String userVote) {
        UserVote = userVote;
    }

    public String getVoteCreated() {
        return VoteCreated;
    }

    public void setVoteCreated(String voteCreated) {
        VoteCreated = voteCreated;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getAvatar() {
        return Avatar;
    }

    public void setAvatar(String avatar) {
        Avatar = avatar;
    }
}
