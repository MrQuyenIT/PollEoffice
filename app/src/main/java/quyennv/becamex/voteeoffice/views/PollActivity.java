package quyennv.becamex.voteeoffice.views;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import quyennv.becamex.voteeoffice.R;
import quyennv.becamex.voteeoffice.Settings;
import quyennv.becamex.voteeoffice.adapter.PollAdapter;
import quyennv.becamex.voteeoffice.commons.PaginationListener;
import quyennv.becamex.voteeoffice.models.PaginationResult;
import quyennv.becamex.voteeoffice.models.Poll;
import quyennv.becamex.voteeoffice.remote.IPollService;
import quyennv.becamex.voteeoffice.remote.NetworkClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PollActivity extends AppCompatActivity {
    private IPollService apiService;
    private RecyclerView recyclerView;
    private PollAdapter adapter;
    SwipeRefreshLayout mSwipeRefreshLayout;
    private ProgressBar progressBar;
    private Toolbar mToolBar;
    private Activity activity;
    Settings settings;

    List<Poll>  listPolls= new ArrayList<>();
    //Page
    private int currentPage =1;
    private boolean isLastPage = false;
    private int totalPage = 10;
    private boolean isLoading = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poll);
        setTitle("Danh sách tham gia bình chọn");
        activity = this;
        settings = new Settings(activity);
        apiService = NetworkClient.getRetrofit(this).create(IPollService.class);
        //Set tool bar
        mToolBar = (Toolbar) findViewById(R.id.list_toolbar);
        setSupportActionBar(mToolBar);
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //Action Button
        FloatingActionButton floatingActionButton = findViewById(R.id.btn_add_vote);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PollActivity.this, AddPollActivity.class);
                startActivityForResult(intent,0);
            }
        });
        //Process bar
        progressBar= findViewById(R.id.progressBar);
        //Swipe refesh layout
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeToRefresh);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                currentPage = 1;
                isLastPage = false;
                adapter.clear();
                loadPolls();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        //Load danh sách bình chọn khi vào trang
        loadPolls();
        LinearLayoutManager llm = new LinearLayoutManager(activity);
        recyclerView = (RecyclerView) findViewById(R.id.recycleViewId);
        recyclerView.setLayoutManager(llm);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new PollAdapter(activity,listPolls);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new PaginationListener(llm) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage++;
                loadPolls();
            }

            @Override
            public boolean isLastPage() {

                return isLastPage;
            }

            @Override
            public boolean isLoading()
            {
                return isLoading;
            }
        });
        //adapter = new VoteAdapter(this, dataFake);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Load init poll
        adapter.clear();
        loadPolls();
    }

    public  void  loadPolls(){
        progressBar.setIndeterminate(true);
        progressBar.setVisibility(View.VISIBLE);

        Log.d("currentPage",""+currentPage);

        apiService.getPollsPaginationByUser(currentPage,20,settings.getUserNameKey()).enqueue(new Callback<PaginationResult<Poll>>() {
            @Override
            public void onResponse(Call<PaginationResult<Poll>> call, Response<PaginationResult<Poll>> response) {
                if(response.isSuccessful()){
                    Gson gson = new Gson();
                    String json = gson.toJson(response.body());

                    Log.d("response",json);

                    PaginationResult<Poll> data = response.body();

                    listPolls =data.getResult();
                    currentPage = data.getCurrentPage();
                    totalPage = data.getTotalPage();
                    if(listPolls.size()==0)
                    {
                        Toast.makeText(activity, activity.getString(R.string.data_notfound), Toast.LENGTH_LONG).show();
                    }
                    if (currentPage != 0)
                        adapter.removeLoading();
                    adapter.addItems(listPolls);
                    adapter.notifyDataSetChanged();
                    mSwipeRefreshLayout.setRefreshing(false);

                    if (currentPage < totalPage && totalPage > 1) {
                        adapter.addLoading();
                    } else {
                        isLastPage = true;
                    }
                    isLoading = false;

                }
                else{
                    Toast.makeText(activity,"Lỗi hệ thống! Xin vui lòng thử lại",Toast.LENGTH_LONG).show();
                }

                progressBar.setIndeterminate(false);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<PaginationResult<Poll>> call, Throwable t) {
                Toast.makeText(activity,"Lỗi hệ thống! Xin vui lòng thử lại",Toast.LENGTH_LONG).show();
                progressBar.setIndeterminate(false);
                progressBar.setVisibility(View.GONE);
            }
        });
    }


    public static Intent getIntent(Context context) {
        return new Intent(context, PollActivity.class);
    }
}