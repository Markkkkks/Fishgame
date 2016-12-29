package com.example.zeus.fishgame.object;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;

import com.example.zeus.fishgame.R;
import com.example.zeus.fishgame.factory.GameObjectFactory;
import com.example.zeus.fishgame.interfaces.IMyFish;
import com.example.zeus.fishgame.view.MainView;

/**
 * Created by zeus on 2016/11/15.
 */
public class MyFish extends GameObject implements IMyFish{
    private float middle_x;			 // 飞机的中心坐标
    private float middle_y;
    private Bitmap myplane;			 // 飞机飞行时的图片
    private Bitmap myplane2;		 // 飞机爆炸时的图片
    private Bitmap myplaneR;         // 飞机反转
    private float resize=0.5f;
    private MainView mainView;
    private boolean LoR;
    private GameObjectFactory factory;
    public MyFish(Resources resources) {
        super(resources);
        // TODO Auto-generated constructor stub
        initBitmap();
        this.speed = 8;

        factory = new GameObjectFactory();
    }
    public void setMainView(MainView mainView) {
        this.mainView = mainView;
    }
    // 设置屏幕宽度和高度
    @Override
    public void setScreenWH(float screen_width, float screen_height) {
        super.setScreenWH(screen_width, screen_height);
        object_x = screen_width/2 - object_width/2;
        object_y = screen_height/2 - object_height/2;
        middle_x = object_x + object_width/2;
        middle_y = object_y + object_height/2;
    }



    // 初始化图片资源的
    @Override
    public void initBitmap() {
        // TODO Auto-generated method stub
        myplane = BitmapFactory.decodeResource(resources, R.drawable.myplane);
        //myplane=resize(myplane,resize);
        myplane= Bitmap.createBitmap(myplane, 0, 0, myplane.getWidth(), myplane.getHeight(), null, false);
        //反转后的鱼
        myplaneR = convert(myplane);
        myplane2 = BitmapFactory.decodeResource(resources, R.drawable.myplaneexplosion);
        object_width = myplane.getWidth() ; // 获得每一帧位图的宽
        object_height = myplane.getHeight(); 	// 获得每一帧位图的高
        LoR=false;
    }
    //反转图片函数
    Bitmap convert(Bitmap a)
    {
        int w = a.getWidth();
        int h = a.getHeight();
        Bitmap newb = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);// 创建一个新的和SRC长度宽度一样的位图
        Canvas cv = new Canvas(newb);
        Matrix m = new Matrix();
//        m.postScale(1, -1);   //镜像垂直翻转
        m.postScale(-1, 1);   //镜像水平翻转
//        m.postRotate(-180);  //旋转-90度
        Bitmap new2 = Bitmap.createBitmap(a, 0, 0, w, h, m, true);
        cv.drawBitmap(new2, new Rect(0, 0, new2.getWidth(), new2.getHeight()),new Rect(0, 0, w, h), null);
        return newb;
    }
    private static Bitmap resize(Bitmap bitmap,float x) {
        Matrix matrix = new Matrix();
        matrix.postScale(1.0f*x,1.0f*x); //长和宽放大缩小的比例
        Bitmap resizeBmp = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
        return resizeBmp;
    }



    // 对象的绘图方法
    @Override
    public void drawSelf(Canvas canvas) {
        // TODO Auto-generated method stub
        if(isAlive){
            int x = (int) (currentFrame * object_width); // 获得当前帧相对于位图的X坐标
            canvas.save();
            canvas.clipRect(object_x, object_y, object_x + (int)(object_width * resize ), object_y + (int)(object_height * resize) );
            //鱼的反转
            if(!LoR)
                canvas.drawBitmap(myplane, new Rect(0, 0, (int)object_width, (int)object_height),
                        new Rect((int)object_x, (int)object_y, (int)object_x + (int)(object_width * resize),  (int)(resize * object_height) + (int)object_y), paint);
                //canvas.drawBitmap(myplane, object_x , object_y, paint);
            else
                canvas.drawBitmap(myplaneR, new Rect(0, 0, (int)object_width, (int)object_height),
                        new Rect((int)object_x, (int)object_y, (int)object_x + (int)(object_width * resize),(int)(resize * object_height)+ (int)object_y), paint);

            canvas.restore();

        }
//        else{
//            int x = (int) (currentFrame * object_width); // 获得当前帧相对于位图的Y坐标
//            canvas.save();
//            canvas.clipRect(object_x, object_y, object_x + object_width, object_y
//                    + object_height);
//            canvas.drawBitmap(myplane2, object_x - x, object_y, paint);
//            canvas.restore();
//            currentFrame++;
//            if (currentFrame >= 2) {
//                currentFrame = 1;
//            }
//        }
    }
    // 释放资源的方法
    @Override
    public void release() {
        // TODO Auto-generated method stub

        if(!myplane.isRecycled()){
            myplane.recycle();
        }
        if(!myplane2.isRecycled()){
            myplane2.recycle();
        }
    }
    //getter和setter方法
    //public void setStartTime(long startTime) {
    //    this.startTime = startTime;
    //}
    @Override
    public float getMiddle_x() {
        return middle_x;
    }
    @Override
    public void setMiddle_x(float middle_x) {
        this.middle_x = middle_x;
        this.object_x = middle_x - object_width/2;
    }
    @Override
    public float getMiddle_y() {
        return middle_y;
    }
    @Override
    public void setMiddle_y(float middle_y) {
        this.middle_y = middle_y;
        this.object_y = middle_y - object_height/2;
    }

    public boolean isLoR() {
        return LoR;
    }

    public void setLoR(boolean loR) {
        LoR = loR;
    }

    public float getResize() {
        return resize;
    }

    public void setResize(float resize) {
        this.resize = resize;
    }
}