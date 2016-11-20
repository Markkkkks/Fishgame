package com.example.zeus.fishgame.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Message;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import com.example.zeus.fishgame.R;
import com.example.zeus.fishgame.constant.ConstantUtil;
import com.example.zeus.fishgame.factory.GameObjectFactory;
import com.example.zeus.fishgame.object.EnermyFish;
import com.example.zeus.fishgame.object.GameObject;
import com.example.zeus.fishgame.object.MyFish;
import com.example.zeus.fishgame.object.testFish;
import com.example.zeus.fishgame.sounds.GameSoundPool;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zeus on 2016/11/15.
 */
public class MainView extends BaseView {
    private int missileCount; 		// 导弹的数量
    private int middlePlaneScore;	// 中型敌机的积分
    private int bigPlaneScore;		// 大型敌机的积分
    private int bossPlaneScore;		// boss型敌机的积分
    private int missileScore;		// 导弹的积分
    private int bulletScore;		// 子弹的积分
    private int sumScore;			// 游戏总得分
    private int speedTime;			// 游戏速度的倍数
    private float bg_y;				// 图片的坐标
    private float bg_y2;
    private float play_bt_w;
    private float play_bt_h;
    private float missile_bt_y;
    private boolean isPlay;			// 标记游戏运行状态
    private boolean isTouchPlane;	// 判断玩家是否按下屏幕
    private Bitmap background; 		// 背景图片
    private Bitmap background2; 	// 背景图片
    private Bitmap playButton; 		// 开始/暂停游戏的按钮图片
    private Bitmap missile_bt;		// 导弹按钮图标
    private MyFish myPlane;		// 玩家的飞机
    private GameObjectFactory factory;
    private List<EnermyFish> enemyFishs;
    public MainView(Context context,GameSoundPool sounds) {
        super(context,sounds);
        // TODO Auto-generated constructor stub
        isPlay = true;
        speedTime = 1;
        factory = new GameObjectFactory();						  //工厂类
        myPlane = (MyFish) factory.createMyPlane(getResources());//生产玩家的飞机
        myPlane.setMainView(this);
        thread = new Thread(this);
        sumScore=5000;
        enemyFishs = new ArrayList<EnermyFish>();
        for(int i = 0;i < testFish.sumCount;i++){
            //生产小型敌机
            testFish smallPlane = (testFish) factory.createtestFish(getResources());
            enemyFishs.add(smallPlane);
        }
    }

