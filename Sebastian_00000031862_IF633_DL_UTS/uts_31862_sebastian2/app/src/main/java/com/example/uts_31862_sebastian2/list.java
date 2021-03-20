package com.example.uts_31862_sebastian2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;

public class list extends AppCompatActivity {
    RecyclerView rec;
    String[] items;
    private final LinkedList<String> mDaftarLagu = new LinkedList<>();
    private RecyclerView mRecyclerView;
    private adapter mAdapter;
    public ArrayList<File> songsMine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        rec = findViewById(R.id.listSongPreview);
        runtimePermission();
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu){
       getMenuInflater().inflate(R.menu.my_menu, menu);
       return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.profilicon:
                Intent intent = new Intent(list.this, profil.class);
                startActivity(intent);
                return true;
            case R.id.logouticon:
                Intent intent1 = new Intent(list.this, login.class);
                startActivity(intent1);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void runtimePermission(){
        Dexter.withContext(this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        displayingSongs();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();}

    public ArrayList<File> findSong (File file){
        ArrayList<File> arrayList = new ArrayList<>();
        File[] files = file.listFiles();
        for(File singlefile: files){
            if(singlefile.isDirectory() && !singlefile.isHidden()){
                arrayList.addAll(findSong(singlefile));
            }
            else{
                if (singlefile.getName().endsWith(".mp3") || singlefile.getName().endsWith(".wav")){
                    arrayList.add(singlefile);
                }
            }
        }
        return arrayList;
    }

    void displayingSongs(){
        songsMine = findSong(Environment.getExternalStorageDirectory());
        items = new String[songsMine.size()];
        for(int i = 0; i<songsMine.size();i++){
            mDaftarLagu.add(songsMine.get(i).getName().toString().replace(".mp3",  "").replace(".wav", ""));

        }

        mRecyclerView =  (RecyclerView) findViewById(R.id.listSongPreview);
        mAdapter = new adapter(this, mDaftarLagu, songsMine);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

}