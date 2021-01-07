package com.example.MainUi;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

public class Player extends YouTubeBaseActivity {
    private YouTubePlayerView youtube;
    //private Button bt;
    private YouTubePlayer.OnInitializedListener listener;
    private int idx;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        //bt=(Button)findViewById(R.id.button6);
        youtube=(YouTubePlayerView)findViewById(R.id.youtubeView);
        Intent intent=getIntent();
        idx=intent.getExtras().getInt("idx");
        Log.d("idx",String.valueOf(idx));
        listener=new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {

                switch(idx)
                {
                    case 0:
                        youTubePlayer.loadVideo("XHMdIA6bEOE");
                        break;
                    case 1:
                        youTubePlayer.loadVideo("kfKI6rbkQZo");
                        break;
                    case 2:
                        youTubePlayer.loadVideo("E0W5sJZ2d64");
                        break;
                    case 3:
                        youTubePlayer.loadVideo("XhmUL56gbRw");
                        break;
                }

            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {


            }
        };
        youtube.initialize("AIzaSyCOHT0rid3jNa2pIhqd3FImKIf2h_OKkh0", listener);
        /*bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                youtube.initialize("AIzaSyCOHT0rid3jNa2pIhqd3FImKIf2h_OKkh0", listener);
            }
        });*/
    }
}