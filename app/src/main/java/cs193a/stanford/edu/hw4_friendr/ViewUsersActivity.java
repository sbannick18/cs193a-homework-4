package cs193a.stanford.edu.hw4_friendr;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.*;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.squareup.picasso.Picasso;

import stanford.androidlib.*;

public class ViewUsersActivity extends SimpleActivity {
    private static final String WEB_DIRECTORY = "http://www.martystepp.com/friendr/friends/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_users);
    }

    public void fetchUsers(){
        Ion.with(this)
                .load(WEB_DIRECTORY+ "list")
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        String[] lines = result.split("\n");
                        loadUsers(lines);
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchUsers();
    }

    public void loadUsers(String [] lines){
        GridLayout layout = (GridLayout) findViewById(R.id.gridLayout);
        Log.d("Array", lines.toString());
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width;
        if(isPortrait()){
            width = size.x/2;
        } else{
            width = size.x/4;
        }

        for (String line:lines){
            ImageView img = new ImageView(this);
            img.setImageResource(R.drawable.user_test);
            img.setTag(line);
            img.setOnClickListener(new View.OnClickListener() {
                                       public void onClick(View v) {
                                           photoClick(v);
                                       }
                                   }
            );
            layout.addView(img);
            Picasso.with(this)
                    .load(WEB_DIRECTORY + line.toLowerCase() + ".jpg")
                    .error(R.drawable.sadface)
                    .placeholder(R.drawable.loading)
                    .resize(width, width
                    / getScreenWidth() * getScreenHeight())
                    .into(img);
        }

    }
    public void photoClick(View v){
        String name = v.getTag().toString();
        if (isPortrait()) {
            Intent intent = new Intent(this, ProfileActivity.class);
            intent.putExtra("name", name);
            startActivity(intent);
        }
        else {
            ProfileFragment frag = (ProfileFragment) findFragmentById(R.id.frag);
            frag.setUser(name);
        }
    }

}
