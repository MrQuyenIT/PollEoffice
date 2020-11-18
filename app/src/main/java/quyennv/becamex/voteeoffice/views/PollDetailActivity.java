package quyennv.becamex.voteeoffice.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import quyennv.becamex.voteeoffice.R;
import quyennv.becamex.voteeoffice.Settings;
import quyennv.becamex.voteeoffice.adapter.PlanDetailAdapter;
import quyennv.becamex.voteeoffice.adapter.PollAdapter;
import quyennv.becamex.voteeoffice.models.Poll;
import quyennv.becamex.voteeoffice.models.PollPlan;
import quyennv.becamex.voteeoffice.models.PollUserPlan;
import quyennv.becamex.voteeoffice.remote.IPollService;
import quyennv.becamex.voteeoffice.remote.NetworkClient;
import quyennv.becamex.voteeoffice.ui.CircularImageView;
import quyennv.becamex.voteeoffice.utils.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.google.android.material.textfield.TextInputLayout.END_ICON_CLEAR_TEXT;

public class PollDetailActivity extends AppCompatActivity {
    private Bundle extras;
    private Activity activity;
    private Toolbar mToolBar;
    private Button sendButton;
    LinearLayout linearLayoutParent;
    Settings settings;
    private ProgressDialog progress;

    private int pollId = 0;
    private ArrayList<EditText> listEditText = new ArrayList<>();
    private IPollService apiService;


    //Plan
    private PlanDetailAdapter adapter;
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poll_detail);
        setTitle("Chi tiết bình chọn");
        //Set tool bar
        mToolBar = (Toolbar) findViewById(R.id.add_toolbar);
        setSupportActionBar(mToolBar);
        activity = this;
        settings = new Settings(activity);
         linearLayoutParent = (LinearLayout) findViewById(R.id.list_plan);
        apiService = NetworkClient.getRetrofit(this).create(IPollService.class);

        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        extras = getIntent().getExtras();
        pollId = extras.getInt("id");

        //Process
        progress = new ProgressDialog(activity);
        progress.setMessage("Đang tải dữ liệu...");
        progress.setIndeterminate(false);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.show();

        LinearLayoutManager llm = new LinearLayoutManager(activity);
        recyclerView = (RecyclerView) findViewById(R.id.recycleViewPlan);
        recyclerView.setLayoutManager(llm);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        loadPollDetail(pollId);
        sendButton = (Button) findViewById(R.id.sendButton);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                List<PollPlan> pollPlansCheck = adapter.getAllPlanCheck();

                progress.show();
                apiService.addRangePollUserPlan(settings.getUserNameKey(),pollId,pollPlansCheck).enqueue(new Callback<Boolean>() {
                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                        if(response.isSuccessful()){
                            Toast.makeText(activity,"Bình chọn thành công",Toast.LENGTH_LONG).show();
                            Intent intent=new Intent();
                            setResult(RESULT_OK,intent);
                            activity.finish();
                        }
                        else{
                            Toast.makeText(activity,"Bình chọn thất bại! Vui lòng thử lại",Toast.LENGTH_LONG).show();
                        }

                    }
                    @Override
                    public void onFailure(Call<Boolean> call, Throwable t) {
                        Toast.makeText(activity,"Bình chọn thất bại! Vui lòng thử lại",Toast.LENGTH_LONG).show();
                        progress.dismiss();
                    }
                });

            }
        });
    }



    public void loadPollDetail(int id){
        //Xóa hết view cũ;
        linearLayoutParent.removeAllViews();

        apiService.findPollByIdUser(id,settings.getUserNameKey()).enqueue(new Callback<Poll>() {
            @Override
            public void onResponse(Call<Poll> call, Response<Poll> response) {

                if(response.isSuccessful()){
                    Poll poll = response.body();
                    TextView question=findViewById(R.id.question);
                    question.setText(poll.getQuestion());

                    CircularImageView circularImageView = (CircularImageView) findViewById(R.id.avatar);
                    Glide.with(activity).load(poll.getAvatar()).override(Utils.dpToPx(60, activity)).into(circularImageView);

                    TextView  name = findViewById(R.id.nameCreated);
                    name.setText(poll.getName());


                    String dateCreated  =  Utils.ConvertStringDateToString(poll.getDateCreated(), "yyyy-MM-dd'T'HH:mm:ss", "dd/MM/yyyy HH:mm", "UTC");
                    TextView  date = findViewById(R.id.dateCreated);
                    date.setText(dateCreated);

                    //Addapter planm
                    adapter = new PlanDetailAdapter(activity, poll.getPollPlans());
                    recyclerView.setAdapter(adapter);

                }
               else{
                    Toast.makeText(activity,"Không có dữ liệu",Toast.LENGTH_LONG).show();
                }

                if (progress != null)
                    progress.dismiss();
            }

            @Override
            public void onFailure(Call<Poll> call, Throwable t) {
                Toast.makeText(activity,"Lỗi hệ thống! Vui lòng thử lại",Toast.LENGTH_LONG).show();
                if (progress != null)
                    progress.dismiss();
            }
        });
    }

}