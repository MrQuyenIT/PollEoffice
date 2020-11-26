package quyennv.becamex.voteeoffice.remote;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import quyennv.becamex.voteeoffice.models.ContactChip;
import quyennv.becamex.voteeoffice.models.ObjectSearch;
import quyennv.becamex.voteeoffice.models.PaginationResult;
import quyennv.becamex.voteeoffice.models.Poll;
import quyennv.becamex.voteeoffice.models.PollPlan;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface IPollService {
    @GET("/api/Poll/GetPolls")
    Call<List<Poll>> getPolls();
    @GET("/api/Poll/GetPollsPagination")
    Call<PaginationResult<Poll>> getPollsPagination(@Query("page") int page, @Query("pageSize") int pageSize);
    @GET("/api/Poll/GetPollsPaginationByUser")
    Call<PaginationResult<Poll>> getPollsPaginationByUser(@Query("page") int page, @Query("pageSize") int pageSize,@Query("username") String username);
    @GET("/api/Poll/FindPollById")
    Call<Poll> findById(@Query("id") int id);
    @GET("/api/Poll/FindPollByIdUser")
    Call<Poll> findPollByIdUser(@Query("id") int id,@Query("userVote")String userVote);
    @POST("/api/Poll/AddPoll")
    Call<Boolean> addPoll(@Body Poll  poll);
    @PUT("/api/Poll/UpdatePoll")
    Call<Boolean> updatePoll(@Body Poll  poll);
    @GET("/api/Poll/ClosePoll")
    Call<Boolean> closePoll(@Query("id") int id);
    @DELETE("/api/Poll/DeletePoll")
    Call<Boolean> deletePoll(@Query("id") int id);

    @POST("/api/PollUserPlan/AddRangePollUserPlan")
    Call<Boolean> addRangePollUserPlan(@Query("userPoll") String userPoll,@Query("pollId") int pollId, @Body List<PollPlan>  plans);

    @GET("/api/PersonalProfile/GetAllPersonals")
    Call<List<ObjectSearch>>GetAllPersonals();

    @GET("/api/ContactChip/GetAllUser")
    Call<List<ContactChip>>GetAllUser();
}
