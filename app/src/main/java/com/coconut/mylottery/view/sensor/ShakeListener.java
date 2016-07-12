package com.coconut.mylottery.view.sensor;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Vibrator;

/**
 * Created by Administrator on 2016/7/7 0007.
 */
public abstract class ShakeListener implements SensorEventListener {

    private float lastX;
    private float lastY;
    private float lastZ;
    private long lastTime;
    private int duration = 200;
    private int total;
    private Context context;
    private Vibrator vibrator;

    public ShakeListener(Context context) {
        this.context = context;
        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
    }

    /**
     * 判断手机摇晃的阈值
     */
    private int switchValue = 200;


    @Override
    public void onSensorChanged(SensorEvent event) {
        //判断第一个点
        if (lastTime == 0) {
            lastX = event.values[SensorManager.DATA_X];
            lastY = event.values[SensorManager.DATA_Y];
            lastZ = event.values[SensorManager.DATA_Z];
            lastTime = System.currentTimeMillis();
        } else {
            //第二个点及以后
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastTime > duration) {
                float x = event.values[SensorManager.DATA_X];
                float y = event.values[SensorManager.DATA_Y];
                float z = event.values[SensorManager.DATA_Z];

                // 屏蔽掉微小的增量

                float dx = Math.abs(x - lastX);
                float dy = Math.abs(y - lastY);
                float dz = Math.abs(z - lastZ);

                if (dx < 1) {
                    dx = 0;
                }
                if (dy < 1) {
                    dy = 0;
                }
                if (dz < 1) {
                    dz = 0;
                }
                // 一点和二点总增量
                float shake = dx + dy + dz;

                if (shake == 0) {
                    init();
                }
                total += shake;
                if (total >= switchValue) {
                    //机选一注彩票
                    randomCure();
                    //提示用户
                    vibrator.vibrate(200);

                    //所有数据初始化
                    init();
                } else {
                    lastX = event.values[SensorManager.DATA_X];
                    lastY = event.values[SensorManager.DATA_Y];
                    lastZ = event.values[SensorManager.DATA_Z];
                    lastTime = System.currentTimeMillis();
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {


    }

    public abstract void randomCure();
    private void init() {
        lastX = 0;
        lastY = 0;
        lastZ = 0;
        lastTime = 0;
        total = 0;
    }
}
