package cs193a.stanford.edu.hw4_friendr;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import stanford.androidlib.SimpleActivity;

public class FriendrMainActivity extends SimpleActivity {
    public static final String BASE_URL = "http://www.martystepp.com/friendr/friends/";
    public static final String TEXT_URL = BASE_URL + "list";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friendr_main);
    }

    public void viewUsers(View view) {
        Intent intent = new Intent(this, ViewUsersActivity.class);
        startActivity(intent);
    }

}
