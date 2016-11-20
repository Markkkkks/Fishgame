package com.example.zeus.fishgame.object;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;

import com.example.zeus.fishgame.R;

import java.util.Random;

/**
 * Created by zeus on 2016/11/15.
 */
public class testFish extends EnermyFish {
    private static int currentCount = 0;	 //	对象当前的数量
    private Bitmap smallPlane; // 对象图片
    private Bitmap Smallplane1;
    private Bitmap Smallplane2;
    public static int sumCount = 8;	 	 	 //	对象总的数量
    private int randomsize;

    public testFish(Resources resources) {
        super(resources);
        // TODO Auto-generated constructor stub
        this.score = 100;		// 为对象设置分数
    }
    //初始化数据
    @Override
    public void initial(int arg0,float arg1,float arg2){
        isAlive = true;
        Random ran = new Random();
        LoR= ran.nextInt(100)>50?false:true;
        randomsize=ran.nextInt(8);
        speed = ran.nextInt(8) + 8 * arg0;
//		object_x = ran.nextInt((int)(screen_width - object_width));
//		object_y = -object_height * (currentCount*2 + 1);
        if(LoR==false) {
            object_y = ran.nextInt((int) (screen_height - object_height));
            object_x = -object_width * (currentCount * 2 + 1);
        }
        else {
            object_y = ran.nextInt((int) (screen_height - object_height));
            object_x = screen_width+object_width * (currentCount * 2 + 1);;
        }
        currentCount++;
        if(currentCount >= sumCount){
            currentCount = 0;
        }


    }
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

    // 初始化图片资源
    @Override
    public void initBitmap() {
        // TODO Auto-generated method stub
        smallPlane = BitmapFactory.decodeResource(resources, R.drawable.small);
        //取得左右不同的飞机

        Smallplane2= Bitmap.createBitmap(smallPlane, 0, 0, smallPlane.getWidth(), smallPlane.getHeight(), null, false);
        Smallplane1 = convert(Smallplane2);
        object_width = smallPlane.getWidth();			//获得每一帧位图的宽
        object_height = smallPlane.getHeight();		//获得每一帧位图的高
        //smallplane1 =Bitmap.createBitmap(smallPlane,0,0,object_width,object_height,paint);
    }
    //调整出现的敌方大小
    private static Bitmap resize(Bitmap bitmap,float x) {
        Matrix matrix = new Matrix();
        matrix.postScale(1.0f*x,1.0f*x); //长和宽放大缩小的比例
        Bitmap resizeBmp = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
        return resizeBmp;
    }
    // 对象的绘图函数
    @Override
    public void drawSelf(Canvas canvas) {
        // TODO Auto-generated method stub
        //判断敌机是否死亡状态
        if(isAlive){
            //判断敌机是否为爆炸状态
            if(!isExplosion){
                if(isVisible){
                    canvas.save();
                    canvas.clipRect(object_x,object_y,object_x + object_width,object_y + object_height);
                    if(!LoR) {

                        canvas.drawBitmap(Smallplane1, object_x, object_y, paint);
                    }
                    else

                        canvas.drawBitmap(Smallplane2, object_x, object_y,paint);
                    canvas.restore();
                }
                logic();
            }
            else{
                int y = (int) (currentFrame * object_height); // 获得当前帧相对于位图的Y坐标
                canvas.save();
                canvas.clipRect(object_x,object_y,object_x + object_width,object_y + object_height);
               if(LoR)
                canvas.drawBitmap(smallPlane, object_x, object_y - y,paint);
                canvas.restore();
                currentFrame++;
                if(currentFrame >= 3){
                    currentFrame = 0;
                    isExplosion = false;
                    isAlive = false;
                }
            }
        }
    }
    // 释放资源
    @Override
    public void release() {
        // TODO Auto-generated method stub
        if(!smallPlane.isRecycled()){
            smallPlane.recycle();
        }
    }
}
