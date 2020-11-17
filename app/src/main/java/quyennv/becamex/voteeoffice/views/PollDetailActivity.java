package quyennv.becamex.voteeoffice.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

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

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import quyennv.becamex.voteeoffice.R;
import quyennv.becamex.voteeoffice.Settings;
import quyennv.becamex.voteeoffice.models.Poll;
import quyennv.becamex.voteeoffice.models.PollPlan;
import quyennv.becamex.voteeoffice.remote.IPollService;
import quyennv.becamex.voteeoffice.remote.NetworkClient;
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
    private int numberOfLines = 0;
    private ArrayList<EditText> listEditText = new ArrayList<>();
    private IPollService apiService;
    private List<PollPlan> pollPlansCheck = new ArrayList<>();

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

        loadPollDetail(pollId);
        sendButton = (Button) findViewById(R.id.sendButton);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progress.show();
                apiService.addRangePollUserPlan(settings.getUserNameKey(),pollId,pollPlansCheck).enqueue(new Callback<Boolean>() {
                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                        if(response.isSuccessful()){
                            Toast.makeText(activity,"Bình chọn thành công",Toast.LENGTH_LONG).show();
                            loadPollDetail(pollId);
                            pollPlansCheck.clear();
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
                    TextInputEditText editText=findViewById(R.id.question);
                    editText.setText(poll.getQuestion());

                    for (PollPlan plan : poll.getPollPlans())
                    {
                        if(plan.getCheck()){
                            pollPlansCheck.add(plan);
                        }
                        RenderCheckboxPlan(plan);
                    }

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


    public  void  RenderCheckboxPlan(final PollPlan poll){
        //CardView
        CardView cardView = new CardView(this);
        CardView.LayoutParams pramsCardView= new CardView.LayoutParams(CardView.LayoutParams.MATCH_PARENT,CardView.LayoutParams.WRAP_CONTENT);
        pramsCardView.setMargins(60,40,60,40);
        cardView.setLayoutParams(pramsCardView);

        //LinearLayout
        LinearLayout linearLayoutCardView = new LinearLayout(this);
        LinearLayout.LayoutParams pramsLinearLayoutCardView= new LinearLayout.LayoutParams( LinearLayout.LayoutParams.MATCH_PARENT,150);
        linearLayoutCardView.setLayoutParams(pramsLinearLayoutCardView);
        //RelativeLayout
        RelativeLayout relativeLayout = new RelativeLayout(this);
        RelativeLayout.LayoutParams paramsRelativeLayout =  new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        relativeLayout.setLayoutParams(paramsRelativeLayout);
        //Checkbox
        final CheckBox checkBox = new CheckBox(this);
        RelativeLayout.LayoutParams pramsCheckBox= new RelativeLayout.LayoutParams( RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        pramsCheckBox.addRule(RelativeLayout.ALIGN_PARENT_START, RelativeLayout.TRUE);
        checkBox.setLayoutParams(pramsCheckBox);
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkBox.isChecked()) {
                    pollPlansCheck.add(poll);
                }else{
                    pollPlansCheck.remove(poll);
                }
            }
        });
        checkBox.setChecked(poll.getCheck());
        //checkBox.setText(poll.getPlanName());


        // add edittext
        TextView tvContent = new TextView(this);
        tvContent.setText(poll.getPlanName());
        tvContent.setTextColor(Color.BLACK);
        tvContent.setGravity(Gravity.CENTER);
        RelativeLayout.LayoutParams pramsTextViewContent= new RelativeLayout.LayoutParams( RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        tvContent.setLayoutParams(pramsTextViewContent);

        // add edittext User
        TextView tvUser = new TextView(this);
        tvUser.setText(poll.getCountUserVote() + " người");
        tvUser.setTextColor(Color.BLACK);
        tvUser.setGravity(Gravity.CENTER);
        RelativeLayout.LayoutParams pramsTextViewUser= new RelativeLayout.LayoutParams( RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        pramsTextViewUser.addRule(RelativeLayout.ALIGN_PARENT_END, RelativeLayout.TRUE);
        pramsTextViewUser.setMargins(0,0,30,0);
        tvUser.setTextColor(Color.parseColor("#ae9450"));
        tvUser.setLayoutParams(pramsTextViewUser);

        relativeLayout.addView(checkBox);
        relativeLayout.addView(tvContent);
        relativeLayout.addView(tvUser);

        linearLayoutCardView.addView(relativeLayout);
        cardView.addView(linearLayoutCardView);
        linearLayoutParent.addView(cardView);
        numberOfLines++;
    }


    public static Intent getIntent(Context context) {
        return new Intent(context, PollDetailActivity.class);
    }
}