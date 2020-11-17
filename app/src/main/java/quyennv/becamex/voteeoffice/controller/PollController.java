package quyennv.becamex.voteeoffice.controller;

import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import quyennv.becamex.voteeoffice.models.PaginationResult;
import quyennv.becamex.voteeoffice.models.Poll;
import quyennv.becamex.voteeoffice.remote.PollService;
import quyennv.becamex.voteeoffice.views.AddPollActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PollController {
    private AddPollActivity view;
    private PollService service;
    public PollController(AddPollActivity view){
        this.view = view;
        service = new PollService();
    }

    public void getPollsPagination(int page,int pageSize){
        service.getPollsPagination(page,pageSize).enqueue(new Callback<PaginationResult<Poll>>() {
            @Override
            public void onResponse(Call<PaginationResult<Poll>> call, Response<PaginationResult<Poll>> response) {
                if(response.isSuccessful()){
                    Gson gson = new Gson();
                    String json = gson.toJson(response.body());
                    Log.d("GetPallpagination",json);
                }
            }

            @Override
            public void onFailure(Call<PaginationResult<Poll>> call, Throwable t) {
                Toast.makeText(view,"Tải dữ liệu thất bại. Vui lòng thử lại",Toast.LENGTH_LONG).show();
            }
        });
    }

    public void   addPoll(Poll poll) {
        service.addPoll(poll).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if(response.isSuccessful()){
                    Gson gson = new Gson();
                    String json = gson.toJson(response.body());
                    Log.d("AddPoll",json);

                    Boolean isAdd =   Boolean.parseBoolean(response.body().toString());
                    if(isAdd){
                        Toast.makeText(view,"Thêm bình chọn thành công",Toast.LENGTH_LONG).show();
                        view.finish();
                    }
                    else{
                        Toast.makeText(view,"Thêm bình chọn thất bại. Vui lòng thử lại",Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                    Log.d("Faild",t.toString());
            }
        });
    }
}
