package quyennv.becamex.voteeoffice.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import quyennv.becamex.voteeoffice.R;
import quyennv.becamex.voteeoffice.Settings;
import quyennv.becamex.voteeoffice.adapter.PlanDetailAdapter;
import quyennv.becamex.voteeoffice.adapter.PollUserDialogAdapter;
import quyennv.becamex.voteeoffice.models.Poll;
import quyennv.becamex.voteeoffice.models.PollPlan;
import quyennv.becamex.voteeoffice.models.PollUserAssign;
import quyennv.becamex.voteeoffice.models.UserDialog;
import quyennv.becamex.voteeoffice.remote.IPollService;
import quyennv.becamex.voteeoffice.remote.NetworkClient;
import quyennv.becamex.voteeoffice.ui.CircularImageView;
import quyennv.becamex.voteeoffice.utils.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PollDetailActivity extends AppCompatActivity {
    private Bundle extras;
    private Activity activity;
    private Toolbar mToolBar;
    private Button sendButton;
    Settings settings;
    private ProgressDialog progress;

    private int pollId = 0;
    private ArrayList<EditText> listEditText = new ArrayList<>();
    private IPollService apiService;


    //Plan
    private PlanDetailAdapter adapter;
    private RecyclerView recyclerView;

    //Assign
    LinearLayout linearLayoutUserPlan;
    private PollUserDialogAdapter adapterAssign;
    private  ArrayList<PollUserAssign> pollUserAssigns;
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


        linearLayoutUserPlan = (LinearLayout) findViewById(R.id.userAssign);
        linearLayoutUserPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<UserDialog> userDialogs = new ArrayList<>();
                for (PollUserAssign assign : pollUserAssigns){
                    userDialogs.add(new UserDialog(assign.getAvatar(),assign.getName()));
                }

                adapterAssign = new PollUserDialogAdapter(activity,userDialogs);

                final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle("Danh sách người tham gia");

                // set the custom layout
                final View customLayout = ((Activity)activity).getLayoutInflater().inflate(R.layout.row_poll_user,null);
                //builder.setView(customLayout);
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                builder.setAdapter(adapterAssign,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int item) {
                                dialog.dismiss();
                            }
                        });

                AlertDialog dialog = builder.create();
                dialog.show();

                Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);

                negativeButton.setTextColor(Color.parseColor("#5E215D"));
                negativeButton.setBackgroundColor(Color.parseColor("#FFFFFF"));
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.detail_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle item selection
        switch (item.getItemId()) {
            case R.id.close_poll:
                closePoll();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void  closePoll(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Đóng bình chọn");
        builder.setMessage("Bạn có chắc chắn đóng bình chọn này không?");
        builder.setPositiveButton(R.string.ok,new DialogInterface.OnClickListener() {
            public void onClick(final DialogInterface dialog, int whichButton) {
                progress = new ProgressDialog(activity);
                progress.setMessage(activity.getString(R.string.loading));
                progress.setIndeterminate(false);
                progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progress.show();
                apiService = NetworkClient.getRetrofit(activity).create(IPollService.class);
                apiService.closePoll(pollId).enqueue(new Callback<Boolean>() {
                    @Override
                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                        if (response.isSuccessful()) {
                            dialog.dismiss();
                            progress.dismiss();
                            if(response.body()){

                                Toast.makeText(activity,"Đóng bình chọn thành công",Toast.LENGTH_LONG).show();

                                Intent intent=new Intent();
                                setResult(RESULT_OK,intent);
                                activity.finish();
                            }
                            else{
                                Toast.makeText(activity,"Đóng bình chọn thất bại! Vui lòng thử lại",Toast.LENGTH_LONG).show();
                            }

                        } else {
                            progress.dismiss();
                            dialog.dismiss();
                            Toast.makeText(activity,"Đóng bình chọn thất bại! Vui lòng thử lại",Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Boolean> call, Throwable t) {
                        dialog.dismiss();
                        progress.dismiss();
                       Toast.makeText(activity,"Đóng bình chọn thất bại! Vui lòng thử lại",Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        // Set the negative button
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

        Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);

        // Change the alert dialog buttons text and background color
        positiveButton.setTextColor(Color.parseColor("#5E215D"));
        positiveButton.setBackgroundColor(Color.parseColor("#FFFFFF"));

        negativeButton.setTextColor(Color.parseColor("#5E215D"));
        negativeButton.setBackgroundColor(Color.parseColor("#FFFFFF"));
    }


    public void loadPollDetail(int id){

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

                    //Render avatar assign
                    pollUserAssigns = poll.getPollUserAssign();
                    RenderAvatarUserAssign(pollUserAssigns);


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

    public  void RenderAvatarUserAssign(ArrayList<PollUserAssign> pollUserAssigns){




        if(pollUserAssigns.size()>2){
            CircularImageView imageView = new CircularImageView(activity);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(Utils.dpToPx(30, activity),Utils.dpToPx(30, activity));
            imageView.setLayoutParams(layoutParams);
            Glide.with(activity).load(pollUserAssigns.get(0).getAvatar()).override(Utils.dpToPx(30, activity)).into(imageView);
            linearLayoutUserPlan.addView(imageView);

            TextView textView = new TextView(activity);
            LinearLayout.LayoutParams layoutParamsPlus = new LinearLayout.LayoutParams(Utils.dpToPx(23, activity),Utils.dpToPx(23, activity));
            layoutParamsPlus.setMargins(0, Utils.dpToPx(0, activity), 0, 0);
            textView.setLayoutParams(layoutParamsPlus);
            textView.setText((pollUserAssigns.size()-1+"+"));
            textView.setGravity(Gravity.CENTER);
            textView.setBackground(ContextCompat.getDrawable(activity, R.drawable.cicle_avatar));
            linearLayoutUserPlan.addView(textView);
        }
        else{
            for (PollUserAssign userAssign: pollUserAssigns){
                CircularImageView imageView = new CircularImageView(activity);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(Utils.dpToPx(30, activity),Utils.dpToPx(30, activity));
                imageView.setLayoutParams(layoutParams);
                Glide.with(activity).load(userAssign.getAvatar()).override(Utils.dpToPx(30, activity)).into(imageView);
                linearLayoutUserPlan.addView(imageView);
            }
        }

    }

}