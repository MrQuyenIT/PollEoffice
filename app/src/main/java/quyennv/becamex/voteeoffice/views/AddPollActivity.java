package quyennv.becamex.voteeoffice.views;

import androidx.annotation.Nullable;
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
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.common.base.Strings;
import com.tylersuehr.chips.Chip;
import com.tylersuehr.chips.ChipDataSource;
import com.tylersuehr.chips.ChipsInputLayout;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import quyennv.becamex.voteeoffice.GlideRenderer;
import quyennv.becamex.voteeoffice.R;
import quyennv.becamex.voteeoffice.Settings;
import quyennv.becamex.voteeoffice.adapter.PlanDetailAdapter;
import quyennv.becamex.voteeoffice.adapter.PollInviteAdapter;
import quyennv.becamex.voteeoffice.adapter.PollPlanAddAdapter;
import quyennv.becamex.voteeoffice.enums.Action;
import quyennv.becamex.voteeoffice.models.ContactChip;
import quyennv.becamex.voteeoffice.models.ObjectSearch;
import quyennv.becamex.voteeoffice.models.Poll;
import quyennv.becamex.voteeoffice.models.PollPlan;
import quyennv.becamex.voteeoffice.models.PollUserAssign;
import quyennv.becamex.voteeoffice.remote.IPollService;
import quyennv.becamex.voteeoffice.remote.NetworkClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.google.android.material.textfield.TextInputLayout.END_ICON_CLEAR_TEXT;

public class AddPollActivity extends AppCompatActivity implements ChipDataSource.SelectionObserver{
    private IPollService apiService;
    private Activity activity;
    private Toolbar mToolBar;
    Settings settings;
    private ProgressDialog progress;
    Intent intent;
    private ProgressBar progressBar;
    ChipsInputLayout chipsInputTo;
    Action action;
    Poll pollEdit = new Poll();
    private ArrayList<EditText> listEditText = new ArrayList<>();
    private ArrayList<PollUserAssign> pollUserAssigns = new ArrayList<>();


