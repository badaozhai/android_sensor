package com.chenjian.android_sensor;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private static String TAG = "SENSOR_DEMO";

    private TextView ms_sensor;

    private SensorManager sm;

    private TextView ms_x,ms_y,ms_z;

    private Sensor mSensorOrientation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        ms_sensor = (TextView) findViewById(R.id.ms_sensor);

        List<Sensor> allSensors = sm.getSensorList(Sensor.TYPE_ALL);
        StringBuilder sb = new StringBuilder();

        sb.append("你手机上有以下 " + allSensors.size() + " 个传感器有：\n\n");

        for (Sensor s:allSensors){
            switch (s.getType()){
                case Sensor.TYPE_ACCELEROMETER:
                    sb.append(s.getType() + " 加速度传感器(Accelerometer sensor)" + "\n");
                    break;
                case Sensor.TYPE_GYROSCOPE:
                    sb.append(s.getType() + " 陀螺仪传感器(Gyroscope sensor)" + "\n");
                    break;
                case Sensor.TYPE_LIGHT:
                    sb.append(s.getType() + " 光线传感器(Light sensor)" + "\n");
                    break;
                case Sensor.TYPE_MAGNETIC_FIELD:
                    sb.append(s.getType() + " 磁场传感器(Magnetic field sensor)" + "\n");
                    break;
                case Sensor.TYPE_ORIENTATION:
                    sb.append(s.getType() + " 方向传感器(Orientation sensor)" + "\n");
                    break;
                case Sensor.TYPE_PRESSURE:
                    sb.append(s.getType() + " 气压传感器(Pressure sensor)" + "\n");
                    break;
                case Sensor.TYPE_PROXIMITY:
                    sb.append(s.getType() + " 距离传感器(Proximity sensor)" + "\n");
                    break;
                case Sensor.TYPE_TEMPERATURE:
                    sb.append(s.getType() + " 温度传感器(Temperature sensor)" + "\n");
                    break;
                default:
                    sb.append(s.getType() + " 其他传感器" + "\n");
                    break;
            }
            sb.append("设备名称：" + s.getName() + "\n设备版本：" + s.getVersion() + "\n供应商："
                    + s.getVendor() + "\n\n");
        }
        //ms_sensor.setText(sb.toString());
        Log.v(TAG,sb.toString());

        // 获取方向传感器
        // TYPE_ORIENTATION     3
//        mSensorOrientation = sm.getDefaultSensor(Sensor.TYPE_ORIENTATION);
//        //注册数值变化监听器
//        sm.registerListener(this, mSensorOrientation,SensorManager.SENSOR_DELAY_UI);

        // 获取加速度传感器
        // TYPE_ACCELEROMETER       1
        mSensorOrientation = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        //注册数值变化监听器
        sm.registerListener(this, mSensorOrientation,SensorManager.SENSOR_DELAY_UI);

        ms_x = (TextView) findViewById(R.id.ms_x);
        ms_y = (TextView) findViewById(R.id.ms_y);
        ms_z = (TextView) findViewById(R.id.ms_z);

    }
    private int step = 0;   //步数
    private double oriValue = 0;  //原始值
    private double lstValue = 0;  //上次的值
    private double curValue = 0;  //当前值
    private boolean motiveState = true;   //是否处于运动状态
    private boolean processState = false;   //标记当前是否已经在计步
    @RequiresApi(api = Build.VERSION_CODES.KITKAT_WATCH)
    @Override
    public void onSensorChanged(SensorEvent event) {

        //数据的精度
        int accuracy = event.accuracy;//精度
        Log.v(TAG,"数据精度:"+accuracy);
        //产生数据的传感器
        Sensor ss = event.sensor;
        Log.v(TAG,"传感器:"+ss.toString());
        Log.v(TAG,"传感器类型:"+ss.getStringType());
        // 产生数据时的时间戳
        long timestamp = event.timestamp;
        Log.v(TAG,"时间戳:"+timestamp);


        // 以及传感器记录的新的数据。
        Log.v(TAG,"方位角：" + (float) (Math.round(event.values[0] * 100)) / 100);
        ms_x.setText("方位角：" + (float) (Math.round(event.values[0] * 100)) / 100);

        Log.v(TAG,"倾斜角：" + (float) (Math.round(event.values[1] * 100)) / 100);
        ms_y.setText("倾斜角：" + (float) (Math.round(event.values[1] * 100)) / 100);

        Log.v(TAG,"滚动角：" + (float) (Math.round(event.values[2] * 100)) / 100);
        ms_z.setText("滚动角：" + (float) (Math.round(event.values[2] * 100)) / 100);


        //设定一个精度范围
        double range = 1;
        float[] value = event.values;

        //计算当前的模
        curValue = magnitude(value[0], value[1], value[2]);

        //向上加速的状态
        if (motiveState == true) {
            if (curValue >= lstValue) lstValue = curValue;
            else {
                //检测到一次峰值
                if (Math.abs(curValue - lstValue) > range) {
                    oriValue = curValue;
                    motiveState = false;
                }
            }
        }
        //向下加速的状态
        if (motiveState == false) {
            if (curValue <= lstValue) lstValue = curValue;
            else {
                if (Math.abs(curValue - lstValue) > range) {
                    //检测到一次峰值
                    oriValue = curValue;
                    if (processState == true) {
                        step++;  //步数 + 1
                        if (processState == true) {
                            Log.v(TAG,step + "");    //读数更新
                        }
                    }
                    motiveState = true;
                }
            }
        }

    }
    //向量求模
    public double magnitude(float x, float y, float z) {
        double magnitude = 0;
        magnitude = Math.sqrt(x * x + y * y + z * z);
        return magnitude;
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        sm.unregisterListener(this);
    }
}
