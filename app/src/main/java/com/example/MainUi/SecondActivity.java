package com.example.MainUi;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class SecondActivity extends AppCompatActivity {
    private Bitmap bitmap;
    private TextView text;
    private ImageView imageView;
    private int idx;

    List<Integer> color=new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_second);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("물감을 추출해주세요");

        imageView=(ImageView)findViewById(R.id.imageView2);
        Intent intent=getIntent();
        idx=intent.getExtras().getInt("idx");
        if(idx==-1)
        {
            byte[] arr = getIntent().getByteArrayExtra("img");
            imageView.setImageBitmap(BitmapFactory.decodeByteArray(arr, 0, arr.length));
        }
        else
        {
            imageView.setImageResource(intent.getExtras().getInt("img"));
        }

        Button button = (Button) findViewById(R.id.button5);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v){
                Intent intent = new Intent(getApplicationContext(), Third.class);
                Intent intent1=getIntent();
                int[] colors=new int[color.size()];
                for(int i=0;i<colors.length;i++)colors[i]=color.get(i);
                intent.putExtra("colors",colors);
                intent.putExtra("colorsSize",colors.length);
                if(idx==-1){
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    byte[] byteArray = stream.toByteArray();
                    intent.putExtra("img",byteArray);
                }
                else intent.putExtra("img",intent1.getExtras().getInt("img"));
                intent.putExtra("idx",idx);
                startActivity(intent);
                //finish();
            }
        });
        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
        bitmap = drawable.getBitmap();
        text=(TextView)findViewById(R.id.textView3);
        imageView.getViewTreeObserver()
                .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {

                        //뷰의 생성된 후 크기와 위치 구하기
                        bitmap=Bitmap.createScaledBitmap(bitmap, imageView.getWidth(), imageView.getHeight(), false);


                        //리스너 해제
                        imageView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                });

        imageView.setOnTouchListener( new View.OnTouchListener() {
            float x;
            float y;
            int pixel;
            int R,G,B;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        x=event.getX();
                        Log.d("XX",String.valueOf(x));
                        y=event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        pixel=bitmap.getPixel((int)x,(int)y);
                        R = Color.red(pixel);
                        G = Color.green(pixel);
                        B = Color.blue(pixel);
                        text.setText("R: "+R+" G: "+G+" B: "+B);
                        color.add(Color.rgb(R,G,B));
                        break;
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_MOVE:
                        break;
                }
                return true;
            }
        });


    }

}