    private PollPlanAddAdapter adapter;
    private RecyclerView recyclerView;
    ArrayList<PollPlan> listPollPlan =  new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_poll);
        setTitle("Tạo mới bình chọn");
        //Set tool bar
        mToolBar = (Toolbar) findViewById(R.id.add_toolbar);
        setSupportActionBar(mToolBar);
        activity = this;
        settings = new Settings(activity);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        //Process
        progress = new ProgressDialog(activity);
        progress.setMessage("Đang thêm bình chọn...");
        progress.setIndeterminate(false);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        //Recycle
        LinearLayoutManager llm = new LinearLayoutManager(activity);
        recyclerView = (RecyclerView) findViewById(R.id.recycleViewPlan);
        recyclerView.setLayoutManager(llm);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        //Chip input layout
        chipsInputTo = (ChipsInputLayout) findViewById(R.id.chips_input_layout_to);
        chipsInputTo.setImageRenderer(new GlideRenderer());
        chipsInputTo.addSelectionObserver(this);

        apiService = NetworkClient.getRetrofit(this).create(IPollService.class);

        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        intent = getIntent();
        if (intent.hasExtra("pollId")){
            int pollId = intent.getIntExtra("pollId",0);
            if(pollId>0){
                action= Action.EDIT;

                setTitle("Sửa bình chọn");
                progress.setMessage("Đang tải dữ liệu...");
                progress.show();
                apiService.findById(pollId).enqueue(new Callback<Poll>() {
                    @Override
                    public void onResponse(Call<Poll> call, Response<Poll> response) {
                        if(response.isSuccessful()){
                            pollEdit = response.body();

                            fetchDataFromServer(pollEdit);

                            TextInputEditText editText=findViewById(R.id.question);
                            editText.setText(pollEdit.getQuestion());
                            adapter = new PollPlanAddAdapter(activity,  pollEdit.getPollPlans());
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
            else{
                fetchDataFromServer(new Poll());
                RenderViewPlanNew();
            }
        }
        else{
            RenderViewPlanNew();
        }

    }

    public void RenderViewPlanNew(){
        action= Action.ADD;
        //Render View thêm mới
        for (int i=1;i<=2;i++){
            listPollPlan.add(new PollPlan());
        }
        adapter = new PollPlanAddAdapter(activity, listPollPlan);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.add_vote_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle item selection
        switch (item.getItemId()) {
            case R.id.save:
                Send();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void Send(){

        Log.d("Action",action +"");
        //Thêm mới
        TextInputEditText editText=findViewById(R.id.question);
        String question = editText.getText().toString();

        Poll request = new Poll();
        request.setQuestion(question);
        request.setUserCreated(settings.getUserNameKey());

        ArrayList<PollPlan> listPlan = getPollPlansInRecycleView();

        //Validate form
        if(!validateForm(question,listPlan)){
            Toast.makeText(activity,"Vui lòng nhập câu hỏi và câu trả lời",Toast.LENGTH_LONG).show();
            return ;
        }

        //Validate người mời

        List<ContactChip> chipToList = (List<ContactChip>) chipsInputTo.getSelectedChips();
        for (ContactChip userAssign : chipToList){
            PollUserAssign assign = new PollUserAssign();
             assign.setUserAssign(userAssign.getEmail());
             pollUserAssigns.add(assign);
        }

        if(pollUserAssigns.size()==0){
            Toast.makeText(activity,"Vui lòng mời người dùng vào bình chọn",Toast.LENGTH_LONG).show();
            return ;
        }


        request.setPollPlans(listPlan);
        request.setPollUserAssign(pollUserAssigns);
        progress.show();
        if(action ==Action.ADD){
            apiService.addPoll(request).enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                    if(response.isSuccessful()){
                        Boolean isAdd =   Boolean.parseBoolean(response.body().toString());
                        if(isAdd){
                            Toast.makeText(activity,"Thêm bình chọn thành công",Toast.LENGTH_LONG).show();

                            Intent intent=new Intent();
                            setResult(RESULT_OK,intent);
                            activity.finish();
                        }
                        else{
                            Toast.makeText(activity,"Thêm bình chọn thất bại. Vui lòng thử lại",Toast.LENGTH_LONG).show();
                        }

                        progress.dismiss();
                    }
                    else{
                        Toast.makeText(activity,"Lỗi hệ thống! Xin vui lòng thử lại",Toast.LENGTH_LONG).show();
                        progress.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<Boolean> call, Throwable t) {
                    Log.d("Thêm thất bại",t.toString());
                }
            });
        }
        else{
            request.setId(pollEdit.getId());
            request.setDateCreated(pollEdit.getDateCreated());
            request.setUserCreated(pollEdit.getUserCreated());

            apiService.updatePoll(request).enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                    if(response.isSuccessful()){
                        Boolean isAdd =   Boolean.parseBoolean(response.body().toString());
                        if(isAdd){
                            Toast.makeText(activity,"Sửa bình chọn thành công",Toast.LENGTH_LONG).show();

                            Intent intent=new Intent();
                            setResult(RESULT_OK,intent);
                            activity.finish();
                        }
                        else{
                            Toast.makeText(activity,"Sửa bình chọn thất bại. Vui lòng thử lại",Toast.LENGTH_LONG).show();
                        }

                        progress.dismiss();
                    }
                    else{
                        Toast.makeText(activity,"Lỗi hệ thống! Xin vui lòng thử lại",Toast.LENGTH_LONG).show();
                        progress.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<Boolean> call, Throwable t) {
                    Log.d("Sửa thất bại",t.toString());
                }
            });
        }

    }

    public ArrayList<PollPlan>  getPollPlansInRecycleView(){
        ArrayList<PollPlan> listPlan = new ArrayList<>();
        for (int i = 0, count = recyclerView.getChildCount(); i < count; ++i) {
            CardView cardView = (CardView) recyclerView.getChildAt(i);
            RelativeLayout relativeLayout = (RelativeLayout) cardView.getChildAt(0);
            TextInputLayout layoutTextView = (TextInputLayout) relativeLayout.getChildAt(0);

            if (layoutTextView instanceof TextInputLayout) {
                listPlan.add(new PollPlan(cardView.getId(),0,layoutTextView.getEditText().getText().toString()));
            }
        }

        return  listPlan;
    }

    public  void  invitePoll(View v){
        Intent intent = new Intent(this, PollInviteActivity.class);
        startActivityForResult(intent,0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data!=null && data.hasExtra("listSelected")){

            ArrayList<ContactChip> contactsSelectedList = data.getParcelableArrayListExtra("listSelected");
//            ArrayList<ContactChip> selectedUsers = gson.fromJson(data.getStringExtra("listSelected"),(ArrayList<ContactChip>).getClass());

            chipsInputTo.setSelectedChipList(contactsSelectedList);


//            if (selectedUsers.length>0){
//                pollUserAssigns.clear();
//
//                for (ContactChip obj :selectedUsers)
//                {
//                    //Dữ liệu test
//                    PollUserAssign assign= new PollUserAssign();
//                    assign.setUserAssign(obj.getEmail());
//                    pollUserAssigns.add(assign);
//                }
//            }
        }
        else{
            String t = "";
        }


    }

    private void fetchDataFromServer(final Poll poll) {
        progressBar.setVisibility(View.VISIBLE);
        apiService.GetAllUser().enqueue(new Callback<List<ContactChip>>() {
            @Override
            public void onResponse(Call<List<ContactChip>> call, Response<List<ContactChip>> response) {
                if (response.isSuccessful()) {
                    List<ContactChip> contactList = response.body();

                    chipsInputTo.setFilterableChipList(contactList);



                    if(action ==Action.EDIT){
                        List<PollUserAssign> userAssign = poll.getPollUserAssign();
                        List<ContactChip> contactChips = new ArrayList<>();
                        if(userAssign!=null){
                            for (PollUserAssign assign : userAssign){
                                ContactChip contactChip = new ContactChip();
                                contactChip.setEmail(assign.getUserAssign());
                                contactChips.add(contactChip);
                                contactChip.setAvatar(assign.getAvatar());
                                contactChip.setName(assign.getName());
                            }
                            chipsInputTo.setSelectedChipList(contactChips);
                        }

                    }

                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<List<ContactChip>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }


    public  Boolean  validateForm(String question, ArrayList<PollPlan> pollPlans){
        Boolean flag= true;
        if(Strings.isNullOrEmpty(question)){
            return false;
        }
        for (PollPlan pollPlan: pollPlans){
            if(Strings.isNullOrEmpty(pollPlan.getPlanName())){
                flag =false;
                break;
            }
        }

        return  flag;
    }

    public void addPlan(View v) {
        ArrayList<PollPlan> listPlan = getPollPlansInRecycleView();
        listPlan.add(new PollPlan());
        adapter.addPlan(listPlan);
    }

    @Override
    public void onChipSelected(Chip chip) {

    }

    @Override
    public void onChipDeselected(Chip chip) {

    }
}