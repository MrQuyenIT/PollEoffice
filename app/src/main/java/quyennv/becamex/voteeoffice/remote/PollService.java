package quyennv.becamex.voteeoffice.remote;

import java.util.List;

import io.reactivex.Single;
import quyennv.becamex.voteeoffice.models.PaginationResult;
import quyennv.becamex.voteeoffice.models.Poll;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class PollService {
    public  static  final  String BASE_URL = "http://api.becamex.com.vn:8002";
    private IPollService api;
    public PollService(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        api = retrofit.create(IPollService.class);
    }
    public Call<PaginationResult<Poll>> getPollsPagination(int page,int pageSize) {
        return api.getPollsPagination(page,pageSize );
    }

    public Call<List<Poll>> getPolls() {
        return api.getPolls();
    }
    public Call<Boolean> addPoll(Poll poll) { return api.addPoll(poll);}
}
