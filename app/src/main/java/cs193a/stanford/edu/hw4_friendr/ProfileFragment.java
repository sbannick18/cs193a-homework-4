package cs193a.stanford.edu.hw4_friendr;

import android.content.*;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.*;
import android.widget.*;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import stanford.androidlib.*;
@AutoSaveFields
public class ProfileFragment extends SimpleFragment {
    private static final String WEB_DIRECTORY = "http://www.martystepp.com/friendr/friends/";
    HashSet<String> likedUsers;
    HashSet<String> dislikedUsers;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        SharedPreferences prefs = getSimpleActivity().getSharedPreferences("userPrefs", Context.MODE_PRIVATE);
        likedUsers = (HashSet<String>) prefs.getStringSet("likedUsers", new HashSet<String>());
        dislikedUsers = (HashSet<String>) prefs.getStringSet("dislikedUsers", new HashSet<String>());
        String username = getSimpleActivity().getStringExtra("name", "Joey");
        setUser(username);
        Button dislikeButton = (Button) getActivity().findViewById(R.id.dislikeButton);
        dislikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                disLike();
            }
        });
        Button likeButton = (Button) getActivity().findViewById(R.id.likeButton);
        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                like();
            }
        });


    }
    public void setUser(String username){
        String filename = WEB_DIRECTORY + username.toLowerCase() + ".jpg";
        TextView nameField = (TextView) getActivity().findViewById(R.id.name);
        ImageView userPic = (ImageView) getActivity().findViewById(R.id.userPic);
        nameField.setText(username);
        if (likedUsers.contains(username)){
            nameField.setTextColor(0xAA00FF00);
        } else if(dislikedUsers.contains(username)){
            nameField.setTextColor(0xAAFF0000);
        }
        else{ nameField.setTextColor(0xAA000000);}
        int width;
        if(getSimpleActivity().isPortrait()){
            width = getSimpleActivity().getScreenWidth()/2;
        } else { width = getSimpleActivity().getScreenWidth()/4; }
        Picasso.with(getActivity())
                .load(filename)
                .error(R.drawable.sadface)
                .placeholder(R.drawable.loading)
                .resize(width, width/ getSimpleActivity().getScreenWidth() * getSimpleActivity().getScreenHeight())
                .into(userPic);
    }

    public void disLike(){
        TextView nameField = (TextView) getActivity().findViewById(R.id.name);
        String username = nameField.getText().toString();
        CharSequence text= "You disliked " + username;
        Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
        nameField.setTextColor(0xAAFF0000);
        if(likedUsers.contains(username)){
            likedUsers.remove(username);
        }
        if (!dislikedUsers.contains(username)){
            dislikedUsers.add(username);
        }
    }

    public void like(){
        TextView nameField = (TextView) getActivity().findViewById(R.id.name);
        String username = nameField.getText().toString();
        CharSequence text= "You liked " + username;
        Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
        nameField.setTextColor(0xAA00FF00);
        if(dislikedUsers.contains(username)){
            dislikedUsers.remove(username);
        }
        if (!likedUsers.contains(username)){
            likedUsers.add(username);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        SharedPreferences prefs = getSimpleActivity().getSharedPreferences("userPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putStringSet("likedUsers", likedUsers);
        prefsEditor.putStringSet("dislikedUsers", dislikedUsers);
        prefsEditor.apply();
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences prefs = getSimpleActivity().getSharedPreferences("userPrefs", Context.MODE_PRIVATE);
        likedUsers = (HashSet<String>) prefs.getStringSet("likedUsers", new HashSet<String>());
        dislikedUsers = (HashSet<String>) prefs.getStringSet("dislikedUsers", new HashSet<String>());
    }
}
