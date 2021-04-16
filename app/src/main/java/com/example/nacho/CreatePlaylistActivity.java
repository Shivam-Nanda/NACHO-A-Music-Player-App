package com.example.nacho;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nacho.R;
import com.example.nacho.ui.yourlibrary.YourLibraryFragment;

public class CreatePlaylistActivity extends AppCompatActivity {

    EditText playlistName,playlistDescription;
    TextView createPlaylist;
    private String description;
    private String name;
    String msg;
    public static String deletedName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_playlist);

        initializeViews();

//
//        Log.i("name",name);
//        description=
        createPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Log.i("cl","Clicked");
//                Intent intent=new Intent(getApplicationContext(), YourLibraryFragment.class);
//                intent.putExtra("Name",name);
//                intent.putExtra("Description",description);
//                startActivity(intent);
//                Bundle bundle=new Bundle();
//                bundle.putString("Name",name);
//                bundle.putString("Description",description);
//                Fragment fragment=new YourLibraryFragment();
//                fragment.setArguments(bundle);
//                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentLayout,fragment).addToBackStack(null).commit();
//                getSupportFragmentManager().popBackStackImmediate();

                deletedName=null;
                if (!checkEmpty()) {
                    Toast.makeText(CreatePlaylistActivity.this, msg, Toast.LENGTH_LONG).show();
                } else{
                    name = playlistName.getText().toString();
                    description = playlistDescription.getText().toString();
                    Intent intent = new Intent(getApplicationContext(), SelectSongsActivity.class);
                    intent.putExtra("name", name);
                    Log.i("name", name);
                    intent.putExtra("description", description);
                    finish();
                    startActivity(intent);

            }


//                FragmentTransaction ft = getFragmentManager().beginTransaction();
//                ft.replace(R.id.fragmentLayout,fragment);
//                ft.addToBackStack(null);
//                ft.commit();
            }
        });
    }
//
//    public void createPlaylist(View view){
//
//    }

    private void initializeViews() {
        playlistName=findViewById(R.id.playlistName);
        playlistDescription=findViewById(R.id.playlistDescription);
        createPlaylist=findViewById(R.id.createButton);
    }

    public boolean checkEmpty(){
        if(playlistName.getText().toString().equals("") && playlistDescription.getText().toString().equals("")){
            msg="NAME AND DESCRIPTION CANNOT BE EMPTY";
            return false;
        }
        else if(playlistName.getText().toString().equals("")){
            msg="NAME CANNOT BE EMPTY";
            return false;
        }
        else if(playlistDescription.getText().toString().equals("")){
            msg="DESCRIPTION CANNOT BE EMPTY";
            return false;
        }else{
            return true;
        }
    }
}