    // 视图改变的方法
    @Override
    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
        // TODO Auto-generated method stub
        super.surfaceChanged(arg0, arg1, arg2, arg3);
    }

    // 视图创建的方法
    @Override
    public void surfaceCreated(SurfaceHolder arg0) {
        // TODO Auto-generated method stub
        super.surfaceCreated(arg0);
        initBitmap(); // 初始化图片资源
        myPlane.setScreenWH(screen_width,screen_height);
        myPlane.setAlive(true);
        for(GameObject obj:enemyFishs){
            obj.setScreenWH(screen_width,screen_height);
        }
        myPlane.setScreenWH(screen_width,screen_height);
        myPlane.setAlive(true);
        if(thread.isAlive()){
            thread.start();
        }
        else{
            thread = new Thread(this);
            thread.start();
        }
    }// 视图销毁的方法
    @Override
    public void surfaceDestroyed(SurfaceHolder arg0) {
        // TODO Auto-generated method stub
        super.surfaceDestroyed(arg0);
        release();
    }
    // 初始化图片资源方法
    @Override
    public void initBitmap() {
        // TODO Auto-generated method stub
        playButton = BitmapFactory.decodeResource(getResources(),R.drawable.play);
        background = BitmapFactory.decodeResource(getResources(), R.drawable.bg_01);
        scalex = screen_width / background.getWidth();
        scaley = screen_height / background.getHeight();
        play_bt_w = playButton.getWidth();
        play_bt_h = playButton.getHeight()/2;
        bg_y = 0;
        bg_y2 = bg_y - screen_height;


    }
    //初始化游戏对象
    public void initObject() {
        for (EnermyFish obj : enemyFishs) {
            //初始化小型敌机
            if (obj instanceof testFish) {
                if (!obj.isAlive()) {
                    obj.initial(speedTime, 0, 0);
                    break;
                }
            }
            //提升等级
            if (sumScore >= speedTime * 10000 && speedTime < 8) {
                speedTime++;
            }

        }
    }
    @Override
    public void release() {
        // TODO Auto-generated method stub

        myPlane.release();
        if(!playButton.isRecycled()){
            playButton.recycle();
        }
        if(!background.isRecycled()){
            background.recycle();
        }


        for(GameObject obj:enemyFishs){
            obj.release();
        }

    }
    // 绘图方法
    @Override
    public void drawSelf() {
        // TODO Auto-generated method stub
        try {
            canvas = sfh.lockCanvas();
            canvas.drawColor(Color.BLACK); // 绘制背景色
            canvas.save();
            canvas.scale(scalex, scaley, 0, 0);					// 计算背景图片与屏幕的比例
            canvas.drawBitmap(background, 0, 0, paint); 		// 绘制背景图
            canvas.restore();
            //绘制按钮
            canvas.save();
            canvas.clipRect(10, 10, 10 + play_bt_w,10 + play_bt_h);
            if(isPlay){
                canvas.drawBitmap(playButton, 10, 10, paint);
            }
            else{
                canvas.drawBitmap(playButton, 10, 10 - play_bt_h, paint);
            }
            canvas.restore();

//绘制敌机
            for(EnermyFish obj:enemyFishs){
                if(obj.isAlive()){
                    obj.drawSelf(canvas);
                    //检测敌机是否与玩家的飞机碰撞
                    if(obj.isCanCollide() && myPlane.isAlive()){
                        if(obj.isCollide(myPlane)){
                            if(0==1) {
                                obj.setAlive(false);
                                sumScore += 100;
                            }
                            else
                                myPlane.setAlive(false);
                        }

                    }
                }
            }
            if(!myPlane.isAlive()){
                threadFlag = false;
                sounds.playSound(4, 0);			//飞机炸毁的音效
            }
            myPlane.drawSelf(canvas);	//绘制玩家的飞机
 //           sounds.playSound(1, 0);	  //子弹音效

            //绘制积分文字
            paint.setTextSize(30);
            paint.setColor(Color.rgb(235, 161, 1));
            canvas.drawText("积分:"+String.valueOf(sumScore), 30 + play_bt_w, 40, paint);		//绘制文字
            canvas.drawText("等级 X "+String.valueOf(speedTime), screen_width - 150, 40, paint); //绘制文字
        } catch (Exception err) {
            err.printStackTrace();
        } finally {
            if (canvas != null)
                sfh.unlockCanvasAndPost(canvas);
        }
    }

    public void playSound(int key){
        sounds.playSound(key, 0);
    }
    // 线程运行的方法
    @Override
    public void run() {
        // TODO Auto-generated method stub
        while (threadFlag) {
            long startTime = System.currentTimeMillis();
            initObject();
            drawSelf();

            long endTime = System.currentTimeMillis();
            if(!isPlay){
                synchronized (thread) {
                    try {
                        thread.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            try {
                if (endTime - startTime < 100)
                    Thread.sleep(100 - (endTime - startTime));
            } catch (InterruptedException err) {
                err.printStackTrace();
            }
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Message message = new Message();
        message.what = 	ConstantUtil.TO_END_VIEW;
        message.arg1 = Integer.valueOf(sumScore);
        mainActivity.getHandler().sendMessage(message);
    }
    // 响应触屏事件的方法
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_UP){
            isTouchPlane = false;
            return true;
        }
        else if(event.getAction() == MotionEvent.ACTION_DOWN) {
            float x = event.getX();
            float y = event.getY();
            if (x > 10 && x < 10 + play_bt_w && y > 10 && y < 10 + play_bt_h) {
                if (isPlay) {
                    isPlay = false;
                } else {
                    isPlay = true;
                    synchronized (thread) {
                        thread.notify();
                    }
                }
                return true;
            }
            //判断玩家飞机是否被按下
            else if (x > myPlane.getObject_x() && x < myPlane.getObject_x() + myPlane.getObject_width()
                    && y > myPlane.getObject_y() && y < myPlane.getObject_y() + myPlane.getObject_height()) {
                if (isPlay) {
                    isTouchPlane = true;
                }
                return true;
            }
        }
        //响应手指在屏幕移动的事件
        else if(event.getAction() == MotionEvent.ACTION_MOVE && event.getPointerCount() == 1){
            //判断触摸点是否为玩家的飞机
            if(isTouchPlane){
                float x = event.getX();
                float y = event.getY();
                if(x > myPlane.getMiddle_x() + 20){
                    if(myPlane.getMiddle_x() + myPlane.getSpeed() <= screen_width){
                        myPlane.setLoR(true);
                        myPlane.setMiddle_x(myPlane.getMiddle_x() + myPlane.getSpeed());

                    }
                }
                else if(x < myPlane.getMiddle_x() - 20){
                    if(myPlane.getMiddle_x() - myPlane.getSpeed() >= 0){
                        myPlane.setLoR(false);
                        myPlane.setMiddle_x(myPlane.getMiddle_x() - myPlane.getSpeed());
                    }
                }
                if(y > myPlane.getMiddle_y() + 20){
                    if(myPlane.getMiddle_y() + myPlane.getSpeed() <= screen_height){
                        myPlane.setMiddle_y(myPlane.getMiddle_y() + myPlane.getSpeed());
                    }
                }
                else if(y < myPlane.getMiddle_y() - 20){
                    if(myPlane.getMiddle_y() - myPlane.getSpeed() >= 0){
                        myPlane.setMiddle_y(myPlane.getMiddle_y() - myPlane.getSpeed());
                    }
                }
                return true;
            }
        }
        return false;
    }
}
