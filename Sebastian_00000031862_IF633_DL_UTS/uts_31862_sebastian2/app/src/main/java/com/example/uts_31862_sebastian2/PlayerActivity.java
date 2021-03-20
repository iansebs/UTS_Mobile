package com.example.uts_31862_sebastian2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class PlayerActivity extends AppCompatActivity {

    Button btnplay, nextbtn, prevbtn;
    TextView songnametext, startsongtext, stopsongtext;
    SeekBar seekmusic;
    String songnames;

    public  static final String EXTRA_NAME = "song_name";
    static MediaPlayer mediaPlayer;
    int position;
    ArrayList<File> songsMine;
    Thread updateseekbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        getSupportActionBar().setTitle("Now Playing");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        prevbtn = findViewById(R.id.prevbtn);
        nextbtn = findViewById(R.id.nextbtn);
        btnplay = findViewById(R.id.playbtn);
        songnametext = findViewById(R.id.songnametext);
        startsongtext = findViewById(R.id.startsongtext);
        stopsongtext = findViewById(R.id.stopsongtext);
        seekmusic = findViewById(R.id.seekbar);

        if (mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();
        }

        Intent i = getIntent();
        Bundle bundle = i.getExtras();

        songsMine = (ArrayList) bundle.getParcelableArrayList("songs");
        String songName = i.getStringExtra("songname");
        position = bundle.getInt("pos",0);
        songnametext.setSelected(true);
        Uri uri = Uri.parse(songsMine.get(position).toString());
        songnames = songsMine.get(position).getName();
        songnametext.setText(songnames);

        mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
        mediaPlayer.start();

        updateseekbar = new Thread(){
            @Override
            public void run() {
                int totalDuration = mediaPlayer.getDuration();
                int currentposition = 0;
                while(currentposition<totalDuration){
                    try{
                        sleep(500);
                        currentposition = mediaPlayer.getCurrentPosition();
                        seekmusic.setProgress(currentposition);
                    }
                    catch (InterruptedException | IllegalStateException e){
                        e.printStackTrace();
                    }
                }
            }
        };
        seekmusic.setMax(mediaPlayer.getDuration());
        updateseekbar.start();


        seekmusic.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });

        String endTime = createTime(mediaPlayer.getDuration());
        stopsongtext.setText(endTime);

        final Handler handler = new Handler();
        final int delay = 1000;

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                String currentTime = createTime(mediaPlayer.getCurrentPosition());
                startsongtext.setText(currentTime);
                handler.postDelayed(this, delay);
            }
        }, delay);

        btnplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()){
                    btnplay.setBackgroundResource(R.drawable.ic_play);
                    mediaPlayer.pause();
                }
                else{
                    btnplay.setBackgroundResource(R.drawable.ic_pause);
                    mediaPlayer.start();
                }
            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                nextbtn.performClick();
            }
        });

        nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.release();
                position = ((position+1)%songsMine.size());
                Uri u = Uri.parse(songsMine.get(position).toString());
                mediaPlayer = MediaPlayer.create(getApplicationContext(), u);
                songnames = songsMine.get(position).getName();
                songnametext.setText(songnames);
                mediaPlayer.start();
                btnplay.setBackgroundResource(R.drawable.ic_pause);

            }
        });

        prevbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.release();
                position = ((position-1)<0)?(songsMine.size()-1):(position-1);

                Uri u = Uri.parse(songsMine.get(position).toString());
                mediaPlayer = MediaPlayer.create(getApplicationContext(), u);
                songnames = songsMine.get(position).getName();
                songnametext.setText(songnames);
                mediaPlayer.start();
                btnplay.setBackgroundResource(R.drawable.ic_pause);
            }
        });
    }

    public String createTime(int duration){
        String time = "";
        int min = duration/1000/60;
        int sec = duration/1000%60;

        time+=min+":";
        if(sec<10){
            time+="0";
        }
        time+=sec;
        return time;
    }
}