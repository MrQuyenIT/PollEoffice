package quyennv.becamex.voteeoffice.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TabHost;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import quyennv.becamex.voteeoffice.MyDividerItemDecoration;
import quyennv.becamex.voteeoffice.R;
import quyennv.becamex.voteeoffice.adapter.PollInviteAdapter;
import quyennv.becamex.voteeoffice.models.ContactChip;
import quyennv.becamex.voteeoffice.models.ObjectSearch;
import quyennv.becamex.voteeoffice.remote.IPollService;
import quyennv.becamex.voteeoffice.remote.NetworkClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tellh.com.recyclertreeview_lib.TreeNode;
import tellh.com.recyclertreeview_lib.TreeViewAdapter;

public class PollInviteActivity extends AppCompatActivity implements PollInviteAdapter.PollInviteSearchAdapterListener{
    private Toolbar mToolBar;

    IPollService apiService;
    ProgressBar userProgressBar;
    EditText editTextSearch;
    private RecyclerView usersRecyclerView;
    private List<ObjectSearch> contactList;
    private PollInviteAdapter mAdapter;
    private List<ObjectSearch> userSelectedList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poll_invite);
        setTitle("Mời bình chọn");
        //Set tool bar
        mToolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolBar);
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        apiService = NetworkClient.getRetrofit(this).create(IPollService.class);

        usersRecyclerView = findViewById(R.id.recycler_view_user);
        contactList = new ArrayList<>();
        mAdapter = new PollInviteAdapter(this, contactList, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        usersRecyclerView.setLayoutManager(mLayoutManager);
        usersRecyclerView.setItemAnimator(new DefaultItemAnimator());
        usersRecyclerView.addItemDecoration(new MyDividerItemDecoration(this, DividerItemDecoration.VERTICAL, 36));
        usersRecyclerView.setAdapter(mAdapter);

        //Edit text search
        editTextSearch = findViewById(R.id.edt_search);
        userProgressBar = findViewById(R.id.user_progressBar);

        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mAdapter.getFilter().filter(s);
            }
        });

        fetchContacts();
    }

    private void fetchContacts() {
        userProgressBar.setVisibility(View.VISIBLE);
        apiService.GetAllPersonals().enqueue(new Callback<List<ObjectSearch>>() {
            @Override
            public void onResponse(Call<List<ObjectSearch>> call, Response<List<ObjectSearch>> response) {
                if (response.isSuccessful()) {
                    contactList.clear();
                    contactList.addAll(response.body());
                    mAdapter.notifyDataSetChanged();

                    userProgressBar.setVisibility(View.GONE);

                } else
                    userProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<List<ObjectSearch>> call, Throwable t) {
                userProgressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onContactSelected(ObjectSearch contact) {
        Toast.makeText(getApplicationContext(), "Selected: " + contact.getFullName() + ", " + contact.getDepartment(), Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_user_menu, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_unselected:
                mAdapter.ClearAllSelection();
                return true;
            case R.id.action_select:
                userSelectedList = mAdapter.getSelected();

                Intent intent = new Intent();
                //Gson gson = new Gson();
                ArrayList<ContactChip> chipList = getContactChips(userSelectedList);
                //intent.putExtra("listSelected",gson.toJson(chipList));

                intent.putParcelableArrayListExtra("listSelected", (ArrayList<? extends Parcelable>) chipList);
                setResult(Activity.RESULT_OK, intent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private ArrayList<ContactChip> getContactChips(List<ObjectSearch> userSelectedList) {
        ArrayList<ContactChip> list = new ArrayList<>();
        ContactChip chip;
        //eOfficeContact chipDept;
        for (ObjectSearch user :
                userSelectedList) {
            chip = new ContactChip(user.getAccountID(), 0, user.userName, "", "", user.email, user.avatar);
            list.add(chip);
        }
        return list;
    }

}