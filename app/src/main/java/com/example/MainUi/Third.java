package com.example.MainUi;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.Arrays;

public class Third extends AppCompatActivity {
    private int[] colors;
    private MyView myv;
    private int myvW,myvH,idx;
    private Bitmap bitmap;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); //가로모드
        setContentView(R.layout.activity_third);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("캐릭터 색칠하기");


        //디바이스 회전시 값 유지를 하기위한 코드
        Resources r = Resources.getSystem();
        Configuration config = r.getConfiguration();
        onConfigurationChanged(config);

        int size;
        myv=(MyView)findViewById(R.id.myView);
        Intent intent=getIntent();
        idx=intent.getExtras().getInt("idx");
        size=intent.getExtras().getInt("colorsSize");
        colors=Arrays.copyOf(intent.getIntArrayExtra("colors"),size);
        Button btn[]=new Button[size];
        if(idx==-1)
        {
            byte[] arr = getIntent().getByteArrayExtra("img");
            bitmap= BitmapFactory.decodeByteArray(arr, 0, arr.length);
            myv.setBitmap(bitmap);
        }
        else myv.setBitmap(intent.getExtras().getInt("img"));
        Button bt2=(Button)findViewById(R.id.button2);
        Button bt3=(Button)findViewById(R.id.button3);
        Button widbt=(Button)findViewById(R.id.widbt);
        Button nextbt=(Button)findViewById(R.id.button4);
        //nextbt.setEnabled(false);
        Button clearbt=(Button)findViewById(R.id.clearbt);
        LinearLayout layout=(LinearLayout)findViewById(R.id.btlayout);
        for(int i=0;i<size;i++)
        {
            btn[i]=new Button(this);
            //btn[i].setText(String.valueOf(i));
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams
                    (changeDP(40),changeDP(40)); //40dp,40dp
            lp.rightMargin=changeDP(15);
            btn[i].setLayoutParams(lp);
            layout.addView(btn[i]);
            GradientDrawable drawable = (GradientDrawable) ContextCompat.getDrawable(this, R.drawable.whitebt);
            drawable.setColor(colors[i]);
            btn[i].setBackground(drawable);
            final int finalI = i;
            btn[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myv.setPathColor(colors[finalI]);
                }
            });
        }
        GradientDrawable drawable = (GradientDrawable) ContextCompat.getDrawable(this, R.drawable.whitebt);
        drawable.setColor(Color.WHITE);
        widbt.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                EditText wid=(EditText)findViewById(R.id.textView2);
                myv.setPaintWidth(Integer.parseInt(wid.getText().toString()));
            }
        });
        bt2.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                myv.setPathColor(Color.WHITE);
            }
        });
        bt3.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                myv.setPathColor(Color.BLACK);
            }
        });
        nextbt.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(), Player.class);
                intent.putExtra("idx",idx);
                startActivity(intent);
                //finish();
            }
        });
        clearbt.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                myv.clear();
            }
        });

        myv.getViewTreeObserver()
                .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {

                        //뷰의 생성된 후 크기와 위치 구하기
                        myvW=myv.getWidth();
                        myvH=myv.getHeight();
                        Log.d("WW",String.valueOf(myvW));
                        Log.d("HH",String.valueOf(myvH));
                        myv.setWH(myvW,myvH);
                        //리스너 해제
                        myv.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                });

    }

    private int changeDP(int value ) {
        Resources r = Resources.getSystem();
        DisplayMetrics displayMetrics = r.getDisplayMetrics();
        int dp = Math.round(value * displayMetrics.density);
        return dp;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {

        View layoutMainView = (View)this.findViewById(R.id.linearManger);

        Log.w("Layout Width - ", String.valueOf(layoutMainView.getWidth()));
        Log.w("Layout Height - ", String.valueOf(layoutMainView.getHeight()));
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        //현재 디바이스의 방향성을 체크...

        switch(newConfig.orientation){
            case Configuration.ORIENTATION_LANDSCAPE:
                Toast.makeText(getApplicationContext(),"가로",Toast.LENGTH_SHORT).show();
                break;
            case Configuration.ORIENTATION_PORTRAIT:
                Toast.makeText(getApplicationContext(),"세로",Toast.LENGTH_SHORT).show();
                return;
        }

    }
}
