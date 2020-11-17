package quyennv.becamex.voteeoffice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import quyennv.becamex.voteeoffice.views.PollActivity;

public class MainActivity extends AppCompatActivity {

    private Toolbar mToolBar;
    //private Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolBar =(Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(mToolBar);

        //button = findViewById(R.id.btn_Vote);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    public void rediactToVote(View view){
       startActivity(PollActivity.getIntent(this));
    }
}