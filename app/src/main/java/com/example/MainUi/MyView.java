package com.example.MainUi;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;


public class MyView extends View {
    private Bitmap bitmap;
    private Canvas canvas;
    private Path path;
    private Paint bitmapPaint;
    private Paint paint;
    private int myvW=0,myvH=0;
    private Bitmap originalbitmap;

    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);

        /*Resources res = getResources();
        bitmap = BitmapFactory.decodeResource(res, R.drawable.emptaegeuk);
        bitmap=Bitmap.createScaledBitmap(bitmap, 2050, 750, false);*/

        path = new Path();
        bitmapPaint = new Paint(Paint.DITHER_FLAG);

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(15);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        /*Resources res = getResources();
        bitmap = BitmapFactory.decodeResource(res, R.drawable.emptaegeuk);
        bitmap=Bitmap.createScaledBitmap(bitmap, 2050, 750, false);*/
        canvas = new Canvas(bitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(bitmap, 0, 0, bitmapPaint);
        canvas.drawPath(path, paint);

    }

    private float mX, mY;
    private static final float TOUCH_TOLERANCE = 4;

    private void touchStart(float x, float y) {
        path.reset();
        path.moveTo(x, y);
        mX = x;
        mY = y;
    }

    private void touchMove(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            path.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
            mX = x;
            mY = y;
        }
    }

    private void touchUp() {
        path.lineTo(mX, mY);
        // commit the path to our offscreen
        canvas.drawPath(path, paint);
        // kill this so we don't double draw
        path.reset();
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchStart(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                touchMove(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                touchUp();
                invalidate();
                break;
        }
        return true;
    }

    /*public Bitmap getBitmap() {
        this.setDrawingCacheEnabled(true);
        this.buildDrawingCache();
        Bitmap bmp = Bitmap.createBitmap(this.getDrawingCache());
        this.setDrawingCacheEnabled(false);

        return bmp;
    }*/

    //Clear screen
    public void clear() {
        //bitmap.eraseColor(Color.WHITE);
        Resources res = getResources();
        bitmap = originalbitmap.copy(originalbitmap.getConfig(),true);
        bitmap=grayScale(bitmap);
        bitmap=Bitmap.createScaledBitmap(bitmap, myvW, myvH, true);
        canvas=new Canvas(bitmap);
        invalidate();
        System.gc();
    }

    /**
     * change path color here
     */
    public void setPathColor(int color) {
        paint.setColor(color);
    }

    public void setPaintWidth(int w) {
        paint.setStrokeWidth(w);
    }

    //화면 크기에 맞추기
    public void setWH(int W,int H)
    {
        myvW=W;
        myvH=H;
        bitmap=Bitmap.createScaledBitmap(bitmap, myvW, myvH, true);
        canvas=new Canvas(bitmap);
    }

    //캔버스 배경 캐릭터 사진으로 설정
    public void setBitmap(int imgs)
    {
        Resources res = getResources();
        bitmap = BitmapFactory.decodeResource(res, imgs);
        originalbitmap=BitmapFactory.decodeResource(res, imgs);
        //bitmap=Bitmap.createScaledBitmap(bitmap, 1800, 1800, true);
        bitmap=grayScale(bitmap);
        bitmap=Bitmap.createScaledBitmap(bitmap, 1000, 1500, true);
        //bitmap=grayScale(bitmap);
        canvas=new Canvas(bitmap);
        //canvas.drawBitmap(bitmap, 0, 0, bitmapPaint);
    }

    public void setBitmap(Bitmap bm)
    {
        bitmap = bm.copy(bm.getConfig(),true);
        originalbitmap = bm.copy(bm.getConfig(),true);
        bitmap=grayScale(bitmap);
        bitmap=Bitmap.createScaledBitmap(bitmap, 1000, 1500, true);
        canvas=new Canvas(bitmap);
    }



    // 테두리 변환
    private Bitmap grayScale(final Bitmap orgBitmap){
        Log.i("gray", "in");
        int width, height;
        width = orgBitmap.getWidth();
        height = orgBitmap.getHeight();

        Bitmap bmpGrayScale = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        // color information
        int R, G, B;
        int pixel;

        // scan through all pixels
        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                // get pixel color
                pixel = orgBitmap.getPixel(x, y);
                R = Color.red(pixel);
                G = Color.green(pixel);
                B = Color.blue(pixel);
                int RGB=R+G+B;
                if(x>0&&x<width-1&&y>0&&y<height-1)
                {
                    int pixelup=orgBitmap.getPixel(x,y-1);
                    int upRGB=Color.red(pixelup)+Color.green(pixelup)+Color.blue(pixelup);
                    int pixeldown=orgBitmap.getPixel(x,y+1);
                    int downRGB=Color.red(pixeldown)+Color.green(pixeldown)+Color.blue(pixeldown);
                    int pixelL=orgBitmap.getPixel(x-1,y);
                    int LRGB=Color.red(pixelL)+Color.green(pixelL)+Color.blue(pixelL);
                    int pixelR=orgBitmap.getPixel(x+1,y);
                    int RRGB=Color.red(pixelR)+Color.green(pixelR)+Color.blue(pixelR);
                    int pixelul=orgBitmap.getPixel(x-1,y-1);
                    int ulRGB=Color.red(pixelul)+Color.green(pixelul)+Color.blue(pixelul);
                    int pixelur=orgBitmap.getPixel(x+1,y-1);
                    int urRGB=Color.red(pixelur)+Color.green(pixelur)+Color.blue(pixelur);
                    int pixeldl=orgBitmap.getPixel(x-1,y+1);
                    int dlRGB=Color.red(pixeldl)+Color.green(pixeldl)+Color.blue(pixeldl);
                    int pixeldr=orgBitmap.getPixel(x+1,y+1);
                    int drRGB=Color.red(pixeldr)+Color.green(pixeldr)+Color.blue(pixeldr);
                    if(Math.abs(RGB-upRGB)>30||Math.abs(RGB-RRGB)>30||Math.abs(RGB-LRGB)>30||Math.abs(RGB-downRGB)>30
                            ||Math.abs(RGB-ulRGB)>30||Math.abs(RGB-urRGB)>30||Math.abs(RGB-dlRGB)>30||Math.abs(RGB-drRGB)>30)
                    {
                        bmpGrayScale.setPixel(x, y, Color.argb(115, 0, 0, 0));
                    }

                }
            }
        }
        return bmpGrayScale;
    }

}
