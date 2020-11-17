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
}
