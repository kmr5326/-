package com.example.MainUi;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private int max,idx=0;
    private List<Integer> imgs;
    private List<String> names;
    private Bitmap galleryBitmap;
    private ImageView imgView;
    private EditText text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); //가로모드
        max =4;
        imgs = new ArrayList<>();
        names=new ArrayList<>(Arrays.asList("짱구","도라에몽","뽀로로","라이언"));

        for (int i = 0; i < max; i++) {
            imgs.add(getApplicationContext().getResources().getIdentifier("img"+ i, "drawable", getPackageName()));
            //리소스 획득 (R.drawable.img0)
        }
        setContentView(R.layout.activity_main);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("그림을 골라주세요");

        Button button = (Button) findViewById(R.id.button);
        imgView=(ImageView)findViewById(R.id.imageView);
        text=(EditText)findViewById(R.id.textView);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v){
                Intent intent = new Intent(getApplicationContext(), SecondActivity.class);
                if(idx==-1)
                {
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    galleryBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    byte[] byteArray = stream.toByteArray();

                    intent.putExtra("idx",idx);
                    intent.putExtra("img",byteArray);
                }
                else
                {
                    intent.putExtra("idx",idx);
                    intent.putExtra("img",imgs.get(idx));
                }
                startActivity(intent);
                finish();
            }
        });
        Button nextbt = (Button) findViewById(R.id.nextbt);
        nextbt.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v){
                //ImageView imgView=(ImageView)findViewById(R.id.imageView);
                //EditText text=(EditText)findViewById(R.id.textView);
                if(idx+1<imgs.size())
                {
                    imgView.setImageResource(imgs.get(++idx));
                    text.setText(names.get(idx));
                }
                else
                {
                    idx=0;
                    imgView.setImageResource(imgs.get(idx));
                    text.setText(names.get(idx));
                }
            }
        });
        Button prebt = (Button) findViewById(R.id.previousbt);
        prebt.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v){
                //ImageView imgView=(ImageView)findViewById(R.id.imageView);
                //EditText text=(EditText)findViewById(R.id.textView);
                if(idx-1>-1)
                {
                    imgView.setImageResource(imgs.get(--idx));
                    text.setText(names.get(idx));
                }
                else
                {
                    idx=imgs.size()-1;
                    imgView.setImageResource(imgs.get(idx));
                    text.setText(names.get(idx));
                }
            }
        });
        Button gallerybt=(Button)findViewById(R.id.gallerybt);
        gallerybt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 1);
            }
        });



    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                try {
                    // 선택한 이미지에서 비트맵 생성
                    InputStream in = getContentResolver().openInputStream(data.getData());
                    galleryBitmap = BitmapFactory.decodeStream(in);
                    in.close();
                    // 이미지 표시
                    imgView.setImageBitmap(galleryBitmap);
                    text.setText("사용자 선택");
                    idx=-1;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}